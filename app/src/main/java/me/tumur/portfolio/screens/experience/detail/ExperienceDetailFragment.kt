package me.tumur.portfolio.screens.experience.detail

import android.content.res.Configuration
import android.location.Address
import android.location.Geocoder
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import me.tumur.portfolio.R
import me.tumur.portfolio.databinding.FragmentExperienceDetailBinding
import me.tumur.portfolio.repository.database.model.LocationModel
import me.tumur.portfolio.repository.database.model.button.ButtonModel
import me.tumur.portfolio.repository.database.model.resource.ResourceModel
import me.tumur.portfolio.repository.database.model.task.TaskModel
import me.tumur.portfolio.utils.adapters.listItemAdapters.experience.resource.ResourceAdapter
import me.tumur.portfolio.utils.adapters.listItemAdapters.experience.task.TaskAdapter
import me.tumur.portfolio.utils.adapters.listItemAdapters.portfolio.button.ButtonAdapter
import me.tumur.portfolio.utils.adapters.listItemAdapters.portfolio.button.ButtonClickListener
import me.tumur.portfolio.utils.adapters.bindingAdapters.loadImage
import me.tumur.portfolio.utils.adapters.bindingAdapters.setDateFromTo
import me.tumur.portfolio.utils.adapters.bindingAdapters.setScreenFavoriteNotEmpty
import me.tumur.portfolio.utils.constants.Constants
import me.tumur.portfolio.utils.extensions.collectFlow
import me.tumur.portfolio.utils.extensions.launchCustomTab
import me.tumur.portfolio.utils.state.Empty
import me.tumur.portfolio.utils.state.NotEmpty
import dagger.hilt.android.AndroidEntryPoint
import java.io.IOException

/**
 * An fragment that inflates a portfolio detail layout.
 */
@AndroidEntryPoint
class ExperienceDetailFragment : Fragment(), OnMapReadyCallback, GoogleMap.OnMarkerClickListener {
    /** VARIABLES * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

    /** Create a new instance */
    companion object {
        fun newInstance() = ExperienceDetailFragment()
    }

    /** ViewModel */

    /**
     * Lazily create a ViewModel the first time the system calls an activity's onCreate() method.
     * Re-created fragments receive the same ViewModel instance created by the parent fragment.
     * */
    private val viewModel: ExperienceDetailFragmentViewModel by viewModels()

    /**
     * Databinding
     */
    private lateinit var binding: FragmentExperienceDetailBinding

    /** Safe args */
    private val args: ExperienceDetailFragmentArgs by navArgs()

    /** Map */
    private var mMap: GoogleMap? = null

    /** INITIALIZATION * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

    /**
     * Called to have the fragment instantiate its user interface view.
     *
     * <p>If you return a View from here, you will later be called in
     * {@link #onDestroyView} when the view is being released.
     *
     * @param inflater The LayoutInflater object that can be used to inflate
     * any views in the fragment,
     * @param container If non-null, this is the parent view that the fragment's
     * UI should be attached to.  The fragment should not add the view itself,
     * but this can be used to generate the LayoutParams of the view.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     * from a previous saved state as given here.
     *
     * @return Return the View for the fragment's UI.
     */

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentExperienceDetailBinding.inflate(inflater, container, false)

        /** Set experience item id for detail screen */
        args.id?.let {
            viewModel.setExperienceItemId(it)
        }

        /** Set map fragment */
        val mapFragment = childFragmentManager.findFragmentById(R.id.experience_item_detail_map) as SupportMapFragment?
        mapFragment?.getMapAsync(this)


        /** Set observers and adapters */
        setAdapters()

        return binding.root
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        val mapStyleDark = MapStyleOptions.loadRawResourceStyle(requireContext(), R.raw.map_style_dark)
        val mapStyle = MapStyleOptions.loadRawResourceStyle(requireContext(), R.raw.map_style)
        mMap?.let { map ->
            map.uiSettings.isZoomControlsEnabled = true
            map.setOnMarkerClickListener(this)
            if (activity?.resources?.configuration?.uiMode?.and(Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES) {
                map.setMapStyle(mapStyleDark)
            } else {
                map.setMapStyle(mapStyle)
            }
        }
    }

    override fun onMarkerClick(marker: Marker) = false

    /** FUNCTIONS * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

    /** Adapters */
    private fun setAdapters() {

        /** Set Button Adapter */
        val buttonAdapter = ButtonAdapter(ButtonClickListener(viewModel::setUrl))
        val layoutManager = GridLayoutManager(activity, 2)
        val buttonList = binding.experienceItemDetailButton

        buttonList.apply {
            this.layoutManager = layoutManager
            this.hasFixedSize()
            this.adapter = buttonAdapter
        }

        /** Set Task Adapter */
        val taskAdapter = TaskAdapter()
        val layoutManagerTask = LinearLayoutManager(context)
        layoutManagerTask.orientation = LinearLayoutManager.VERTICAL
        val taskList = binding.experienceItemDetailTask

        taskList.apply {
            this.layoutManager = layoutManagerTask
            this.hasFixedSize()
            this.adapter = taskAdapter
        }

        /** Set Resource Adapter */
        val resourceAdapter = ResourceAdapter(ButtonClickListener(viewModel::setUrl))
        val layoutManagerResource = LinearLayoutManager(context)
        layoutManagerResource.orientation = LinearLayoutManager.HORIZONTAL
        val resourceList = binding.experienceItemDetailResource

        resourceList.apply {
            this.layoutManager = layoutManagerResource
            this.hasFixedSize()
            this.adapter = resourceAdapter
        }

        /** Set observers */
        setObservers(buttonAdapter, taskAdapter, resourceAdapter)
    }

    /** Observers */
    private fun setObservers(buttonAdapter: ButtonAdapter, taskAdapter: TaskAdapter, resourceAdapter: ResourceAdapter) {

        /** Set observer for button */
        viewLifecycleOwner.collectFlow(viewModel.dataFlow) { item ->
            item ?: return@collectFlow
            binding.experienceItemDetailCoverImage.contentDescription = item.imageDescription
            loadImage(binding.experienceItemDetailCoverImage, item.coverImage)
            binding.experienceItemDetailLogo.contentDescription = item.logoDescription
            loadImage(binding.experienceItemDetailLogo, item.logo)
            binding.experienceItemDetailJobTitle.text = item.title
            binding.experienceItemDetailCompanyName.text = item.company
            binding.experienceItemDetailLocationName.text = item.location
            binding.experienceItemDetailDateToFrom.setDateFromTo(item.dateFrom, item.dateTo)
            binding.experienceItemDetailInfo.text = item.info
        }

        viewLifecycleOwner.collectFlow(viewModel.resourceStateFlow) { state ->
            setScreenFavoriteNotEmpty(binding.experienceItemDetailSectionResource, state)
            setScreenFavoriteNotEmpty(binding.experienceItemDetailResource, state)
        }

        viewLifecycleOwner.collectFlow(viewModel.button) { button ->
            buttonAdapter.submitData(viewLifecycleOwner.lifecycle, button)
        }

        /** Set observer for task */
        viewLifecycleOwner.collectFlow(viewModel.task) { task ->
            taskAdapter.submitData(viewLifecycleOwner.lifecycle, task)
        }

        /** Set observer for resource */
        viewLifecycleOwner.collectFlow(viewModel.resource) { resource ->
            resourceAdapter.submitData(viewLifecycleOwner.lifecycle, resource)
        }

        /** Set observer for check resource table */
        viewLifecycleOwner.collectFlow(viewModel.checkResourceTable) {
            if (it > 0) viewModel.setResourceState(NotEmpty) else viewModel.setResourceState(Empty)
        }

        /** Set observer for url */
        viewLifecycleOwner.collectFlow(viewModel.urlFlow) {
            it?.let {
                startCustomTab(it)
                viewModel.setUrl(null)
            }
        }

        /** Set observer for location */
        viewLifecycleOwner.collectFlow(viewModel.locationFlow) {
            it?.let { location ->
                location.latitude?.let { lat ->
                    location.longitude?.let { long ->

                        val currentLatLng = LatLng(lat, long)
                        resolveAddress(currentLatLng) { title ->
                            binding.root.post {
                                placeMarkerOnMap(title, currentLatLng)
                                mMap?.let { map ->
                                    map.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 12f))
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * Starts custom tab
     * as an new intent
     * */
    private fun startCustomTab(url: String?) {

        url?.let {
            requireContext().launchCustomTab(it)
        }
    }

    /** Place market on a map */
    private fun placeMarkerOnMap(title: String, location: LatLng) {
        val markerOptions = MarkerOptions().position(location)
        markerOptions.title(title)
        mMap?.let {
            it.addMarker(markerOptions)
        }
    }

    /** Resolve address */
    @Suppress("DEPRECATION")
    private fun resolveAddress(latLng: LatLng, onResolved: (String) -> Unit) {
        val geocoder = Geocoder(requireContext())
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            geocoder.getFromLocation(
                latLng.latitude,
                latLng.longitude,
                1,
                object : Geocoder.GeocodeListener {
                    override fun onGeocode(addresses: MutableList<Address>) {
                        onResolved(formatAddress(addresses.firstOrNull()))
                    }

                    override fun onError(errorMessage: String?) {
                        Log.d(Constants.FRAGMENT_EXPERIENCE, errorMessage.orEmpty())
                        onResolved("")
                    }
                }
            )
            return
        }

        try {
            onResolved(formatAddress(geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1)?.firstOrNull()))
        } catch (e: IOException) {
            Log.d(Constants.FRAGMENT_EXPERIENCE, e.localizedMessage.orEmpty())
            onResolved("")
        }
    }

    /** Format address */
    private fun formatAddress(address: Address?): String {
        return buildString {
            if (address != null) {
                for (i in 0 until address.maxAddressLineIndex) {
                    append(if (i == 0) address.getAddressLine(i) else "\n" + address.getAddressLine(i))
                }
            }
        }
    }
}

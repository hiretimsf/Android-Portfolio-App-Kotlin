package me.tumur.portfolio.screens.portfolio.detail.preview.pager

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import me.tumur.portfolio.utils.constants.Constants

class PreviewPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = 6

    override fun createFragment(position: Int): Fragment {
        return PreviewPagerFragment().apply {
            arguments = Bundle().apply {
                putInt(Constants.POSITION, position)
            }
        }
    }
}

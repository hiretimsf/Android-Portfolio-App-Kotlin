package me.tumur.portfolio.screens.welcome.pager

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import me.tumur.portfolio.utils.constants.Constants.POSITION

class WelcomePagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        return WelcomePagerFragment().apply {
            arguments = Bundle().apply {
                putInt(POSITION, position)
            }
        }
    }
}

package com.example.finaltask

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.example.finaltask.fragments.CurrentTrackFragment
import com.example.finaltask.fragments.FavoriteFragment
import com.example.finaltask.fragments.RandomFragment
import com.google.android.material.tabs.TabLayout


class MainPageActivity : AppCompatActivity(), RandomFragment.OnCurrentFragmentClickListener {
    private lateinit var mViewPager: ViewPager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_page)

        mViewPager = findViewById<ViewPager>(R.id.view_pager)
        val tabLayout = findViewById<TabLayout>(R.id.tab_layout)

        mViewPager.adapter = ViewPagerAdapter(supportFragmentManager)
        tabLayout.setupWithViewPager(mViewPager)

        mViewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {
            }

            override fun onPageScrolled(
                position: Int, positionOffset: Float,
                positionOffsetPixels: Int
            ) {
            }

            override fun onPageSelected(position: Int) {
                Toast.makeText(this@MainPageActivity, position.toString(), Toast.LENGTH_SHORT)
                    .show()
            }
        })
    }

    fun getViewPager(): ViewPager {
        return mViewPager
    }

    @Suppress("DEPRECATION")
    inner class ViewPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {
        override fun getItem(position: Int): Fragment {
            return when (position) {
                0 -> RandomFragment(this@MainPageActivity)
                1 -> CurrentTrackFragment()
                else -> FavoriteFragment()
            }
        }

        override fun getCount(): Int {
            return 3
        }

        override fun getPageTitle(position: Int): CharSequence? {
            return when (position) {
                0 -> {
                    "Рандом"
                }
                1 -> {
                    "Текущий"
                }
                else -> {
                    "Избранное"
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.main_settings -> {
                startActivity(Intent(this, SettingsActivity::class.java))
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onItemClick() {
        getViewPager().setCurrentItem(1, true)
    }
}
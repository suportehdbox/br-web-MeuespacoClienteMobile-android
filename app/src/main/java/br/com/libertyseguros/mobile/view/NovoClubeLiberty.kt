package br.com.libertyseguros.mobile.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import br.com.libertyseguros.mobile.R
import br.com.libertyseguros.mobile.view.baseActivity.BaseActionBar
import com.google.android.material.tabs.TabLayout
import com.tbuonomo.viewpagerdotsindicator.DotsIndicator
import com.tbuonomo.viewpagerdotsindicator.SpringDotsIndicator
import com.tbuonomo.viewpagerdotsindicator.WormDotsIndicator

class NovoClubeLiberty : BaseActionBar(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_novo_clube_liberty)
        val pager = findViewById<ViewPager2>(R.id.pager)

        val demoAdapter = DemoCollectionAdapter(this)
        pager.adapter = demoAdapter

        val dotsIndicator = findViewById<WormDotsIndicator>(R.id.dots_indicator)
        dotsIndicator.setViewPager2(pager)

    }
}

class DemoCollectionAdapter(activity: AppCompatActivity) : FragmentStateAdapter (activity) {

    val arrayLayout = listOf (R.layout.fragment_club_tutorial, R.layout.fragment_club_tutorial_2, R.layout.fragment_club_tutorial_3, R.layout.fragment_club_tutorial_4)


    override fun createFragment(position: Int): Fragment {
        val fragment = DemoObjectFragment()
        fragment.arguments = Bundle().apply {
            // Our object is just an integer :-P
            putInt(ARG_OBJECT, arrayLayout[position])
        }
        return fragment
    }
    override fun getItemCount(): Int {
        return arrayLayout.size
    }
}

private const val ARG_OBJECT = "layout"

// Instances of this class are fragments representing a single
// object in our collection.
class DemoObjectFragment : Fragment() {

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {

        return inflater.inflate(arguments!!.getInt(ARG_OBJECT), container, false)

    }

}


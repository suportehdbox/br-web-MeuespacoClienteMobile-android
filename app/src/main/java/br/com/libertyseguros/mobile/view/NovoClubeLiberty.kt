package br.com.libertyseguros.mobile.view

import android.graphics.PorterDuff
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import br.com.libertyseguros.mobile.R
import br.com.libertyseguros.mobile.controller.ClubController
import br.com.libertyseguros.mobile.view.baseActivity.BaseActionBar
import br.com.libertyseguros.mobile.viewmodel.NovoClubeViewModel
import com.tbuonomo.viewpagerdotsindicator.WormDotsIndicator


class NovoClubeLiberty : BaseActionBar(), View.OnClickListener{

    lateinit var clubController: ClubController
    private lateinit var btPrevious: ImageButton
    private lateinit var btNext: ImageButton
    private lateinit var pager:ViewPager2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_novo_clube_liberty)
        mFirebaseAnalytics.setCurrentScreen(this, getString(R.string.title_action_bar_11), null)

        setTitle(getString(R.string.title_action_bar_11))
        pager = findViewById<ViewPager2>(R.id.pager)

        btPrevious = findViewById<ImageButton>(R.id.bt_previous)
        btPrevious.setOnClickListener(this)
        btNext = findViewById<ImageButton>(R.id.bt_next)
        btNext.setOnClickListener(this)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            btPrevious.background.setColorFilter(getColor(R.color.blueDefault), PorterDuff.Mode.SRC_IN)
            btNext.background.setColorFilter(getColor(R.color.blueDefault), PorterDuff.Mode.SRC_IN)
        }else{
            btPrevious.background.setColorFilter(resources.getColor(R.color.blueDefault), PorterDuff.Mode.SRC_IN)
            btNext.background.setColorFilter(resources.getColor(R.color.blueDefault), PorterDuff.Mode.SRC_IN)
        }



        val demoAdapter = DemoCollectionAdapter(this)
        pager.adapter = demoAdapter
        pager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                updateButtonsAtPageIndex(position)

                super.onPageSelected(position)
            }
        })

        val dotsIndicator = findViewById<WormDotsIndicator>(R.id.dots_indicator)
        dotsIndicator.setViewPager2(pager)

        clubController = ClubController(this)
        clubController.checkTermsAlreadyAgreed(this)

        updateButtonsAtPageIndex(0)
    }

    private fun updateButtonsAtPageIndex(index: Int){
        btPrevious.visibility = View.VISIBLE
        btNext.visibility = View.VISIBLE
        when(index){
            0 -> {
                btPrevious.visibility = View.GONE
            }
            3 ->{
                btNext.visibility = View.GONE
            }
        }
    }

    override fun onClick(v: View?) {
        when(v!!.id){
            btPrevious.id -> {
                pager.currentItem = pager.currentItem-1
            }
            btNext.id ->{
                pager.currentItem = pager.currentItem+1
            }
        }
    }

}

class DemoCollectionAdapter(activity: AppCompatActivity) : FragmentStateAdapter (activity) {

    val arrayLayout = listOf (R.layout.fragment_club_tutorial, R.layout.fragment_club_tutorial_2, R.layout.fragment_club_tutorial_3, R.layout.fragment_club_tutorial_4)
    val hasViewMoldel = listOf(false,false,false,true)

    override fun createFragment(position: Int): Fragment {
        val fragment = TutorialStepFragment()
        fragment.arguments = Bundle().apply {
            // Our object is just an integer :-P
            putInt(ARG_OBJECT, arrayLayout[position])
            putBoolean(ARG_HAS_VM, hasViewMoldel[position])
        }
        return fragment
    }
    override fun getItemCount(): Int {
        return arrayLayout.size
    }
}

private const val ARG_OBJECT = "layout"
private const val ARG_HAS_VM = "HAS_VM"

// Instances of this class are fragments representing a single
// object in our collection.
class TutorialStepFragment : Fragment(), View.OnClickListener {

    private lateinit var mViewModel:NovoClubeViewModel
    private lateinit var checkbox:CheckBox
    private lateinit var llCheck: LinearLayout
    private lateinit var btLogin: Button
    private lateinit var btRegister: Button
    private lateinit var btOpen: Button
    private lateinit var txtClub: TextView
    private lateinit var txtTerms: TextView
    private lateinit var clubController:ClubController
    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {

        return inflater.inflate(arguments!!.getInt(ARG_OBJECT), container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if(arguments!!.getBoolean(ARG_HAS_VM)){
            val factory: ViewModelProvider.Factory = ViewModelProvider.AndroidViewModelFactory(activity!!.application!!)
            mViewModel = ViewModelProvider(this, factory).get(NovoClubeViewModel::class.java)
        }

        if(this::mViewModel.isInitialized){
            checkbox = view.findViewById(R.id.checkbox_clube)
            btOpen = view.findViewById(R.id.bt_acessar_clube)
            btLogin = view.findViewById(R.id.bt_login_club)
            txtTerms = view.findViewById(R.id.txt_terms)
            btRegister = view.findViewById(R.id.bt_register_club)
            llCheck = view.findViewById(R.id.ll_terms)
            btOpen = view.findViewById(R.id.bt_acessar_clube)
            txtClub = view.findViewById(R.id.txt_club)
            checkbox.setOnClickListener(this)
            btOpen.setOnClickListener(this)
            btLogin.setOnClickListener(this)
            btRegister.setOnClickListener(this)
            txtTerms.setOnClickListener(this)
            clubController = ClubController(activity)
            updateView()
        }
    }

    private fun updateView(){
        if (mViewModel.isLoggedIn()){
            btLogin.visibility = View.GONE
            btRegister.visibility = View.GONE
            btOpen.visibility = View.VISIBLE
            llCheck.visibility = View.VISIBLE
            checkbox.isChecked=mViewModel.isAgreed
            btOpen.isEnabled=mViewModel.buttonEnabled
            txtClub.text = getString(R.string.clube_final_logado)
        }else{
            btOpen.visibility = View.GONE
            llCheck.visibility = View.GONE
            btLogin.visibility = View.VISIBLE
            btRegister.visibility = View.VISIBLE
            txtClub.text = getString(R.string.clube_final_nao_logado)
        }

    }


    override fun onClick(v: View?) {
        when (v!!.id){
            txtTerms.id ->{
                clubController.openTerms(activity)
            }
            btOpen.id -> {
                mViewModel.agreedTerms()
                clubController.openClubWebview(activity)
                activity?.finish()
            }
            btLogin.id -> {
                clubController.openLogin(activity);
            }
            btRegister.id -> {
                clubController.openRegister(activity);
            }
            checkbox.id -> {
                mViewModel.isAgreed = checkbox.isChecked
                updateView()
            }
        }
    }
}


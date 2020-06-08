package br.com.libertyseguros.mobile.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentStatePagerAdapter
import br.com.libertyseguros.mobile.R

class NovoClubeLiberty : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_novo_clube_liberty)
    }
}
//
//class DemoCollectionAdapter(fragment: Fragment) : FragmentStatePagerAdapter(fragment) {
//
//    fun createFragment(position: Int): Fragment {
//        // Return a NEW fragment instance in createFragment(int)
//        val fragment = DemoObjectFragment()
//        fragment.arguments = Bundle().apply {
//            // Our object is just an integer :-P
//            putInt(ARG_OBJECT, position + 1)
//        }
//        return fragment
//    }
//
//    override fun getItem(position: Int): Fragment {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
//    }
//
//    override fun getCount(): Int {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
//        return 100
//    }
//}
//
//private const val ARG_OBJECT = "object"
//
//// Instances of this class are fragments representing a single
//// object in our collection.
//class DemoObjectFragment : Fragment() {
//
//    override fun onCreateView(
//            inflater: LayoutInflater,
//            container: ViewGroup?,
//            savedInstanceState: Bundle?
//    ): View {
//        return inflater.inflate(R.layout.fragment_collection_object, container, false)
//    }
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        arguments?.takeIf { it.containsKey(ARG_OBJECT) }?.apply {
//            val textView: TextView = view.findViewById(android.R.id.text1)
//            textView.text = getInt(ARG_OBJECT).toString()
//        }
//    }


package `in`.covid360rf.covid360.ui.home

import `in`.covid360rf.covid360.AllStatesCovidAdapter
import `in`.covid360rf.covid360.CovidInfo
import `in`.covid360rf.covid360.R
import `in`.covid360rf.covid360.databinding.FragmentHomeBinding

import android.app.Dialog
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.view.*
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.eazegraph.lib.models.PieModel
import org.json.JSONException
import org.json.JSONObject
import java.util.ArrayList


class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel
    private var _binding: FragmentHomeBinding? = null
    private lateinit var mProgressDialog : Dialog
    private val stateWiseCovidInfoList = ArrayList<CovidInfo>()
    private var adapter: AllStatesCovidAdapter? = null
    private lateinit var mSharedPreferences : SharedPreferences
    private lateinit var editor: SharedPreferences.Editor

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        setHasOptionsMenu(true)

        mSharedPreferences = context?.getSharedPreferences("stored previous data",MODE_PRIVATE)!!

//        val confirmed = mSharedPreferences.getString()



////        val textView: TextView = binding.textHome
//        homeViewModel.text.observe(viewLifecycleOwner, Observer {
//            textView.text = it
//        })
//        binding.confirmedCases.text = mSharedPreferences.getString("confirmed")
        showProgressDialog(getString(R.string.please_wait))
        fetchDataAllOverIndia()
        fetchDataAsPerState()
        return root

    }

    private fun fetchDataAllOverIndia() {
        val url = "https://corona.lmao.ninja/v2/countries/india/"
        val request = StringRequest(Request.Method.GET, url, { response ->
            try {
                val jsonObject = JSONObject(response)
                binding.confirmedCases.text = jsonObject.getString("cases")
                binding.activeCases.text = jsonObject.getString("active")
                binding.recoveredCases.text = jsonObject.getString("recovered")
                binding.deaths.text = jsonObject.getString("deaths")

                binding.pieChart.clearAnimation()
                binding.pieChart.clearChart()
                binding.pieChart.addPieSlice(PieModel("Confirmed", binding.confirmedCases.text.toString().toFloat(), Color.parseColor("#FFA726")))
                binding.pieChart.addPieSlice(PieModel("Recovered", binding.recoveredCases.text.toString().toFloat(), Color.parseColor("#66BB6A")))
                binding.pieChart.addPieSlice(PieModel("Deaths", binding.deaths.text.toString().toFloat(), Color.parseColor("#EF5350")))
                binding.pieChart.addPieSlice(PieModel("Active", binding.activeCases.text.toString().toFloat(), Color.parseColor("#29B6F6")))

                binding.pieChart.startAnimation()
                gettingDataSuccess()

                editor = mSharedPreferences.edit()
                editor.putString("confirmed",binding.confirmedCases.text.toString())
                editor.putString("recovered",binding.recoveredCases.text.toString())
                editor.putString("deaths",binding.deaths.text.toString())
                editor.putString("active",binding.activeCases.text.toString())
                editor.apply()

            } catch (e: JSONException) {
                e.printStackTrace()
            }
        }) { error -> Toast.makeText(context, error.message, Toast.LENGTH_SHORT).show() }
        val requestQueue = Volley.newRequestQueue(context)
        requestQueue.add(request)
    }
    private fun fetchDataAsPerState() {
        val url = "https://api.covid19india.org/data.json"
        val request = StringRequest(Request.Method.GET, url, { response ->
            try {
                val `object` = JSONObject(response)
                val stateWise = `object`.getJSONArray("statewise")
                for (i in 1 until stateWise.length()) {
                    val jsonObject = stateWise.getJSONObject(i)
                    val stateName = jsonObject.getString("state")
                    val activeCases = jsonObject.getString("active")
                    val deaths = jsonObject.getString("deaths")
                    val recoveredCases = jsonObject.getString("recovered")
                    val confirmedCases = jsonObject.getString("confirmed")
                   val model = CovidInfo(stateName, activeCases, deaths, confirmedCases, recoveredCases)
                    stateWiseCovidInfoList.add(model)
                }
                adapter = context?.let { AllStatesCovidAdapter(it, stateWiseCovidInfoList) }
                val linearLayoutManager = LinearLayoutManager(context)
                binding.rvStatesData.layoutManager = linearLayoutManager
                binding.rvStatesData.adapter = adapter
            } catch (e: JSONException) {
                e.printStackTrace()
            }
        }) { error -> Toast.makeText(context, error.message, Toast.LENGTH_SHORT).show() }
        val requestQueue = Volley.newRequestQueue(context)
        requestQueue.add(request)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    private fun gettingDataSuccess(){
       hideProgressDialog()
    }
    private fun showProgressDialog(text : String){

        mProgressDialog = Dialog(requireContext())
        mProgressDialog.setContentView(R.layout.dialog_progress)
        mProgressDialog.findViewById<TextView>(R.id.tv_progress_text).text = text
        mProgressDialog.setCanceledOnTouchOutside(true)
        mProgressDialog.setCancelable(true)
        mProgressDialog.show()
    }
    private fun hideProgressDialog(){
        mProgressDialog.hide()
        mProgressDialog.dismiss()
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.action_refresh ->{
                showProgressDialog(getString(R.string.please_wait))
                fetchDataAllOverIndia()
                fetchDataAsPerState()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_refresh,menu)
        super.onCreateOptionsMenu(menu, inflater)
    }
}
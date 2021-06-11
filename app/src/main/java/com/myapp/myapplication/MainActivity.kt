package com.myapp.myapplication

import android.app.DatePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONException
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {
    private lateinit var searchbutton:Button
    lateinit var pincode:EditText
    lateinit var  centerRv :RecyclerView
    lateinit var loading :ProgressBar
    lateinit var centerlist :List<CenterRVmodel>
    lateinit var centerRVapadter:CenterAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        searchbutton=findViewById(R.id.idbutton)
        pincode=findViewById(R.id.idPinCode)
        centerRv=findViewById(R.id.RV)
        loading=findViewById(R.id.progress)
        centerlist=ArrayList<CenterRVmodel>()

        vacccinated()

        searchbutton.setOnClickListener{
            val pinCode=pincode.text.toString()

            if(pinCode.length!=6){
                Toast.makeText(this,"please enter valid pin code",Toast.LENGTH_SHORT).show()
            }else{
                (centerlist as ArrayList<CenterRVmodel>).clear()
                val c= Calendar.getInstance()
                val year=c.get(Calendar.YEAR)
                val month=c.get(Calendar.MONTH)
                val day=c.get(Calendar.DAY_OF_MONTH)

                val dpd =DatePickerDialog(this,
                DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                    loading.setVisibility(View.VISIBLE)
                    val datestr:String="""$dayOfMonth-${month+1}-$year""""
                    getAppointmentdeatails(pinCode,datestr)
                },
                year,
                    month,
                    day

                    )
                dpd.show()

            }
        }
    }

    private fun vacccinated() {


        val url=""
        val queue=Volley.newRequestQueue(this)
        val request=JsonObjectRequest(Request.Method.GET,url,null,{
            response ->
            try {


            }catch (e :JSONException){
                e.printStackTrace()
            }

        }, {
          error ->
            Toast.makeText(this,"fail to get data",Toast.LENGTH_SHORT).show()

        })
    }

    private  fun getAppointmentdeatails(pincode:String,date:String){
        val url="https://cdn-api.co-vin.in/api/v2/appointment/sessions/public/findByPin?pincode="+pincode+"&date="+date
        val queue=Volley.newRequestQueue(this)
        val request=JsonObjectRequest(Request.Method.GET,url,null,{
            response ->
            try {
                val centerarray=response.getJSONArray("centers")
                if (centerarray.length().equals(0)){
                    Toast.makeText(this,"no vaccination center available",Toast.LENGTH_SHORT).show()
                }
                for (i in 0 until centerarray.length()){
                    val centerobj =centerarray.getJSONObject(i)
                    val centername:String=centerobj.getString("name")
                    val centeradd:String=centerobj.getString("address")
                    val centerfromtime:String=centerobj.getString("from")
                    val centertotime:String=centerobj.getString("to")
                    val fee_type:String=centerobj.getString("fee_type")
                    val sessionOBJ = centerobj.getJSONArray("sessions").getJSONObject(0)
                    val availablecapacity :Int=sessionOBJ.getInt("available_capacity")
                    val agelimit :Int=sessionOBJ.getInt("min_age_limit")
                    val vaccinename:String=sessionOBJ.getString("vaccine")


                    val center=CenterRVmodel(
                        centername,centeradd,centerfromtime,centertotime,agelimit,fee_type,availablecapacity,vaccinename
                    )
                    centerlist=centerlist+center
                }
                centerRVapadter= CenterAdapter(centerlist)
                centerRv.layoutManager=LinearLayoutManager(this)
                centerRv.adapter=centerRVapadter


            }catch (e :JSONException){
                e.printStackTrace()
            }
        },

                {
                    error->
                    Toast.makeText(this,"fail to get data",Toast.LENGTH_SHORT).show()
                }
                )
        queue.add(request)

    }

}
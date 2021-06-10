/**
 *  @author     Rajesh Keshoju
 *  @since      2021/06/04
 *  @updated    2021/06/07
 */

package `in`.covid360rf.covid360.ui.about

import `in`.covid360rf.covid360.R
import `in`.covid360rf.covid360.databinding.ActivityAboutBinding
import `in`.covid360rf.covid360.utils.Constants
import `in`.covid360rf.covid360.utils.toast
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton

class AboutActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAboutBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAboutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.textAppDevelopers.text = getString(R.string.app_devs, Constants.APP_DEVELOPER)
        binding.textAppDevelopers.setOnClickListener {
            startActivity(
                Intent(Intent.ACTION_VIEW)
                .setData(Uri.parse(Constants.FOUNDATION_URL))
            )
        }

        findViewById<FloatingActionButton>(R.id.btn_back).setOnClickListener {
            toast("Go back...")
            super.onBackPressed()
        }

    }

    /*override fun onStart() {
        super.onStart()

        loadWebContent()
    }*/

    /*private fun loadWebContent() {
        binding.webViewContent.loadUrl(Constants.FOUNDATION_URL)
        binding.webViewContent.visibility = View.GONE

        binding.webViewContent.evaluateJavascript(
            "() => {return ('<html>' + document.getElementsByTagName('p')[0].innerHTML + '</html>')}",
        ) {
            binding.webViewContent.visibility = View.VISIBLE
        }
    }*/
}
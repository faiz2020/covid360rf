/**
 *  @author     Rajesh Keshoju
 *  @since      2021/06/07
 *  @updated    2021/06/07
 */

package `in`.covid360rf.covid360.utils

import android.content.Context
import android.widget.Toast

fun Context.toast(message:String, length:Int = 0){
    Toast.makeText(this, message, length).show()
}
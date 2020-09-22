package com.hazz.kotlinmvp.utils

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.orhanobut.logger.Logger

import java.lang.reflect.Field

/**
 * Created by xuhao on 2017/12/13.
 * desc:
 */
object CleanLeakUtils {

    fun fixInputMethodManagerLeak(destContext: Context?) {
        if (destContext == null) {
            return
        }

        val inputMethodManager = destContext.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

        val viewArray = arrayOf("mCurRootView", "mServedView", "mNextServedView")
        var filed: Field
        var filedObject: Any?

        for (view in viewArray) {
            try {
                filed = inputMethodManager.javaClass.getDeclaredField(view)
                if (!filed.isAccessible) {
                    filed.isAccessible = true
                }
                filedObject = filed.get(inputMethodManager)
                if (filedObject != null && filedObject is View) {
                    val fileView = filedObject as View?
                    if (fileView!!.context === destContext) {   // The context referenced by the InputMethodManager is to be destroyed by the target
                        filed.set(inputMethodManager, null)     // Leave it blank, destroy the path to gc node
                    } else {
                        break   // It is not that you want the target to be destroyed,
                        // that is, it has entered another layer of interface, do not deal with it,
                        // to avoid affecting the original logic,
                        // and there is no need to continue the for loop
                    }
                }
            } catch (t: Throwable) {
                t.printStackTrace()
            }
        }
    }
}

package com.hazz.kotlinmvp.base

import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import com.classic.common.MultipleStatusView
import com.hazz.kotlinmvp.MyApplication
import io.reactivex.annotations.NonNull
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions


/**
 * @author xuhao
 * created: 2017/10/25
 * desc: BaseActivity base class
 */
abstract class BaseActivity : AppCompatActivity(),EasyPermissions.PermissionCallbacks {
    /**
     * Switching of views in multiple states
     */
    protected var mLayoutStatusView: MultipleStatusView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layoutId())
        initData()
        initView()
        start()
        initListener()
    }

    private fun initListener() {
        mLayoutStatusView?.setOnClickListener(mRetryClickListener)
    }

    open val mRetryClickListener: View.OnClickListener = View.OnClickListener {
        start()
    }

    /**
     *  Load layout
     */
    abstract fun layoutId(): Int

    /**
     * Initialization data
     */
    abstract fun initData()

    /**
     * Initialize View
     */
    abstract fun initView()

    /**
     * Start request
     */
    abstract fun start()

    /**
     * Clock in soft keyboard
     */
    fun openKeyBord(mEditText: EditText, mContext: Context) {
        val imm = mContext.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(mEditText, InputMethodManager.RESULT_SHOWN)
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY)
    }

    /**
     * Close soft keyboard
     */
    fun closeKeyBord(mEditText: EditText, mContext: Context) {
        val imm = mContext.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(mEditText.windowToken, 0)
    }

    override fun onDestroy() {
        super.onDestroy()
        MyApplication.getRefWatcher(this)?.watch(this)
    }


    /**
     * Rewrite the onRequestPermissionsResult() method of the Activity or Fragment to be applied for permissions,
     * Call EasyPermissions.onRequestPermissionsResult() inside to implement callback.
     *
     * @param requestCode  Identification code of permission request
     * @param permissions  Requested permissions
     * @param grantResults Authorization result
     */
    override fun onRequestPermissionsResult(requestCode: Int, @NonNull permissions: Array<String>, @NonNull grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

    /**
     * Execute callback when permission is successfully applied
     *
     * @param requestCode Identification code of permission request
     * @param perms       The name of the requested permission
     */
    override fun onPermissionsGranted(requestCode: Int, perms: List<String>) {
        Log.i("EasyPermissions", "Get successful permission$perms")
    }

    /**
     * Callback executed when permission application fails
     *
     * @param requestCode Identification code of permission request
     * @param perms       The name of the requested permission
     */
    override fun onPermissionsDenied(requestCode: Int, perms: List<String>) {
        // Processing authority name string
        val sb = StringBuffer()
        for (str in perms) {
            sb.append(str)
            sb.append("\n")
        }
        sb.replace(sb.length - 2, sb.length, "")
        // The user clicks to reject and is not called when asked
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            Toast.makeText(this, "Permission denied" + sb + "And don't ask again", Toast.LENGTH_SHORT).show()
            AppSettingsDialog.Builder(this)
                    .setRationale("This feature requires" + sb + "Authority，Otherwise it cannot be used normally, whether to open the settings")
                    .setPositiveButton("It is good")
                    .setNegativeButton("No way")
                    .build()
                    .show()
        }
    }
}

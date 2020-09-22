package com.hazz.kotlinmvp.base

import android.os.Bundle
import android.support.annotation.LayoutRes
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.classic.common.MultipleStatusView
import com.hazz.kotlinmvp.MyApplication
import io.reactivex.annotations.NonNull
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions

/**
 * @author Xuhao
 * created: 2017/10/25
 * desc:
 */

 abstract class BaseFragment: Fragment(),EasyPermissions.PermissionCallbacks{

    /**
     * Is the view loaded
     */
    private var isViewPrepare = false
    /**
     * Has the data been loaded
     */
    private var hasLoadData = false
    /**
     * Switching of views in multiple states
     */
    protected var mLayoutStatusView: MultipleStatusView? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(getLayoutId(),null)
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (isVisibleToUser) {
            lazyLoadDataIfPrepared()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        isViewPrepare = true
        initView()
        lazyLoadDataIfPrepared()
        // Views with multiple state switching retry click events
        mLayoutStatusView?.setOnClickListener(mRetryClickListener)
    }

    private fun lazyLoadDataIfPrepared() {
        if (userVisibleHint && isViewPrepare && !hasLoadData) {
            lazyLoad()
            hasLoadData = true
        }
    }

    open val mRetryClickListener: View.OnClickListener = View.OnClickListener {
        lazyLoad()
    }

    /**
     * Load layout
     */
    @LayoutRes
    abstract fun getLayoutId():Int

    /**
     * Initialize View
     */
    abstract fun initView()

    /**
     * Lazy loading
     */
    abstract fun lazyLoad()

    override fun onDestroy() {
        super.onDestroy()
        activity?.let { MyApplication.getRefWatcher(it)?.watch(activity) }
    }

    /**
     * Rewrite the onRequestPermissionsResult() method of the Activity or Fragment that you want to apply for permissions,
     * Call EasyPermissions.onRequestPermissionsResult() inside to implement the callback.
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
        Log.i("EasyPermissions", "获取成功的权限$perms")
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
            Toast.makeText(activity, "Permission denied" + sb + "And don't ask again", Toast.LENGTH_SHORT).show()
            AppSettingsDialog.Builder(this)
                    .setRationale("This feature requires" + sb + "Permission, otherwise it cannot be used normally, whether to open the settings")
                    .setPositiveButton("OK")
                    .setNegativeButton("NO")
                    .build()
                    .show()
        }
    }
}

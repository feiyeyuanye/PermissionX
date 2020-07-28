package com.permissionx.jhrdev

import androidx.fragment.app.FragmentActivity
import java.security.Permissions

/**
 * 对外接口
 * 使用方法：PermissionX.request()
 *
 * 指定成单例类的原因是为了能够更加方便地被调用
 */
object PermissionX {

    private const val TAG = "InvisibleFragment"

    /**
     * @param activity FragmentActivity 是 AppCompatActivity 的父类
     * @param permissions 可变长度的参数列表
     * @param callback 回调
     */
    fun request(activity:FragmentActivity, vararg permissions: String,callback: PermissionCallback){
        // 获取 FragmentManager 实例
        val fragmentManager = activity.supportFragmentManager
        // 判断传入的 Activity 参数中是否已经包含了指定 TAG 的 Fragment
        val existedFragment = fragmentManager.findFragmentByTag(TAG)
        val fragment = if (existedFragment != null){
            // 包含则直接使用该 Fragment
            existedFragment as InvisibleFragment
        }else {
            // 否则就创建一个新的 实例，并将它添加到 Activity 中，同时指定一个 TAG。
            val invisibleFragment = InvisibleFragment()
            // 需要调用 commitNow()，因为 commit() 并不会立即执行添加操作。
            fragmentManager.beginTransaction().add(invisibleFragment, TAG).commitNow()
            invisibleFragment
        }
        // 申请运行时权限
        // 申请结果会自动回调到 callback 参数中
        // permissions 参数在这里实际上是一个数组。对于数组，我们可以遍历它，可以通过下标访问，
        // 但是不可以直接将它传递给另外一个接收可变长度参数的方法。因此，这里加上了一个 *，表示将一个数组转换成可变长度参数传递过去。
        fragment.requestNow(callback,*permissions)
    }
}
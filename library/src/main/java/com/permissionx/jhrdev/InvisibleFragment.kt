package com.permissionx.jhrdev

import android.content.pm.PackageManager
import androidx.fragment.app.Fragment

/**
 * 对运行时权限的 API 进行封装并不是一件容易的事，因为这个操作是有特定的上下文依赖的的，
 * 一般需要在 Activity 中接收 onRequestPermissionsResult() 的回调才行，
 * 所以不能简单地将整个操作封装到一个独立的类中。当然，受此限制，也衍生出一些特别的解决方案，
 * 比如将运行时权限的操作封装到 BaseActivity 中，或是提供一个透明的 Activity 来处理运行时权限等。
 * --------------------------------------------------------------------------------
 * 而这里会采用另一种业内普遍比较认可的小技巧来进行实现，那就是采用 Fragment，
 * Google 在 Fragment 中也提供了一份相同的 API 用来申请运行时权限。
 * --------------------------------------------------------------------------------
 * 并且，Fragment 不像 Activity 那样必须有界面，我们完全可以向 Activity 中添加一个隐藏的 Fragment，
 * 然后在这个隐藏的 Fragment 中对运行时权限的 API 进行封装。
 * 这是一种非常轻量级的做法，不用担心隐藏 Fragment 会对 Activity 的性能造成什么影响。
 * 所以，内部并没有重写 onCreateView() 来加载布局，因此它自然就是一个不可见的 Fragment。
 */

// typealias 关键字可以用于给任意类型指定一个别名，简化写法。
typealias PermissionCallback = (Boolean,List<String>) -> Unit

class InvisibleFragment :Fragment() {

    /**
     * 定义 callback 变量作为运行时权限申请结果的回调通知方式，并将它声明成了一种函数类型变量，
     * 该函数类型接收 Boolean 和 List<String> 这两种类型的参数，并且没有返回值。
     */
    private var callback : PermissionCallback? = null

    /**
     * 接收一个与 callback 变量类型相同的函数类型参数，同时使用 vararg 关键字接收一个可变长度的参数列表。
     */
    fun requestNow(cb: PermissionCallback, vararg permissions: String){
        // 将传递进来的函数类型参数赋值给 callback 变量
        callback = cb
        // 调用 Fragment 中提供的 requestPermissions() 立即申请运行时权限，并将参数列表传递进去。
        // 这样就可以实现由外部调用方自主指定要申请哪些权限的功能了。
        requestPermissions(permissions,1)
    }

    /**
     * 重写方法，并处理运行时权限的申请结果。
     */
    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (requestCode == 1){
            // 记录所有被用户拒绝的权限
            val deniedList = ArrayList<String>()
            for ((index,result) in grantResults.withIndex()){
                // 遍历数组，并记录未被用户授权的权限。
                if (result != PackageManager.PERMISSION_GRANTED){
                    deniedList.add(permissions[index])
                }
            }
            // allGranted 变量标识是否所有申请的权限均已被授权，判断的依据是 deniedList 列表是否为空。
            val allGranted = deniedList.isEmpty()
            // 最后使用 callback 变量对运行时权限的申请结果进行回调。
            callback?.let { it(allGranted, deniedList) }
        }
    }
}
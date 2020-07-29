# PermissionX
第一行代码(第 3 版) - 编写并发布一个开源库练习

需要添加一些对此库的使用描述：（练习项目，不描述了，请直接移步）

https://github.com/guolindev/PermissionX


添加依赖：
```cpp
dependencies {
    implementation 'com.permissionx.jhrdev:permissionx:1.0.0'
}
```

测试代码：
```kotlin
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 测试发布的开源库，记得在 AndroidManifest.xml 中配置权限。
        PermissionX.request(this,
            Manifest.permission.CALL_PHONE,
            Manifest.permission.READ_CONTACTS){allGranted, deniedList ->
            if (allGranted){
                Toast.makeText(this,"ALL permissions are granted",Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(this,"ALL denied $deniedList",Toast.LENGTH_SHORT).show()
            }
        }
    }
}
```

# 自定义动画控件

## 使用步骤

#### Step 1.
```
dependencies{
    compile 'com.ms:behavior:1.0.0'
}
```
或者引用本地lib
```groovy
compile project(':viewbehavior')
```

#### Step 2.
```xml
<ImageView
        android:id="@+id/iv_icon"
        android:layout_width="90dp"
        android:layout_height="90dp"
        android:layout_gravity="center|top"
        android:layout_marginTop="70dp"
        android:src="@mipmap/ic_launcher"
        app:layout_behavior="com.ms.behavior.SimpleViewBehavior"
        app:svb_dependTarget="-160dp"
        app:svb_dependType="y"
        app:svb_dependsOn="@+id/app_bar"
        app:svb_targetHeight="40dp"
        app:svb_targetWidth="40dp"
        app:svb_targetX="-75dp"
        app:svb_targetY="40dp"
        tools:ignore="MissingPrefix" />
```

#### 使用说明文档
> 目前该库已在公司项目中使用，有问题欢迎提issue。
> 如果该库对你有帮助，请动动你的小手指，给个star哦🤩     

[English documentation](https://raw.githubusercontent.com/Leo199206/SideBar/main/README_EN.md)

+ kotlin语言开发
+ 列表快速索引控件（微信联系人，字母索引效果）
+ API灵活，可自定义配置不同颜色及样式



#### 效果预览
<img src="https://raw.githubusercontent.com/Leo199206/SideBar/main/image/device-2021-02-19-132024.gif" width="300" heght="500" align=center />


#### 依赖
+ 添加maven仓库配置到项目根目录gradle文件下

```
allprojects {
    repositories {
        maven { url 'https://jitpack.io' }
    }
}
```

+ 添加以下maven依赖配置到app模块，gradle文件下

```
implementation  'com.github.Leo199206:SideBar:1.0.1'
```

#### 添加到布局

```
   <?xml version="1.0" encoding="utf-8"?>
   <androidx.constraintlayout.widget.ConstraintLayout xmlns:android="http://schemas.android.com/apk/res/android"
       xmlns:app="http://schemas.android.com/apk/res-auto"
       xmlns:tools="http://schemas.android.com/tools"
       android:layout_width="match_parent"
       android:layout_height="match_parent"
       tools:context="com.sidebar.sample.MainActivity">
   
       <androidx.recyclerview.widget.RecyclerView
           android:id="@+id/recycler"
           android:layout_width="match_parent"
           android:layout_height="match_parent" />
   
       <com.jlertele.sidebar.SideBarView
           android:id="@+id/side_bar"
           android:layout_width="50dp"
           android:layout_height="wrap_content"
           app:layout_constraintBottom_toBottomOf="parent"
           app:layout_constraintRight_toRightOf="parent"
           app:layout_constraintTop_toTopOf="parent"
           app:sideItemSpacing="10dp"
           app:sidePressedTextBgColor="@android:color/holo_orange_dark"
           app:sidePressedTextColor="@color/white"
           app:sideTextColor="@color/black"
           app:sideTextSize="14sp" />
   
   
       <TextView
           android:id="@+id/side_hint"
           android:layout_width="60dp"
           android:layout_height="44dp"
           android:layout_gravity="right"
           android:layout_marginRight="20dp"
           android:background="@drawable/bg_side_hint"
           android:backgroundTint="@android:color/holo_orange_dark"
           android:gravity="center"
           android:paddingRight="15dp"
           android:textColor="@color/white"
           android:textSize="20sp"
           android:visibility="gone"
           app:layout_constraintRight_toLeftOf="@id/side_bar"
           app:layout_constraintTop_toTopOf="parent" />
   
   </androidx.constraintlayout.widget.ConstraintLayout>
```

+ 代码配置

```

  side_bar.setLetters(letterArray)
  side_bar.setSideBarListener(object: SideBarView.OnSideBarListener{
            override fun onSideTouchState(sideBarView: SideBarView?, isTouch: Boolean) {
                //根据isTouch判断是否需要显示提示View，具体参考demo
            }

            override fun onSideSelected(
                sideBarView: SideBarView?,
                position: Int,
                currentY: Float,
                selectedValue: String?
            ) {
             //滑动选择成功回调，
             //selectedValue为选中的字符串，position为字符串在letterArray中的位置
             //可根据selectedValue去列表中查找需要滑动的位置（建议在添加数据时，对位置进行缓存，减少查找操作，参考demo）
            }
        })

```


#### 已定义样式属性

| 属性  | 说明 |
| --- | --- |
| sideTextColor | 未选中字体颜色 |
| sidePressedTextColor | 按下时，选中字体颜色 |
| sidePressedTextBgColor | 按下时，选中背景颜色 |
| sideTextSize | 字体大小 | 
| sideItemSpacing | item间距，默认为10 |
| sideItemHeight | item高度，不设置时，默认拿字体大小作为高度 |

#### LICENSE
SideBar is under the Apache License Version 2.0. See the [LICENSE](https://raw.githubusercontent.com/Leo199206/SideBar/main/LICENSE) file for details.
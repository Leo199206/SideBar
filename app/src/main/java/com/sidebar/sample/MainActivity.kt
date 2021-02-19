package com.sidebar.sample

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.jlertele.sidebar.SideBarView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), SideBarView.OnSideBarListener {
    private val letterArray: List<String> by lazy {
        var list = arrayListOf<String>()
        for (char in 'A'..'Z') {
            list.add(char.toString())
        }
        list
    }
    private val testData = mutableListOf<TextEntity>(
        TextEntity("A", mutableListOf("阿拉伯", "阿里", "奥迪尔", "奥地利")),
        TextEntity("B", mutableListOf("北京", "北上广", "北极", "倍数", "背景")),
        TextEntity("C", mutableListOf("朝阳", "超速", "重启", "传奇")),
        TextEntity("D", mutableListOf("东北", "动静", "冬季")),
        TextEntity("E", mutableListOf("俄罗斯，二百件", "贰拾")),
        TextEntity("F", mutableListOf("风的季节", "风声", "封装", "封闭", "烽火狼烟")),
        TextEntity("G", mutableListOf("格式", "鸽子", "归仁", "盖子", "噶油")),
        TextEntity("L", mutableListOf("里程", "琉璃", "流星雨")),
        TextEntity("Z", mutableListOf("中心", "中间", "桌子", "琢磨"))
    )
    private val indexMap = HashMap<String, Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initSideBar()
        initListData()
    }


    /**
     * set List Data
     */
    private fun initListData() {
        val data = mutableListOf<String>()
        testData.forEachIndexed { _, textEntity ->
            indexMap[textEntity.latter] = data.size
            data.add((textEntity.latter))
            textEntity.data.forEach {
                data.add(it)
            }
        }
        recycler.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recycler.adapter = SampleAdapter(data)
    }


    /**
     * Config SideBar
     */
    private fun initSideBar() {
        side_bar.setLetters(letterArray)
        side_bar.setSideBarListener(this)
    }


    /**
     * Side Selected Callback
     * @param sideBarView SideBarView?
     * @param isTouch Boolean
     */
    override fun onSideTouchState(sideBarView: SideBarView?, isTouch: Boolean) {
        if (!isTouch) {
            side_hint.visibility = View.GONE
        }
    }

    override fun onSideSelected(
        sideBarView: SideBarView?,
        position: Int,
        currentY: Float,
        selectedValue: String?
    ) {
        scrollPosition(selectedValue)
        side_hint.translationY = currentY
        side_hint.text = selectedValue
        side_hint.visibility = View.VISIBLE
    }


    /**
     * scroll to Recycler position
     * @param selectedValue String
     */
    private fun scrollPosition(selectedValue: String?) {
        val index: Int? = indexMap[selectedValue]
        if (index != null) {
            recycler.smoothScrollToPosition(index)
        }
    }
}
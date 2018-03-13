package io.weimu.www

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import io.weimu.www.ui.*
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        list.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        val adapter = MainListAdapter()
        list.adapter = adapter

        val data = mutableListOf<String>()
        data.add("test")//测试专用
        data.add("circle_progress_bar")
        data.add("fan_bar")
        data.add("index_chart")
        data.add("stock_chart")
        data.add("switch_color_bar")
        data.add("text_with_border")
        data.add("wave")//波浪
        data.add("progress_button")//倒计时按钮
        data.add("granule")//粒子连线
        data.add("arc")
        data.add("hot_tag")//热门标签
        data.add("horizontal_auto_scroll_bgView")//横向自动滚动背景图

        adapter.dataList = data

        adapter.onItemClickListener = { text, position ->
            when (text) {
                "test" -> startActivity(TestAreaActivity.newIntent(this))//测试专用
                "circle_progress_bar" -> startActivity(CircleProgressBarActivity.newIntent(this))
                "fan_bar" -> startActivity(FanbarActivity.newIntent(this))
                "index_chart" -> startActivity(IndexChartActivity.newIntent(this))
                "stock_chart" -> startActivity(StockChartActivity.newIntent(this))
                "switch_color_bar" -> startActivity(SwitchColorBarActivity.newIntent(this))
                "text_with_border" -> startActivity(TextViewWithBorderActivity.newIntent(this))
                "wave" -> startActivity(WaveActivity.newIntent(this))//波浪
                "progress_button" -> startActivity(ProgresButtonActivity.newIntent(this))//倒计时按钮
                "granule" -> startActivity(GranuleGridActivity.newIntent(this))//粒子连线
                "arc" -> startActivity(ArcActivity.newIntent(this))//曲线图片
                "hot_tag" -> startActivity(HotTagActivity.newIntent(this))
                "horizontal_auto_scroll_bgView" -> startActivity(BgViewActivity.newIntent(this))
            }
        }
    }
}

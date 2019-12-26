package io.weimu.www

import android.view.LayoutInflater.from
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_switch_color_bar.view.*
import kotlin.properties.Delegates

/**
 * Author:你需要一台永动机
 * Date:2017/11/30 14:16
 * Description:
 */

class MainListAdapter : RecyclerView.Adapter<MainListAdapter.Holder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val targetView = from(parent.context).inflate(getItemLayoutRes(), parent, false)
        return Holder(targetView)
    }

    var dataList: MutableList<String> by Delegates.observable(mutableListOf<String>()) { kProperty, old, new ->
        notifyDataSetChanged()
    }

    var onItemClickListener: ((text: String, position: Int) -> Unit)? = null

    override fun getItemCount() = dataList.size

    private fun getItemLayoutRes(): Int = R.layout.list_item_main


    override fun onBindViewHolder(holder: Holder, position: Int) {
        val tv = holder.itemView.tv
        tv.text = dataList[position]
        holder.itemView.setOnClickListener {
            onItemClickListener?.invoke(dataList[position], position)
        }

    }

    class Holder(itemView: View) : RecyclerView.ViewHolder(itemView)

}

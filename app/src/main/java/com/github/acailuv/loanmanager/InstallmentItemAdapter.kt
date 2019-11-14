package com.github.acailuv.loanmanager

import android.view.LayoutInflater
import androidx.recyclerview.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.github.acailuv.loanmanager.database.Installment
import java.text.SimpleDateFormat
import java.util.*

class InstallmentItemAdapter(private val installmentList: List<Installment>) :
    RecyclerView.Adapter<InstallmentItemAdapter.InstallmentItemViewHolder>() {

    private lateinit var listener: OnItemClickListener

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): InstallmentItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.card_item, parent, false)
        return InstallmentItemViewHolder(view, listener)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }

    override fun getItemCount(): Int {
        return installmentList.size
    }

    override fun onBindViewHolder(holder: InstallmentItemViewHolder, position: Int) {
        val currentCardItem = installmentList[position]

        val startDate = Calendar.getInstance()
        startDate.timeInMillis = currentCardItem.startDate
        val endDate = Calendar.getInstance()
        endDate.timeInMillis = currentCardItem.endDate

        val dateFormat = SimpleDateFormat("dd MMM yyyy")

        holder.icon.setImageResource(R.drawable.ic_installment_big)
        holder.nickname.text = currentCardItem.installmentName
        holder.cardId.text = ""
        holder.startDate.text = dateFormat.format(startDate.time)
        holder.endDate.text = dateFormat.format(endDate.time)
        holder.status.text = currentCardItem.status
    }

    class InstallmentItemViewHolder(itemView: View, listener: OnItemClickListener) :
        RecyclerView.ViewHolder(itemView) {

        var icon: ImageView = itemView.findViewById(R.id.icon)
        var nickname: TextView = itemView.findViewById(R.id.nickname)
        var startDate: TextView = itemView.findViewById(R.id.cardholder)
        var endDate: TextView = itemView.findViewById(R.id.issuing_bank)
        var status: TextView = itemView.findViewById(R.id.variant)
        var cardId: TextView = itemView.findViewById(R.id.grade)

        val iHaveToDoThisToExecuteTheFollowingFunction = itemView.setOnClickListener {
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                listener.onItemClick(position)
            }
        }
    }

}
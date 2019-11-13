package com.github.acailuv.loanmanager

import android.view.LayoutInflater
import androidx.recyclerview.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.github.acailuv.loanmanager.database.Card
import com.github.acailuv.loanmanager.database.Installment


class CardItemAdapter(private val cardList: List<Card>) :
    RecyclerView.Adapter<CardItemAdapter.CardItemViewHolder>() {

    private lateinit var listener: OnItemClickListener

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CardItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.card_item, parent, false)
        return CardItemViewHolder(view, listener)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }

    override fun getItemCount(): Int {
        return cardList.size
    }

    override fun onBindViewHolder(holder: CardItemViewHolder, position: Int) {
        val currentCardItem = cardList[position]

        holder.icon.setImageResource(R.drawable.ic_credit_card_big)
        holder.nickname.text = currentCardItem.nickname
        holder.cardholder.text = currentCardItem.cardholder
        holder.issuingBank.text = currentCardItem.bank
        holder.variant.text = currentCardItem.variant
        holder.grade.text = currentCardItem.grade
    }

    class CardItemViewHolder(itemView: View, listener: OnItemClickListener) :
        RecyclerView.ViewHolder(itemView) {

        var icon: ImageView = itemView.findViewById(R.id.icon)
        var nickname: TextView = itemView.findViewById(R.id.nickname)
        var cardholder: TextView = itemView.findViewById(R.id.cardholder)
        var issuingBank: TextView = itemView.findViewById(R.id.issuing_bank)
        var variant: TextView = itemView.findViewById(R.id.variant)
        var grade: TextView = itemView.findViewById(R.id.grade)

        val iHaveToDoThisToExecuteTheFollowingFunction = itemView.setOnClickListener {
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                listener.onItemClick(position)
            }
        }
    }

}
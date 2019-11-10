package com.github.acailuv.loanmanager.manage_card

import android.view.LayoutInflater
import androidx.recyclerview.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.github.acailuv.loanmanager.R
import com.github.acailuv.loanmanager.database.Card


class CardItemAdapter(private val cardList: List<Card>) :
    RecyclerView.Adapter<CardItemAdapter.CardItemViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CardItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.card_item, parent, false)
        return CardItemViewHolder(view)
    }

    override fun getItemCount(): Int {
        return cardList.size
    }

    override fun onBindViewHolder(holder: CardItemViewHolder, position: Int) {
        val currentCardItem = cardList[position]

        holder.nickname.text = currentCardItem.nickname
        holder.cardholder.text = currentCardItem.cardholder
        holder.issuingBank.text = currentCardItem.bank
        holder.variant.text = currentCardItem.variant
        holder.grade.text = currentCardItem.grade
    }

    class CardItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var nickname: TextView = itemView.findViewById(R.id.nickname)
        var cardholder: TextView = itemView.findViewById(R.id.cardholder)
        var issuingBank: TextView = itemView.findViewById(R.id.issuing_bank)
        var variant: TextView = itemView.findViewById(R.id.variant)
        var grade: TextView = itemView.findViewById(R.id.grade)
    }

}
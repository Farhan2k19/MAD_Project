package com.example.madproject

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.lang.String
import kotlin.Int


class MineGridRecyclerAdapter(cells: List<Cell>, listener: OnCellClickListener) :
    RecyclerView.Adapter<MineGridRecyclerAdapter.MineTileViewHolder>() {
    private var cells: List<Cell>
    private val listener: OnCellClickListener
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MineTileViewHolder {
        val itemView: View =
            LayoutInflater.from(parent.context).inflate(R.layout.item_cell, parent, false)
        return MineTileViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MineTileViewHolder, position: Int) {
        holder.bind(cells[position])
        holder.setIsRecyclable(false)
    }

    override fun getItemCount(): Int {
        return cells.size
    }

    fun setCells(cells: List<Cell>) {
        this.cells = cells
        notifyDataSetChanged()
    }

    internal inner class MineTileViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var valueTextView: TextView
        fun bind(cell: Cell) {
            itemView.setBackgroundColor(Color.GRAY)
            itemView.setOnClickListener { listener.cellClick(cell) }
            if (cell.isRevealed()) {
                if (cell.getValue() === Cell.BOMB) {
                    valueTextView.setText(R.string.bomb)
                } else if (cell.getValue() === Cell.BLANK) {
                    valueTextView.text = ""
                    itemView.setBackgroundColor(Color.WHITE)
                } else {
                    valueTextView.setText(String.valueOf(cell.getValue()))
                    if (cell.getValue() === 1) {
                        valueTextView.setTextColor(Color.BLUE)
                    } else if (cell.getValue() === 2) {
                        valueTextView.setTextColor(Color.GREEN)
                    } else if (cell.getValue() === 3) {
                        valueTextView.setTextColor(Color.RED)
                    }
                }
            } else if (cell.isFlagged()) {
                valueTextView.setText(R.string.flag)
            }
        }

        init {
            valueTextView = itemView.findViewById(R.id.item_cell_value)
        }
    }

    init {
        this.cells = cells
        this.listener = listener
    }
}

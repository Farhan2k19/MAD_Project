package com.example.madproject

import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView


class MainActivity : AppCompatActivity(), OnCellClickListener {
    private var mineGridRecyclerAdapter: MineGridRecyclerAdapter? = null
    private var grid: RecyclerView? = null
    private var smiley: TextView? = null
    private var timer: TextView? = null
    private var flag: TextView? = null
    private var flagsLeft: TextView? = null
    private var mineSweeperGame: MineSweeperGame? = null
    private var countDownTimer: CountDownTimer? = null
    private var secondsElapsed = 0
    private var timerStarted = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        grid = findViewById(R.id.activity_main_grid)
        grid.setLayoutManager(GridLayoutManager(this, 10))
        timer = findViewById(R.id.activity_main_timer)
        timerStarted = false
        countDownTimer = object : CountDownTimer(TIMER_LENGTH, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                secondsElapsed += 1
                timer.setText(String.format("%03d", secondsElapsed))
            }

            override fun onFinish() {
                mineSweeperGame.outOfTime()
                Toast.makeText(applicationContext, "Game Over: Timer Expired", Toast.LENGTH_SHORT)
                    .show()
                mineSweeperGame.getMineGrid().revealAllBombs()
                mineGridRecyclerAdapter.setCells(mineSweeperGame.getMineGrid().getCells())
            }
        }
        flagsLeft = findViewById(R.id.activity_main_flagsleft)
        mineSweeperGame = MineSweeperGame(GRID_SIZE, BOMB_COUNT)
        flagsLeft.setText(
            java.lang.String.format(
                "%03d",
                mineSweeperGame.getNumberBombs() - mineSweeperGame.getFlagCount()
            )
        )
        mineGridRecyclerAdapter =
            MineGridRecyclerAdapter(mineSweeperGame.getMineGrid().getCells(), this)
        grid.setAdapter(mineGridRecyclerAdapter)
        smiley = findViewById(R.id.activity_main_smiley)
        smiley.setOnClickListener(View.OnClickListener {
            mineSweeperGame = MineSweeperGame(GRID_SIZE, BOMB_COUNT)
            mineGridRecyclerAdapter.setCells(mineSweeperGame.getMineGrid().getCells())
            timerStarted = false
            countDownTimer.cancel()
            secondsElapsed = 0
            timer.setText(R.string.default_count)
            flagsLeft.setText(
                java.lang.String.format(
                    "%03d",
                    mineSweeperGame.getNumberBombs() - mineSweeperGame.getFlagCount()
                )
            )
        })
        flag = findViewById(R.id.activity_main_flag)
        flag.setOnClickListener(View.OnClickListener {
            mineSweeperGame.toggleMode()
            if (mineSweeperGame.isFlagMode()) {
                val border = GradientDrawable()
                border.setColor(-0x1)
                border.setStroke(1, -0x1000000)
                flag.setBackground(border)
            } else {
                val border = GradientDrawable()
                border.setColor(-0x1)
                flag.setBackground(border)
            }
        })
    }

    fun cellClick(cell: Cell?) {
        mineSweeperGame.handleCellClick(cell)
        flagsLeft!!.text = java.lang.String.format(
            "%03d",
            mineSweeperGame.getNumberBombs() - mineSweeperGame.getFlagCount()
        )
        if (!timerStarted) {
            countDownTimer!!.start()
            timerStarted = true
        }
        if (mineSweeperGame.isGameOver()) {
            countDownTimer!!.cancel()
            Toast.makeText(applicationContext, "Game Over", Toast.LENGTH_SHORT).show()
            mineSweeperGame.getMineGrid().revealAllBombs()
        }
        if (mineSweeperGame.isGameWon()) {
            countDownTimer!!.cancel()
            Toast.makeText(applicationContext, "Game Won", Toast.LENGTH_SHORT).show()
            mineSweeperGame.getMineGrid().revealAllBombs()
        }
        mineGridRecyclerAdapter.setCells(mineSweeperGame.getMineGrid().getCells())
    }

    companion object {
        const val TIMER_LENGTH = 999000L // 999 seconds in milliseconds
        const val BOMB_COUNT = 10
        const val GRID_SIZE = 10
    }
}
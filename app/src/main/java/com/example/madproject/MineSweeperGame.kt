package com.example.madproject

class MineSweeperGame(size: Int, val numberBombs: Int) {
    private val mineGrid: MineGrid
    var isGameOver = false
        private set
    var isFlagMode = false
        private set
    var isClearMode = true
        private set
    var flagCount = 0
        private set
    private var timeExpired = false
    fun handleCellClick(cell: Cell) {
        if (!isGameOver && !isGameWon && !timeExpired && !cell.isRevealed()) {
            if (isClearMode) {
                clear(cell)
            } else if (isFlagMode) {
                flag(cell)
            }
        }
    }

    fun clear(cell: Cell) {
        val index: Int = getMineGrid().getCells().indexOf(cell)
        getMineGrid().getCells().get(index).setRevealed(true)
        if (cell.getValue() === Cell.BOMB) {
            isGameOver = true
        } else if (cell.getValue() === Cell.BLANK) {
            val toClear: MutableList<Cell> = ArrayList<Cell>()
            val toCheckAdjacents: MutableList<Cell> = ArrayList<Cell>()
            toCheckAdjacents.add(cell)
            while (toCheckAdjacents.size > 0) {
                val c: Cell = toCheckAdjacents[0]
                val cellIndex: Int = getMineGrid().getCells().indexOf(c)
                val cellPos: IntArray = getMineGrid().toXY(cellIndex)
                for (adjacent in getMineGrid().adjacentCells(cellPos[0], cellPos[1])) {
                    if (adjacent.getValue() === Cell.BLANK) {
                        if (!toClear.contains(adjacent)) {
                            if (!toCheckAdjacents.contains(adjacent)) {
                                toCheckAdjacents.add(adjacent)
                            }
                        }
                    } else {
                        if (!toClear.contains(adjacent)) {
                            toClear.add(adjacent)
                        }
                    }
                }
                toCheckAdjacents.remove(c)
                toClear.add(c)
            }
            for (c in toClear) {
                c.setRevealed(true)
            }
        }
    }

    fun flag(cell: Cell) {
        cell.setFlagged(!cell.isFlagged())
        var count = 0
        for (c in getMineGrid().getCells()) {
            if (c.isFlagged()) {
                count++
            }
        }
        flagCount = count
    }

    val isGameWon: Boolean
        get() {
            var numbersUnrevealed = 0
            for (c in getMineGrid().getCells()) {
                if (c.getValue() !== Cell.BOMB && c.getValue() !== Cell.BLANK && !c.isRevealed()) {
                    numbersUnrevealed++
                }
            }
            return if (numbersUnrevealed == 0) {
                true
            } else {
                false
            }
        }

    fun toggleMode() {
        isClearMode = !isClearMode
        isFlagMode = !isFlagMode
    }

    fun outOfTime() {
        timeExpired = true
    }

    fun getMineGrid(): MineGrid {
        return mineGrid
    }

    init {
        mineGrid = MineGrid(size)
        mineGrid.generateGrid(numberBombs)
    }
}
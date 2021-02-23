package board

import board.Direction.*

fun createSquareBoard(width: Int): SquareBoard =object: SquareBoard {
    override val width: Int= width
    val cells: List<Cell> = List(width*width){it}.map{Cell(it/width + 1, it % width + 1)}

    override fun getCellOrNull(i: Int, j: Int): Cell?{
        return if(i>width || j>width) null else cells[(i-1)*width+j-1]
    }
    override fun getCell(i: Int, j: Int): Cell{
        return if(i>width || j>width) throw IllegalArgumentException() else cells[(i-1)*width+j-1]
    }

    override fun getAllCells(): Collection<Cell>{
        return cells
    }

    override fun getRow(i: Int, jRange: IntProgression): List<Cell>{
        val triple = triple(jRange)
        val row = cells.filterIndexed { index, _ -> index in ((i-1)*width+triple.second-1) until (i-1)*width+triple.third }
        return if(triple.first) row.reversed() else row

    }

    private fun triple(jRange: IntProgression): Triple<Boolean, Int, Int> {
        val isReversed = jRange.first > jRange.last
        val first = if (isReversed) jRange.last else jRange.first
        var last = if (isReversed) jRange.first else jRange.last
        last = if (last > width) width else last
        return Triple(isReversed, first, last)
    }

    override fun getColumn(iRange: IntProgression, j: Int): List<Cell>{
        val triple = triple(iRange)
        val row = cells.filterIndexed { index, _ -> index in ((triple.second-1)*width+j-1) until (triple.third-1)*width+j && index % width ==((triple.second-1)*width+j-1) % width}
        return if(triple.first) row.reversed() else row
    }

    override fun Cell.getNeighbour(direction: Direction): Cell?{
        return when(direction){
            UP->if(((this.i-1)*width+this.j-1-width)>=0) cells[(this.i-1)*width+this.j-1-width] else null
            LEFT->if((this.i-1)*width+this.j-2>=0) cells[(this.i-1)*width+this.j-2] else null
            DOWN->if((this.i-1)*width+this.j-1+width<width*width) cells[(this.i-1)*width+this.j-1+width] else null
            RIGHT->if((this.i-1)*width+this.j<width*width) cells[(this.i-1)*width+this.j] else null
        }
    }
}


fun <T> createGameBoard(width: Int): GameBoard<T> = TODO()


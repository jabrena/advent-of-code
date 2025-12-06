package info.jab.aoc2022.day9;

import com.putoet.grid.Grid;
import com.putoet.grid.GridUtils;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

class RopePhysics {

    private boolean[][] cellsH;
    private boolean[][] cellsT;
    private boolean[][] cellsS;

    private Grid cellsVisited;
    private Integer currentHX = 0;
    private Integer currentHY = 0;
    private Integer currentTX = 0;
    private Integer currentTY = 0;

    public void create(Integer noOfColumns, Integer noOfRows) {
        cellsH = new boolean[noOfRows][noOfColumns];
        cellsT = new boolean[noOfRows][noOfColumns];
        cellsS = new boolean[noOfRows][noOfColumns];
        cellsVisited = new Grid(GridUtils.of(0, noOfColumns, 0, noOfRows, '.'));
        for (int y = 0; y < cellsH.length; y++) {
            for (int x = 0; x < cellsH[0].length; x++) {
                cellsH[y][x] = false;
                cellsT[y][x] = false;
                cellsS[y][x] = false;
            }
        }
    }

    public void print() {
        for (int y = 0; y < cellsH.length; y++) {
            for (int x = 0; x < cellsH[0].length; x++) {
                char symbol = '.';
                if (cellsH[y][x]) {
                    symbol = 'H';
                } else if (cellsT[y][x]) {
                    symbol = 'T';
                } else if (cellsS[y][x]) {
                    symbol = 's';
                }
                System.out.print(symbol);
            }
            System.out.println();
        }
    }

    public void printVisited() {
        for (int y = cellsVisited.minY(); y < cellsVisited.maxY(); y++) {
            for (int x = cellsVisited.minX(); x < cellsVisited.maxX(); x++) {
                System.out.print(cellsVisited.get(x, y));
            }
            System.out.println();
        }
    }

    public void setInitialState() {
        ReadWriteLock lock = new ReentrantReadWriteLock();
        Lock readLock = lock.readLock();
        try {
            readLock.lock();
            this.currentHX = cellsH.length / 2;
            this.currentHY = cellsH.length / 2;
            this.currentTX = this.currentHX;
            this.currentTY = this.currentHY;
            cellsH[this.currentHY][this.currentHX] = true;
            cellsT[this.currentTY][this.currentTX] = true;
            cellsS[this.currentTY][this.currentTX] = true;
            cellsVisited.set(this.currentTX, this.currentTY, '#');
        } finally {
            readLock.unlock();
        }
    }

    public void execute(Movement movement) {
        for (int i = 0; i < movement.number(); i++) {
            this.moveH(movement.direction());
            if (this.checkDistanceTwithH()) {
                this.moveT(movement.direction());
            }
        }
    }

    /**
     * TTT
     * THT
     * TTT
     */
    private boolean checkDistanceTwithH() {
        // @formatter:off
        return Math.abs(this.currentHX - this.currentTX) > 1
            || Math.abs(this.currentHY - this.currentTY) > 1;
        // @formatter:on
    }

    private void moveH(Direction direction) {
        //Begin Transaction
        //Update every field in current
        this.cellsH[this.currentHY][this.currentHX] = false;

        //Update HEAD to the new cell
        if (direction == Direction.R) {
            this.currentHX += 1;
        } else if (direction == Direction.L) {
            this.currentHX -= 1;
        } else if (direction == Direction.U) {
            this.currentHY -= 1;
        } else if (direction == Direction.D) {
            this.currentHY += 1;
        }
        this.cellsH[this.currentHY][this.currentHX] = true;
        //End Transaction
    }

    private void moveT(Direction direction) {
        //Begin Transaction
        //Update every field in current
        this.cellsT[this.currentTY][this.currentTX] = false;

        //Update Tail to the new cell
        if (direction == Direction.R) {
            this.currentTX += 1;
            if (this.currentHY < this.currentTY) {
                this.currentTY -= 1;
            }
            if (this.currentHY > this.currentTY) {
                this.currentTY += 1;
            }
        } else if (direction == Direction.L) {
            this.currentTX -= 1;
            if (this.currentHY < this.currentTY) {
                this.currentTY -= 1;
            }
            if (this.currentHY > this.currentTY) {
                this.currentTY += 1;
            }
        } else if (direction == Direction.U) {
            this.currentTY -= 1;
            //Diagonal adjustment
            if (this.currentHX > this.currentTX) {
                this.currentTX += 1;
            }
            if (this.currentHX < this.currentTX) {
                this.currentTX -= 1;
            }
        } else if (direction == Direction.D) {
            this.currentTY += 1;
            if (this.currentHX > this.currentTX) {
                this.currentTX -= 1;
            }
            if (this.currentHX < this.currentTX) {
                this.currentTX += 1;
            }
        }
        this.cellsT[this.currentTY][this.currentTX] = true;
        this.cellsVisited.set(this.currentTX, this.currentTY, '#');
        //End Transaction
    }

    public Grid getCellsVisited() {
        return cellsVisited;
    }
}

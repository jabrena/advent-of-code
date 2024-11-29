package jab.aoc.day9;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

class RopePhysics {

    private Boolean[][] cellsH;
    private Boolean[][] cellsT;
    private Boolean[][] cellsS;

    private Boolean[][] cellsVisited;
    private Integer currentHX = 0;
    private Integer currentHY = 0;
    private Integer currentTX = 0;
    private Integer currentTY = 0;

    public void create(Integer noOfColumns, Integer noOfRows) {
        cellsH = new Boolean[noOfRows][noOfColumns];
        cellsT = new Boolean[noOfRows][noOfColumns];
        cellsS = new Boolean[noOfRows][noOfColumns];
        cellsVisited = new Boolean[noOfRows][noOfColumns];
        for (int y = 0; y < cellsH.length; y++) {
            for (int x = 0; x < cellsH[0].length; x++) {
                cellsH[y][x] = false;
                cellsT[y][x] = false;
                cellsS[y][x] = false;
                cellsVisited[y][x] = false;
            }
        }
    }

    public void print() {
        for (int y = 0; y < cellsH.length; y++) {
            for (int x = 0; x < cellsH[0].length; x++) {
                if (cellsH[y][x]) {
                    System.out.print("H");
                    continue;
                }
                if (cellsT[y][x]) {
                    System.out.print("T");
                    continue;
                }
                if (cellsS[y][x]) {
                    System.out.print("s");
                    continue;
                }
                System.out.print(".");
            }
            System.out.println();
        }
    }

    public void printVisited() {
        for (int y = 0; y < cellsVisited.length; y++) {
            for (int x = 0; x < cellsVisited[0].length; x++) {
                if (cellsVisited[y][x]) {
                    System.out.print("#");
                    continue;
                }
                System.out.print(".");
            }
            System.out.println();
        }
    }

    public void setInitialState() {
        ReadWriteLock lock = new ReentrantReadWriteLock();
        Lock readLock = lock.readLock();
        try {
            readLock.lock();
            this.currentHX = this.cellsH.length / 2;
            this.currentHY = this.cellsH.length / 2;
            this.currentTX = this.currentHX;
            this.currentTY = this.currentHY;
            cellsH[this.currentHY][this.currentHX] = true;
            cellsT[this.currentTY][this.currentTX] = true;
            cellsS[this.currentTY][this.currentTX] = true;
            cellsVisited[this.currentTY][this.currentTX] = true;
        } finally {
            readLock.unlock();
        }
    }

    public void execute(Movement movement) {
        //System.out.println();
        //System.out.println("== " + movement.direction() + " " + movement.number() + " ==");
        //System.out.println();

        for (int i = 0; i < movement.number(); i++) {
            this.moveH(movement.direction());
            if (this.checkDistanceTwithH()) {
                //System.out.println("Move");
                this.moveT(movement.direction());
            }
            //this.print();
        }
    }

    /**
     * TTT
     * THT
     * TTT
     */
    private Boolean checkDistanceTwithH() {
        // @formatter:off
        if (Math.abs(this.currentHX - this.currentTX) > 1
            || Math.abs(this.currentHY - this.currentTY) > 1) {
            return true;
        }
        // @formatter:on
        return false;
    }

    private void moveH(Direction direction) {
        //Begin Transaction
        //Update every field in current
        boolean previousVisited = false;
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
        //Updates
        //System.out.println("H: " + this.currentHX + " " + this.currentHY);
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
            //System.out.println(this.currentHX + this.currentHY);
            //System.out.println(this.currentTX + this.currentTY);
        } else if (direction == Direction.D) {
            this.currentTY += 1;
            if (this.currentHX > this.currentTX) {
                this.currentTX -= 1;
            }
            if (this.currentHX < this.currentTX) {
                this.currentTX += 1;
            }
        }
        //System.out.println("T: " + this.currentTX + " " + this.currentTY);
        this.cellsT[this.currentTY][this.currentTX] = true;
        this.cellsVisited[this.currentTY][this.currentTX] = true;
        //End Transaction
    }

    public Boolean[][] getCellsVisited() {
        return cellsVisited;
    }
}

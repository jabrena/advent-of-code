package info.jab.aoc.day4;

import com.putoet.resources.ResourceLines;
import com.putoet.grid.Grid;
import com.putoet.grid.GridUtils;

public class SoupLetter {

    private Grid grid;

    public SoupLetter(String fileName) {
        var list = ResourceLines.list(fileName);
        grid = new Grid(GridUtils.of(list));
    }

    final static String XMAS = "XMAS";

    public static int checkWord(int x, int y, char[][] grid){
        int total = 0;

        //Up left
        if(x >= 3 && y >= 3){
            if(grid[x-1][y-1] == 'M' && grid[x-2][y-2] == 'A' && grid[x-3][y-3] == 'S'){
                total++;
            }
        }
        //Up
        if(y >= 3){
            if(grid[x][y-1] == 'M' && grid[x][y-2] == 'A' && grid[x][y-3] == 'S'){
                total++;
            }
        }
        //Up right
        if(x <= grid.length - XMAS.length() && y >= 3){
            if(grid[x+1][y-1] == 'M' && grid[x+2][y-2] == 'A' && grid[x+3][y-3] == 'S'){
                total++;
            }
        }
        //Left
        if(x >= 3){
            if(grid[x-1][y] == 'M' && grid[x-2][y] == 'A' && grid[x-3][y] == 'S'){
                total++;
            }
        }
        //Right
        if(x <= grid.length - XMAS.length()){
            if(grid[x+1][y] == 'M' && grid[x+2][y] == 'A' && grid[x+3][y] == 'S'){
                total++;
            }
        }
        //Down left
        if(x >= 3 && y <= grid.length - XMAS.length()){
            if(grid[x-1][y+1] == 'M' && grid[x-2][y+2] == 'A' && grid[x-3][y+3] == 'S'){
                total++;
            }
        }
        //Down
        if(y <= grid.length - XMAS.length()){
            if(grid[x][y+1] == 'M' && grid[x][y+2] == 'A' && grid[x][y+3] == 'S'){
                total++;
            }
        }
        //Down Right
        if(x <= grid.length - XMAS.length() && y <= grid.length - XMAS.length()){
            if(grid[x+1][y+1] == 'M' && grid[x+2][y+2] == 'A' && grid[x+3][y+3] == 'S'){
                total++;
            }
        }

        return total;
    }

    public int countXMAS() {
        int total = 0;
        var map = grid.grid();
        for(int i = 0; i < map.length; i++){
            for(int j = 0; j < map[i].length; j++){
                if(map[i][j] == 'X'){
                    total += checkWord(i, j, map);
                }
            }
        }
        return total;
    }

    public void print() {
        System.out.println(grid);
    }

}

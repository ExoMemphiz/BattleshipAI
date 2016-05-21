/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package r2;

import battleship.interfaces.Position;
import java.util.ArrayList;

/**
 *
 * @author CHRIS
 */
public class TileBoard {
 
    private Tile[][] board;

    public TileBoard(int width, int height) {
        board = new Tile[width][height];
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                board[x][y] = new Tile(new Position(x, y), Tile.UNKNOWN);
            }
        }
    }
    
    public TileBoard(Tile[][] board) {
        this.board = board;
    }
    
    public Tile getTile(int x, int y) {
        return board[x][y];
    }
 
    public void calculateHeatmap(ArrayList<Shot> shots) {
        for (int x = 0; x < 10; x++) {
            for (int y = 0; y < 10; y++) {
                
                
            }
        }
    }
    
}
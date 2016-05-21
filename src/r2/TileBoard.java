/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package r2;

import battleship.interfaces.Position;
import java.util.ArrayList;
import java.util.Iterator;

/**
 *
 * @author CHRIS
 */
public class TileBoard implements Iterable<Tile> {
 
    private int width, height;
    private Tile[][] board;

    public TileBoard(int width, int height) {
        this.width = width;
        this.height = height;
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
 
    private ArrayList<Tile> getBounding(Tile t) {
        Position p = t.getPos();
        ArrayList<Tile> tiles = new ArrayList<>();
        if (p.x > 0) {
            //x - 1; iterations
            if (p.y > 0) {
                //y - 1; iterations
                Tile bound = getTile(p.x - 1, p.y - 1);
                if (bound.getTileState() != Tile.MISS) {
                    tiles.add(bound);
                }
            }
            if (p.y < height - 1) {
                //y + 1; iterations
                Tile bound = getTile(p.x - 1, p.y + 1);
                if (bound.getTileState() != Tile.MISS) {
                    tiles.add(bound);
                }
            }
        }
        if (p.x < width - 1) {
            //x + 1; iterations
            if (p.y > 0) {
                //y - 1; iterations
                Tile bound = getTile(p.x + 1, p.y - 1);
                if (bound.getTileState() != Tile.MISS) {
                    tiles.add(bound);
                }
            }
            if (p.y < height - 1) {
                //y + 1; iterations
                Tile bound = getTile(p.x + 1, p.y + 1);
                if (bound.getTileState() != Tile.MISS) {
                    tiles.add(bound);
                }
            }
        }
        return tiles;
    }
    
    public void calculateHeatmap() {
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                Tile t = getTile(x, y);
                if (t.getTileState() == Tile.UNKNOWN) {
                    //Possibility of shooting here
                    ArrayList<Tile> bounding = getBounding(t);
                    for (Tile bound : bounding) {
                        if (bound.getTileState() == Tile.HIT) {
                            ArrayList<Tile> hitBounding = getBounding(bound);
                            //Do something
                            for (Tile hitBound : hitBounding) {
                                if (!hitBound.equals(t)) {
                                    if (inLineOfHits(bound, hitBound, t)) {
                                        
                                    }
                                }
                            }
                        } else {
                            //
                        }
                    }
                } else if (t.getTileState() == Tile.HIT) {
                    
                }
            }
        }
    }
    
    private boolean boundHit() {
        return false;
    }
    
    /**
     * @param t1 = bound
     * @param t2 = hitBound
     * @param t3 = t
     * @return 
     */
    private boolean inLineOfHits(Tile t1, Tile t2, Tile t3) {
        //Make a line between t1 and t2, is t3 a continium of the line?
        if (t1.getPos().x == t2.getPos().x) {
            return t3.getPos().x == t1.getPos().x;
        }
        if (t1.getPos().y == t2.getPos().y) {
            return t3.getPos().y == t1.getPos().y;
        }
        return false;
    }

    @Override
    public Iterator<Tile> iterator() {
        Iterator<Tile> tileIterator = new Iterator<Tile>() {
            int x, y;
            
            @Override
            public boolean hasNext() {
                return width < x && height < y;
            }

            @Override
            public Tile next() {
                if (x >= width) {
                    y++;
                    x = 0;
                }
                Tile t = getTile(x, y);
                x++;
                return t;
            }
        };
        return tileIterator;
    }
    
}
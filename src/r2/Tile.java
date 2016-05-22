/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package r2;

import battleship.interfaces.Position;

/**
 *
 * @author CHRIS
 */
public class Tile {
 
    public static final int HIT = 1;
    public static final int MISS = -1;
    public static final int UNKNOWN = 0;
    
    private Position pos;
    private int tileState, tileValue;
    private double heuristicValue;

    public Tile(Position pos, int tileState) {
        this.pos = pos;
        this.tileState = tileState;
        heuristicValue = heuristicValue(this);
    }

    private double heuristicValue(Tile t) {
        //Distance from tile to location: (4, 5)
        int x2 = 4;
        int y2 = 5;
        int x1 = t.getPos().x;
        int y1 = t.getPos().y;
        return Math.sqrt((x1-x2)*(x1-x2) + (y1-y2)*(y1-y2));
    }
    
    public Position getPos() {
        return pos;
    }

    public int getTileState() {
        return tileState;
    }

    public void setTileState(int tileState) {
        this.tileState = tileState;
    }

    public int getTileValue() {
        return tileValue;
    }
    
    public void setTileValue(int value) {
        this.tileValue = value;
    }

    public double getHeuristicValue() {
        return heuristicValue;
    }

    @Override
    public boolean equals(Object obj) {
        Tile t = (Tile) obj;
        return (t.getPos().equals(this.getPos()));
    }

    @Override
    public String toString() {
        return "[" + pos.x + ", " + pos.y + ", state: " + tileState + ", value: " + tileValue + "]";
    }
    
}
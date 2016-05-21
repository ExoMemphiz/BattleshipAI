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

    public Tile(Position pos, int tileState) {
        this.pos = pos;
        this.tileState = tileState;
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
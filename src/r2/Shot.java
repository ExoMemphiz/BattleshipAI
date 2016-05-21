
package r2;

import battleship.interfaces.Position;


public class Shot 
{
    private final Position pos;
    private final boolean hit;
    
    public Shot(Position pos, boolean hit){
        this.pos = pos;
        this.hit = hit;
    }

    public Position getPos() {
        return pos;
    }

    public boolean isHit() {
        return hit;
    }

}
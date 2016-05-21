
package r2;

import battleship.interfaces.Position;


public class Shot 
{
    private Position pos;
    private boolean hit;
    
    public Shot(Position pos, boolean hit){
        this.pos = pos;
        this.hit = hit;
    }

    public Position getPos() {
        return pos;
    }

    public void setPos(Position pos) {
        this.pos = pos;
    }

    public boolean isHit() {
        return hit;
    }

    public void setHit(boolean hit) {
        this.hit = hit;
    }
    
    
}

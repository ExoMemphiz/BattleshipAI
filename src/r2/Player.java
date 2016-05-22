/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package r2;

import battleship.interfaces.BattleshipsPlayer;
import battleship.interfaces.Fleet;
import battleship.interfaces.Position;
import battleship.interfaces.Board;
import battleship.interfaces.Ship;
import java.util.ArrayList;
import java.util.Random;

/**
 *
 * @author Tobias
 */
public class Player implements BattleshipsPlayer {
    
    private final static Random rnd = new Random();
    private int sizeX = 10;
    private int sizeY = 10;
    private Board myBoard;
    private TileBoard tileBoard;
    private Tile previousTile;
    private boolean killingShip;
    private int cachedShips = 5;
   
    public Player() {
        tileBoard = new TileBoard(sizeX, sizeY);
    }

   
    /**
     * The method called when its time for the AI to place ships on the board 
     * (at the beginning of each round).
     * 
     * The Ship object to be placed  MUST be taken from the Fleet given 
     * (do not create your own Ship objects!).
     * 
     * A ship is placed by calling the board.placeShip(..., Ship ship, ...) 
     * for each ship in the fleet (see board interface for details on placeShip()).
     * 
     * A player is not required to place all the ships. 
     * Ships placed outside the board or on top of each other are wrecked.
     * 
     * @param fleet Fleet all the ships that a player should place. 
     * @param board Board the board were the ships must be placed.
     */
    @Override
    public void placeShips(Fleet fleet, Board board) {
        placeShipsCorner(fleet, board);
        /*
        myBoard = board;
        sizeX = board.sizeX();
        sizeY = board.sizeY();
        for(int i = 0; i < fleet.getNumberOfShips(); ++i)
        {
            Ship s = fleet.getShip(i);
            boolean vertical = rnd.nextBoolean();
            Position pos;
            if(vertical)
            {
                int x = rnd.nextInt(sizeX);
                int y = rnd.nextInt(sizeY-(s.size()-1));
                pos = new Position(x, y);
            }
            else
            {
                int x = rnd.nextInt(sizeX-(s.size()-1));
                int y = rnd.nextInt(sizeY);
                pos = new Position(x, y);
            }
            board.placeShip(pos, s, vertical);
        }
        */
    }
    
    public void placeShipsCorner(Fleet fleet, Board board)
    {
        myBoard = board;
        sizeX = board.sizeX();
        sizeY = board.sizeY();
        boolean vertical = true;
        Position startPos = null;
        int bingo = rnd.nextInt(4);
        switch (bingo)
        {
            case 0:
                startPos = new Position(0,0);
                break;
            case 1:
                startPos = new Position(0,5);
                break;
            case 2:
                startPos = new Position(4,0);
                break;
            case 3:
                startPos = new Position(4,5);
                break;
        }
        
        for (int i = 0; i < fleet.getNumberOfShips(); i++) {
            Ship s = fleet.getShip(i);
            board.placeShip(startPos, s, vertical);
        }
    }

    /**
     * Called every time the enemy has fired a shot.
     * 
     * The purpose of this method is to allow the AI to react to the 
     * enemy's incoming fire and place his/her ships differently next round.
     * 
     * @param pos Position of the enemy's shot 
     */
    @Override
    public void incoming(Position pos) {
        //Do nothing
    }

    
    /**
     * Called by the Game application to get the Position of your shot.
     *  hitFeedBack(...) is called right after this method.
     * 
     * @param enemyShips Fleet the enemy's ships. Compare this to the Fleet 
     * supplied in the hitFeedBack(...) method to see if you have sunk any ships.
     * 
     * @return Position of you next shot.
     */
    @Override
    public Position getFireCoordinates(Fleet enemyShips) {
        ArrayList<Tile> suitableTiles = new ArrayList<>();
        //if tilevalue is >= 50 the chance of hitting a ship is high, hit here.
        tileBoard.calculateHeatmap();
        Tile[][] heatmapBoard = tileBoard.getBoard();
        for (Tile[] tiles : heatmapBoard) {
            for (Tile t : tiles) {
                if (t.getTileValue() >= 20) {
                    suitableTiles.add(t);
                }
            }
        }
        //If there are suitable heatmap tiles
        int count = 0;
        for (Tile t : suitableTiles) {
            if (t.getTileValue() >= 25) {
                count++;
            }
        }
        if (suitableTiles.size() > 0) {
            if (killingShip) {
                previousTile = suitableTiles.get(rnd.nextInt(suitableTiles.size()));
                Position p = previousTile.getPos();
                //System.out.println("Choosing from heat map (" + p.x + ", " + p.y + ")");
                return p;
            } else if (count > 0) {
                //Only 25 or over
                
                previousTile = suitableTiles.get(rnd.nextInt(suitableTiles.size()));
                
                while (previousTile.getTileValue() < 25) {
                    previousTile = suitableTiles.get(rnd.nextInt(suitableTiles.size()));
                }
                Position p = previousTile.getPos();
                //System.out.println("Choosing from heat map (" + p.x + ", " + p.y + ")");
                return p;
            } else {
                return shootRandomShit(suitableTiles);
            }
        } else {
            return shootRandomShit(suitableTiles);
        }
    }

    public Position shootRandomShit(ArrayList<Tile> suitableTiles) {
        //If no suitable heatmap tiles (any within range of killing a ship) use diagonal lines
        //Check every third tile, with a specific heuristic in mind
        for (int y = 0; y < 10; y++) {
            int offset = y % 3;     //X indentation for each y increment
            for (int x = offset; x < 10; x += 3) {
                //System.out.println("Offset: " + offset + ", X: " + x + ", Y: " + y);
                Tile t = tileBoard.getTile(x, y);
                if (t.getTileState() == Tile.UNKNOWN) {
                    suitableTiles.add(t);
                }
            }
        }
        if (suitableTiles.size() > 0) {
            Tile highestHeuristic = suitableTiles.get(0);
            for (Tile t : suitableTiles) {
                if (t.getHeuristicValue() < highestHeuristic.getHeuristicValue() || rnd.nextInt(100) > 90) {
                    highestHeuristic = t;
                }
            }
            previousTile = highestHeuristic;
            //previousTile = suitableTiles.get(rnd.nextInt(suitableTiles.size()));
            Position p = previousTile.getPos();
            //System.out.println("Shooting at Modulus 3 (" + p.x + ", " + p.y + ")");
            return p;
        } else {
            //Randomly selected from within every 2nd tile of the grid
            for (int y = 0; y < 10; y++) {
                int offset = y % 2;     //X indentation for each y increment
                for (int x = offset; x < 10; x += 2) {
                    Tile t = tileBoard.getTile(x, y);
                    if (t.getTileState() == Tile.UNKNOWN) {
                        suitableTiles.add(t);
                    }
                }
            }
            Tile highestHeuristic = suitableTiles.get(0);
            for (Tile t : suitableTiles) {
                if (t.getHeuristicValue() < highestHeuristic.getHeuristicValue() || rnd.nextInt(100) > 90) {
                    highestHeuristic = t;
                }
            }
            previousTile = highestHeuristic;
            Position p = previousTile.getPos();
            //System.out.println("Shooting at Modulus 2 (" + p.x + ", " + p.y + ")");
            return p;
        }
    }
    
    /**
     * Called right after getFireCoordinates(...) to let your AI know if you hit
     * something or not. 
     * 
     * Compare the number of ships in the enemyShips with that given in 
     * getFireCoordinates in order to see if you sunk a ship.
     * 
     * @param hit boolean is true if your last shot hit a ship. False otherwise.
     * @param enemyShips Fleet the enemy's ships.
     */
    @Override
    public void hitFeedBack(boolean hit, Fleet enemyShips) {
        //Do nothing
        if (hit) {
            killingShip = true;
        }
        if (enemyShips.getNumberOfShips() < cachedShips) {
            cachedShips--;
            killingShip = false;
        }
        previousTile.setTileState(hit ? Tile.HIT : Tile.MISS);
    }    

    
    /**
     * Called in the beginning of each match to inform about the number of 
     * rounds being played.
     * @param rounds int the number of rounds i a match
     */
    @Override
    public void startMatch(int rounds) {
        //Do nothing
    }
    
    
    /**
     * Called at the beginning of each round.
     * @param round int the current round number.
     */
    @Override
    public void startRound(int round) {
        //Do nothing
    }

    
    /**
     * Called at the end of each round to let you know if you won or lost.
     * Compare your points with the enemy's to see who won.
     * 
     * @param round int current round number.
     * @param points your points this round: 100 - number of shot used to sink 
     * all of the enemy's ships. 
     *
     * @param enemyPoints int enemy's points this round. 
     */
    @Override
    public void endRound(int round, int points, int enemyPoints) {
        //Do nothing
    }
    
    
    /**
     * Called at the end of a match (that usually last 1000 rounds) to let you 
     * know how many losses, victories and draws you scored.
     * 
     * @param won int the number of victories in this match.
     * @param lost int the number of losses in this match.
     * @param draw int the number of draws in this match.
     */
    @Override
    public void endMatch(int won, int lost, int draw) {
        //Do nothing
    }
    
}

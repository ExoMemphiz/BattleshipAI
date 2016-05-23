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
import java.util.Arrays;
import java.util.Random;

/**
 *
 * @author Tobias
 */
public class Player implements BattleshipsPlayer {
    
    private final static Random rnd = new Random();
    private int sizeX = 10;
    private int sizeY = 10;
    private double enemyShots[][];
    private double incrementer = 1.0;
    private Board myBoard;
    private TileBoard tileBoard;
    private Tile previousTile;
    private boolean killingShip;
    private int cachedShips = 5;
    private int roundNumber = 0;
    private boolean topRightWin = true;
    private Board copyOfBoard;
   
    public Player() {
        tileBoard = new TileBoard(sizeX, sizeY);
        enemyShots = new double[sizeX][sizeY];
    }

    public void resetEnemyShots() {
        for (int i = 0; i < enemyShots.length; i++) {
            for (int j = 0; j < enemyShots[i].length; j++) {
                enemyShots[i][j] = 0;
            }
        }
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
        /*
        if (topRightWin) {
            System.out.println("Running topRight!");
            placeShipsCorner(fleet, board);
            return;
        }
        */
        System.out.println("Running heatmap placements");
        //Example: Pos(0, 1)
        //Get heatmap:
        //[0] [ ] [ ] [ ] [ ] [ ]
        //[0] [ ] [ ] [ ] [ ] [ ]
        //[0] [ ] [ ] [1] [ ] [ ]
        //[1] [ ] [ ] [ ] [ ] [ ]
        //[0] [ ] [ ] [ ] [ ] [ ]
        //Calc = (PositionHits * 100) / All shots;

        int[] shipSizes = {5, 4, 3, 3, 2};
        int[][] shipPlacements = new int[sizeX][sizeY];
        int decrement = 4;
        for (int size : shipSizes) {
            double bestShipScore = Double.MAX_VALUE;
            Position bestPosition = new Position(0, 0);
            boolean bestPlacementVertical = false;
            for (int vert = 0; vert <= 1; vert++) {
                boolean vertical = (vert == 0);
                for (int x = 0; x < (vertical ? sizeX : sizeX - size); x++) {
                    for (int y = 0; y < (vertical ? sizeY - size : sizeY); y++) {
                        if (shipPlacements[x][y] == 0) {
                            double currentScore = getShipPlacementScore(shipPlacements, new Position(x, y), size, vertical);
                            //System.out.println("Checking ship " + size + " (" + x + ", " + y + ") with score: " + currentScore);
                            if (currentScore < bestShipScore) {
                                //System.out.println("Found a new best positon for ship size: " + size + " at: (" + x + ", " + y + ") with score: " + currentScore);
                                bestPosition = new Position(x, y);
                                bestPlacementVertical = vertical;
                                bestShipScore = currentScore;
                            } 
                        } else {
                            //System.out.println("Checking ship " + size + " (" + x + ", " + y + ") --- SKIPPING ---");
                        }
                    }
                }
            }
            //System.out.println("[Round: " + roundNumber + "] Placing ship " + size + " on Position: " + bestPosition.x + ", " + bestPosition.y + " " + (bestPlacementVertical ? "vertically" : "horizontally"));
            board.placeShip(bestPosition, fleet.getShip(decrement--), bestPlacementVertical);
            for (int x = bestPosition.x; x < (bestPlacementVertical ? bestPosition.x + 1 : bestPosition.x + size); x++) {
                for (int y = bestPosition.y; y < (bestPlacementVertical ? bestPosition.y + size : bestPosition.y + 1); y++) {
                    shipPlacements[x][y] = size;
                }
            }
            //Example: size == 5
            //Next step? iterate over all horizontal spaces that are valid, then vertical spaces that are valid.
            //Save the startPosition for whatever has the lowest score
        }
        
        if (roundNumber == 5) {
            //Print board here
            System.out.println("---------");
            for (int y = 0; y < shipPlacements[0].length; y++) {
                String s = "";
                for (int x = 0; x < shipPlacements.length; x++) {
                    int value = shipPlacements[x][y];
                    s += (value == 0 ? "-" : value);
                }
                System.out.println(s);
            }
        }
        
        
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
    
    private double getShipPlacementScore(int[][] shipPlacements, Position startPosition, int shipSize, boolean vertical) {
        double score = 0;
        int baseX = startPosition.x;
        int baseY = startPosition.y;
        for (int x = 0; x < (vertical ? 1 : shipSize); x++) {
            for (int y = 0; y < (vertical ? shipSize : 1); y++) {
                if (shipPlacements[baseX + x][baseY + y] != 0) {
                    return Double.MAX_VALUE;
                }
                score += enemyShots[baseX + x][baseY + y];
            }
        }
        return score;
    }
    
    public void placeShipsCorner(Fleet fleet, Board board)
    {
        myBoard = board;
        sizeX = board.sizeX();
        sizeY = board.sizeY();
        boolean vertical = false;
        Position startPos = new Position(4,5);
        //Ship positions
        Position[] posAr = new Position[5];
        posAr[0] = new Position(startPos.x + 1, startPos.y + 3);        //2
        posAr[1] = new Position(startPos.x + 3, startPos.y + 3);        //3
        posAr[2] = new Position(startPos.x + 3, startPos.y + 1);      //3
        posAr[3] = new Position(startPos.x + 2, startPos.y + 2);      //4
        posAr[4] = new Position(startPos.x + 1, startPos.y + 4);      //5
       
        for (int i = 0; i < fleet.getNumberOfShips(); i++) {
            Ship s = fleet.getShip(i);
            board.placeShip(posAr[i], s, vertical);
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
        
        if (incrementer < 0.025) {
            incrementer = 0.025;
        }
        
        //Example: Pos(0, 1)
        //Get heatmap:
        //[0] [ ] [ ] [ ] [ ] [ ]
        //[0] [ ] [ ] [ ] [ ] [ ]
        //[0] [ ] [ ] [1] [ ] [ ]
        //[1] [ ] [ ] [ ] [ ] [ ]
        //[0] [ ] [ ] [ ] [ ] [ ]
        //Calc = (PositionHits * 100) / All shots;

        enemyShots[pos.x][pos.y] += 1 + incrementer;
        incrementer -= 0.025;
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
                if (t.getHeuristicValue() < highestHeuristic.getHeuristicValue()) {
                    highestHeuristic = t;
                }
            }
            if (rnd.nextInt(100) > 90) {
                highestHeuristic = suitableTiles.get(rnd.nextInt(suitableTiles.size()));
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
                if (t.getHeuristicValue() < highestHeuristic.getHeuristicValue()) {
                    highestHeuristic = t;
                }
            }
            if (rnd.nextInt(100) > 90) {
                highestHeuristic = suitableTiles.get(rnd.nextInt(suitableTiles.size()));
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
        //Clear enemy shots???
        resetEnemyShots();
    }
    
    
    /**
     * Called at the beginning of each round.
     * @param round int the current round number.
     */
    @Override
    public void startRound(int round) {
        //Do nothing
        roundNumber = round;
        tileBoard = new TileBoard(sizeX, sizeY);
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
        if (points <= enemyPoints) {
            topRightWin = false;
        }
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

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package r2;

import battleship.interfaces.BattleshipsPlayer;
import tournament.player.PlayerFactory;

/**
 *
 * @author Tobias Grundtvig
 */
public class R2 implements PlayerFactory<BattleshipsPlayer> {

    public R2(){
    
    }
    
    @Override
    public BattleshipsPlayer getNewInstance() {
        return new Player();
    }

    @Override
    public String getID() {
        return "E3";
    }

    @Override
    public String getName() {
        return "E3 player";
    }
    
}
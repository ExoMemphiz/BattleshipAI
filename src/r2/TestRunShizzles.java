/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package r2;

import battleshipstournament.TestTournament;
import battleshipstournament.SingleRoundVisualizer;
import java.util.ArrayList;
import tournament.impl.ParticipantInfo;

/**
 *
 * @author CHRIS
 */
public class TestRunShizzles {
 
    public static void main(String[] args) {
        RunTournament();
    }
    
    private static ArrayList<ParticipantInfo> RunTournament() {
        battleship.interfaces.BattleshipsPlayer p = (new R2()).getNewInstance();
        SingleRoundVisualizer.chrisMain(p);
        return null;
    }
    
}
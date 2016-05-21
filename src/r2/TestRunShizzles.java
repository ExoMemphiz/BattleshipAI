/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package r2;

import battleshipstournament.ExtraGameResult;
import battleshipstournament.TestTournament;
import battleshipstournament.SingleRoundVisualizer;
import java.util.ArrayList;
import tournament.game.GameResult;
import tournament.impl.ParticipantInfo;

/**
 *
 * @author CHRIS
 */
public class TestRunShizzles {
 
    public static void main(String[] args) {
        RunSingle();
    }
    
    private static void RunSingle() {
        battleship.interfaces.BattleshipsPlayer p = (new R2()).getNewInstance();
        int playerAScore = 0;
        int playerBScore = 0;
        for (int i = 0; i < 1; i++) {
            ExtraGameResult result = SingleRoundVisualizer.chrisMain(p);
            playerAScore += result.getRes().minorPointsA;
            playerBScore += result.getRes().minorPointsB;
            
        }
        
        System.out.println("Player R2: " + playerAScore);
        System.out.println("Player Systematic shooter: " + playerBScore);
    }
    
    private static void RunTournament() {
        printScore(new TestTournament(new R2(), null).run());
    }
    
       
    public static void printScore(ArrayList<ParticipantInfo> list) {
        for (ParticipantInfo p : list) {
            System.out.println(p.getName() + " has " + p.getMinorPoints() + " minor points.");
        }
    }
    
}
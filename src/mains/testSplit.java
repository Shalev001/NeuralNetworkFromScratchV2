/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mains;

import MatrixVector.Vector;
import chess.ChessBoard;

/**
 *
 * @author shale
 */
public class testSplit {

    public static void main(String[] args) {

        int turncount = 0;
        int whiteWins = 0;
        int blackWins = 0;
        int staleMates = 0;

        for (int i = 0; i < 100; i++) {

            ChessBoard cb = new ChessBoard();

            while (!cb.checkMate() && !cb.staleMate()) {
                
                cb.takeRandomMove();
                System.out.println(cb.toString());
                turncount++;
                
            }
            if(cb.staleMate()){
                staleMates++;
            }
            else if (cb.getTurn() == 0) {// if black is in checkmate, white wins and vise versa
                whiteWins++;
            } else if (cb.getTurn() == 1) {
                blackWins++;
            }
            if (i % 1 == 0){
                System.out.println(i);
            }
        }
        System.out.println("average turn count: " + ((double) turncount/100));
        System.out.println("white win %: " + (whiteWins));
        System.out.println("black win %: " + (blackWins));
        System.out.println("stalemate %: " + (staleMates));
    }

}

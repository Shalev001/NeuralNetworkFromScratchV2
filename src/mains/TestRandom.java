/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mains;

import MatrixVector.Vector;
import chess.ChessBoard;
import chess.King;

/**
 *
 * @author shale
 */
public class TestRandom {

    public static void main(String[] args) {

        int turncount = 0;
        int whiteWins = 0;
        int blackWins = 0;
        int staleMates = 0;

        for (int i = 0; i < 1000; i++) {

            ChessBoard cb = new ChessBoard();
            // this is a bit jank but what it does is makes the king aware of its position on the board so it can know
            //if it is in check or not. this cannot be done in the constructor because you can not referance an object inside itself
            ((King)(cb.getWhite().get(0))).setBoard(cb);
            ((King)(cb.getBlack().get(0))).setBoard(cb);

            while (!cb.checkMate() && !cb.staleMate()) {
                
                cb.takeRandomMove();
                //System.out.println(cb.toString());
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
            if (i % 10 == 0){
                System.out.println(i);
            }
        }
        System.out.println("average turn count: " + ((double) turncount/1000));
        System.out.println("white win %: " + ((double)whiteWins)/10);
        System.out.println("black win %: " + ((double)blackWins)/10);
        System.out.println("stalemate %: " + ((double)staleMates)/10);
    }

}

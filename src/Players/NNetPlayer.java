/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Players;

import activationFunctions.Function;
import chess.ChessBoard;
import chess.Piece;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import neuralnetwork.Network;

/**
 *
 * @author shale
 */
public class NNetPlayer implements Player {

    Network nnet;
    Function actiFunc;
    int searchDepth;

    public NNetPlayer(String address, Function actiFunc, int searchDepth) {

        this.actiFunc = actiFunc;
        this.searchDepth = searchDepth;

        File file = new File(address);

        nnet = null;

        try {
            nnet = Network.importf(file);
        } catch (IOException ex) {
            Logger.getLogger(NNetPlayer.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public double evaluate(ChessBoard cb) {

        nnet.setInput(cb.toNNetInput());

        nnet.compute(actiFunc);

        //the network itself flips the value if the team is white so this is fixing the input
        int multiplier = (cb.getTurn() == 1) ? -1 : 1;

        return (nnet.getOutput().getValue(0) * 2 - 1) * multiplier;

    }

    //this function is copied from Neural Networks for Chess by Dominik Klein
    public double alphaBeta(ChessBoard cb, int depth, double alpha, double beta, boolean maximize) {

        if (cb.checkMate()) {
            if (cb.getTurn() == 1) {
                return -10000;
            } else {
                return 10000;
            }
        }

        if (depth == 0) {
            return evaluate(cb);
        }

        ArrayList<int[]> LegalMoves = cb.getLegalMoves();

        double bestVal;

        if (maximize) {

            bestVal = -99999;

            for (int[] move : LegalMoves) {

                // taking note of the turn and boardState so that the turn can be undone
                int turn = cb.getTurn();

                ArrayList<Piece> team = (turn == 0) ? cb.getBlack() : cb.getWhite();
                ArrayList<Piece> otherTeam = (turn == 0) ? cb.getWhite() : cb.getBlack();

                ArrayList<Piece> preTurnAllies = new ArrayList<>();
                for (Piece alliedPiece : team) {
                    preTurnAllies.add(alliedPiece.clone());
                }

                ArrayList<Piece> preTurnEnemy = new ArrayList<>();
                for (Piece enemyPiece : otherTeam) {
                    preTurnEnemy.add(enemyPiece.clone());
                }

                cb.takeNextTurn(move[0], move[1], move[2], move[3]);

                double alphaBeta = alphaBeta(cb, depth - 1, alpha, beta, !maximize);

                //taking the max between bestVal and the rating for the current move
                bestVal = (bestVal >= alphaBeta) ? bestVal : alphaBeta;

                //restoring previos board position
                cb.setTurn(turn);
                if (turn == 0) {
                    cb.setBlack(preTurnAllies);
                    cb.setWhite(preTurnEnemy);
                } else {
                    cb.setWhite(preTurnAllies);
                    cb.setBlack(preTurnEnemy);
                }

                alpha = (alpha >= bestVal) ? alpha : bestVal;
                if (alpha >= beta) {
                    return bestVal;
                }
            }
            return bestVal;
        } else {

            bestVal = 99999;

            for (int[] move : LegalMoves) {

                // taking note of the turn and boardState so that the turn can be undone
                int turn = cb.getTurn();

                ArrayList<Piece> team = (turn == 0) ? cb.getBlack() : cb.getWhite();
                ArrayList<Piece> otherTeam = (turn == 0) ? cb.getWhite() : cb.getBlack();

                ArrayList<Piece> preTurnAllies = new ArrayList<>();
                for (Piece alliedPiece : team) {
                    preTurnAllies.add(alliedPiece.clone());
                }

                ArrayList<Piece> preTurnEnemy = new ArrayList<>();
                for (Piece enemyPiece : otherTeam) {
                    preTurnEnemy.add(enemyPiece.clone());
                }

                cb.takeNextTurn(move[0], move[1], move[2], move[3]);

                double alphaBeta = alphaBeta(cb, depth - 1, alpha, beta, !maximize);

                //taking the min between bestVal and the rating for the current move
                bestVal = (bestVal <= alphaBeta) ? bestVal : alphaBeta;

                //restoring previos board position
                cb.setTurn(turn);
                if (turn == 0) {
                    cb.setBlack(preTurnAllies);
                    cb.setWhite(preTurnEnemy);
                } else {
                    cb.setWhite(preTurnAllies);
                    cb.setBlack(preTurnEnemy);
                }

                beta = (beta <= bestVal) ? beta : bestVal;
                if (beta <= alpha) {
                    return bestVal;
                }
            }
            return bestVal;
        }

    }

    public void takeNextTurn(ChessBoard cb) {

        ArrayList<int[]> moves = cb.getLegalMoves();

        // taking note of the turn and boardState so that the turn can be undone
        int turn = cb.getTurn();

        boolean maximize = (turn == 1);

        double bestValue = (turn == 1) ? -99999 : 99999;
        int[] bestMove = {0, 0, 0, 0};

        for (int[] move : moves) {

            ArrayList<Piece> team = (turn == 0) ? cb.getBlack() : cb.getWhite();
            ArrayList<Piece> otherTeam = (turn == 0) ? cb.getWhite() : cb.getBlack();

            ArrayList<Piece> preTurnAllies = new ArrayList<>();
            for (Piece alliedPiece : team) {
                preTurnAllies.add(alliedPiece.clone());
            }

            ArrayList<Piece> preTurnEnemy = new ArrayList<>();
            for (Piece enemyPiece : otherTeam) {
                preTurnEnemy.add(enemyPiece.clone());
            }

            cb.takeNextTurn(move[0], move[1], move[2], move[3]);

            if (maximize) {
                double alphaBeta = alphaBeta(cb, searchDepth - 1, -99999, 99999, !maximize);
                if (alphaBeta > bestValue) {
                    bestValue = alphaBeta;
                    bestMove = move;
                }
            } else {
                double alphaBeta = alphaBeta(cb, searchDepth - 1, -99999, 99999, !maximize);
                if (alphaBeta < bestValue) {
                    bestValue = alphaBeta;
                    bestMove = move;
                }
            }
            
            //reseting the board state
            cb.setTurn(turn);
            if (turn == 0) {
                cb.setBlack(preTurnAllies);
                cb.setWhite(preTurnEnemy);
            } else {
                cb.setWhite(preTurnAllies);
                cb.setBlack(preTurnEnemy);
            }
        }
        
        cb.takeNextTurn(bestMove[0], bestMove[1], bestMove[2], bestMove[3]);
    }

}

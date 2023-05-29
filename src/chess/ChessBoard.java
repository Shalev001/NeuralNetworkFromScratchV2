/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package chess;

import java.util.ArrayList;
import java.util.Collections;
import MatrixVector.Vector;

/**
 *
 * @author shale
 */
public class ChessBoard {

    ArrayList<Piece> white;
    ArrayList<Piece> black;
    int turn;// 0 = black turn, 1 = white turn

    public ChessBoard() {

        white = new ArrayList();
        black = new ArrayList();
        turn = 1;

        white.add(new King(5, 1, 1));// kings will always be at index 0

        for (int i = 1; i <= 8; i++) {
            white.add(new Pawn(i, 2, 1));
        }

        white.add(new Rook(1, 1, 1));
        white.add(new Rook(8, 1, 1));

        white.add(new Knight(2, 1, 1));
        white.add(new Knight(7, 1, 1));

        white.add(new Bishop(3, 1, 1));
        white.add(new Bishop(6, 1, 1));

        white.add(new Queen(4, 1, 1));

        black.add(new King(5, 8, 0));

        for (int i = 1; i <= 8; i++) {
            black.add(new Pawn(i, 7, 0));
        }

        black.add(new Rook(1, 8, 0));
        black.add(new Rook(8, 8, 0));

        black.add(new Knight(2, 8, 0));
        black.add(new Knight(7, 8, 0));

        black.add(new Bishop(3, 8, 0));
        black.add(new Bishop(6, 8, 0));

        black.add(new Queen(4, 8, 0));
    }
    
    public boolean legalMove(int x1, int y1, int x2, int y2){
        
        ArrayList<Piece> team;
        ArrayList<Piece> otherTeam;
        
        if (turn == 0){
            team = black;
            otherTeam = white;
        }else{
            team = white;
            otherTeam = black;
        }
        
        for(Piece piece : team){
            if(piece.getPieceLocation()[0] == x1 && piece.getPieceLocation()[1] == y1 && piece.canMove(x2, y2, otherTeam, team)){
                return true;
            }
        }
        
        return false;
    }

    /**
     * returns the index of the threatening piece
     * @param colour
     * @return 
     */
    public int inCheck(int colour) {

        ArrayList<Piece> enemyPieces;
        ArrayList<Piece> alliedPieces;
        int xLoc;
        int yLoc;

        if (colour == 0) {
            enemyPieces = white;
            alliedPieces = black;
            xLoc = black.get(0).getPieceLocation()[0];
            yLoc = black.get(0).getPieceLocation()[1];
        } else {
            enemyPieces = black;
            alliedPieces = white;
            xLoc = white.get(0).getPieceLocation()[0];
            yLoc = white.get(0).getPieceLocation()[1];
        }

        for (int i = 0; i < enemyPieces.size(); i++) {
            if (enemyPieces.get(i).canMove(xLoc, yLoc, alliedPieces, enemyPieces)) {
                ((King) alliedPieces.get(0)).setInCheck(true);
                return i;
            }
        }
        ((King) alliedPieces.get(0)).setInCheck(false);
        return -1;
    }

    private boolean wouldBeInCheck(int xLoc, int yLoc, int colour) {

        int xOrg;
        int yOrg;
        ArrayList<Piece> enemyPieces;
        ArrayList<Piece> alliedPieces;
        ArrayList<Piece> preTurnEnemy;

        if (colour == 0) {
            xOrg = black.get(0).pieceLocation[0];
            yOrg = black.get(0).pieceLocation[1];
            enemyPieces = white;
            alliedPieces = black;

            preTurnEnemy = (ArrayList<Piece>) white.clone();

        } else {
            xOrg = white.get(0).pieceLocation[0];
            yOrg = white.get(0).pieceLocation[1];
            enemyPieces = black;
            alliedPieces = white;

            preTurnEnemy = (ArrayList<Piece>) black.clone();
        }

        boolean flag = false;

        if (alliedPieces.get(0).move(xLoc, yLoc, enemyPieces, alliedPieces)) {

            for (int i = 0; i < enemyPieces.size(); i++) {
                if (enemyPieces.get(i).canMove(xLoc, yLoc, alliedPieces, enemyPieces)) {
                    alliedPieces.get(0).move(xOrg, yOrg, enemyPieces, alliedPieces);
                    alliedPieces.get(0).movecountdown(2);
                    enemyPieces = preTurnEnemy;
                    flag = true;
                }
            }
            alliedPieces.get(0).move(xOrg, yOrg, enemyPieces, alliedPieces);
            alliedPieces.get(0).movecountdown(2);
            enemyPieces = preTurnEnemy;
        }

        if (colour == 0) {
            white = enemyPieces;
            black = alliedPieces;

        } else {
            black = enemyPieces;
            white = alliedPieces;
        }

        return flag;
    }

    private boolean KingUnableToMove(int colour) {

        int kingxLoc;
        int kingyLoc;

        if (colour == 0) {
            kingxLoc = black.get(0).getPieceLocation()[0];
            kingyLoc = black.get(0).getPieceLocation()[1];
        } else {
            kingxLoc = white.get(0).getPieceLocation()[0];
            kingyLoc = white.get(0).getPieceLocation()[1];
        }

        boolean flag = true;

        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if ((j != 0 || i != 0) && (Piece.SonBoard(kingxLoc + j, kingyLoc + i))) {

                    if (!wouldBeInCheck(kingxLoc + j, kingyLoc + i, colour)) {
                        flag = false;
                    }
                }
            }
        }
        return flag;
    }

    public boolean takeNextTurn(int pxLoc, int pyLoc, int nxLoc, int nyLoc) {
        if (turn == 0) {
            ArrayList<Piece> preTurnEnemy = new ArrayList<>();
            for (int i = 0; i < white.size(); i++) {
                preTurnEnemy.add(null);
            }
            Collections.copy(preTurnEnemy, white);
            for (int i = 0; i < black.size(); i++) {
                if (black.get(i).getPieceLocation()[0] == pxLoc && black.get(i).getPieceLocation()[1] == pyLoc) {
                    if (black.get(i).move(nxLoc, nyLoc, white, black)) {
                        if (inCheck(turn) != -1) {
                            black.get(i).move(pxLoc, pyLoc, white, black);
                            black.get(i).movecountdown(2);
                            white = preTurnEnemy;
                            return false;
                        }
                        turn = 1;
                        return true;
                    }
                }
            }
        } else if (turn == 1) {
            ArrayList<Piece> preTurnEnemy = new ArrayList<Piece>();
            for (int i = 0; i < black.size(); i++) {
                preTurnEnemy.add(null);
            }
            Collections.copy(preTurnEnemy, black);
            for (int i = 0; i < white.size(); i++) {
                if (white.get(i).getPieceLocation()[0] == pxLoc && white.get(i).getPieceLocation()[1] == pyLoc) {
                    if (white.get(i).move(nxLoc, nyLoc, black, white)) {
                        if (inCheck(turn) != -1) {
                            white.get(i).move(pxLoc, pyLoc, black, white);
                            white.get(i).movecountdown(2);
                            black = preTurnEnemy;
                            return false;
                        }
                        turn = 0;
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public boolean checkMate(int colour) {//########## this is the problem

        //this if statement is broken
        if (!KingUnableToMove(colour)) {// if the king can move, not checkmate
            return false;
        }

        ArrayList<Piece> enemyPieces;
        ArrayList<Piece> alliedPieces;
        int xLoc;
        int yLoc;

        if (colour == 0) {
            enemyPieces = white;
            alliedPieces = black;
            xLoc = black.get(0).getPieceLocation()[0];
            yLoc = black.get(0).getPieceLocation()[1];
        } else {
            enemyPieces = black;
            alliedPieces = white;
            xLoc = white.get(0).getPieceLocation()[0];
            yLoc = white.get(0).getPieceLocation()[1];
        }

        //finding the piece that is threataning the king
        if (inCheck(colour) == -1) {
            return false;
        }
        Piece threataningPiece = enemyPieces.get(inCheck(colour));

        //if the piece is a pawn or knight the only option is to take it
        if (threataningPiece.getPieceNum() == 0 || threataningPiece.getPieceNum() == 2) {
            //checking if it can be taken
            for (int i = 1; i < alliedPieces.size(); i++) {//excluding king since it must be stuck
                if (alliedPieces.get(i).canMove(threataningPiece.getPieceLocation()[0],
                        threataningPiece.getPieceLocation()[0], enemyPieces, alliedPieces)) {
                    return false;
                }
            }
            return true;
        } //if the piece is any other piece it can either be blocked or taken
        else {
            //checking if it can be taken
            for (int i = 1; i < alliedPieces.size(); i++) {//excluding king since it must be stuck
                if (alliedPieces.get(i).canMove(threataningPiece.getPieceLocation()[0],
                        threataningPiece.getPieceLocation()[0], enemyPieces, alliedPieces)) {
                    return false;
                }
            }
            for (int[] spaceBetween : threataningPiece.spacesBetween(xLoc, yLoc)) {
                for (int i = 1; i < alliedPieces.size(); i++) {//excluding king since it must be stuck
                    if (alliedPieces.get(i).canMove(spaceBetween[0],
                            spaceBetween[1], enemyPieces, alliedPieces)) {
                        return false;
                    }
                }
            }
            return true;
        }

    }

    public ArrayList<Piece> getWhite() {
        return white;
    }

    public ArrayList<Piece> getBlack() {
        return black;
    }

    /**
     * a method to take the current board state and convert it into input for a
     * neural network. follows the format outlined in the design document
     *
     * @param colour
     * @return
     */
    public Vector toNNetInput() {

        double[] output = new double[65];//every piece on the board plus an entry for the colour

        output[64] = turn; //the last entry is the colour;

        for (Piece piece : white) {
            //             xLocation              +          yLocation * 8    adding one since the first index should be the colour
            output[(piece.getPieceLocation()[0] - 1 + (piece.getPieceLocation()[1] - 1) * 8)] = piece.getPieceNum() + 1;//entering the piece code for the specific piece
        }
        for (Piece piece : black) {
            output[(piece.getPieceLocation()[0] - 1 + (piece.getPieceLocation()[1] - 1) * 8)] = piece.getPieceNum() + 7;
        }

        return new Vector(output);
    }
    
    /**
     * a method to convert an integer indexing a 4 dimensional space with 8 entries per dimension into coordinates
     * the first two dimensions are the original x/y and the second two are the new x/y
     * @param index
     * @return coordinates in the format: x1, y1, x2, y2
     */
    public static int[] indexToCoordinates(int index){
        
        int[] output = new int[4];
        
        output[1] = index / 512;
        
        output[0] = (index % 512) / 64;
        
        output[3] = ((index % 512) % 64) / 8;
        
        output[2] = ((index % 512) % 64) % 8;
        
        return output;
    }

    public int getEnemyPointTotal() {

        int sum = 0;

        if (turn == 0) {
            for (Piece piece : white) {
                sum += piece.getPieceValue();
            }
        } else {
            for (Piece piece : black) {
                sum += piece.getPieceValue();
            }
        }

        return sum;
    }

    public int getAlliedPointTotal() {

        int sum = 0;

        if (turn == 0) {
            for (Piece piece : black) {
                sum += piece.getPieceValue();
            }
        } else {
            for (Piece piece : white) {
                sum += piece.getPieceValue();
            }
        }

        return sum;
    }

    public int getTurn() {
        return turn;
    }

    public String toString() {
        String str = "";
        boolean flag;

        for (int k = 8; k >= 1; k--) {
            for (int i = 1; i <= 8; i++) {
                str += "|";
                flag = false;
                for (int j = 0; j < black.size(); j++) {
                    if (black.get(j).getPieceLocation()[0] == i && black.get(j).getPieceLocation()[1] == k) {
                        switch (black.get(j).getPieceNum()) {
                            case 0:
                                str += "BPn";
                                break;
                            case 1:
                                str += "BBp";
                                break;
                            case 2:
                                str += "BKn";
                                break;
                            case 3:
                                str += "BRk";
                                break;
                            case 4:
                                str += "BQn";
                                break;
                            case 5:
                                str += "BKg";
                                break;
                        }
                        flag = true;
                    }
                }
                for (int j = 0; j < white.size(); j++) {

                    if (white.get(j).getPieceLocation()[0] == i && white.get(j).getPieceLocation()[1] == k) {
                        switch (white.get(j).getPieceNum()) {
                            case 0:
                                str += "WPn";
                                break;
                            case 1:
                                str += "WBp";
                                break;
                            case 2:
                                str += "WKn";
                                break;
                            case 3:
                                str += "WRk";
                                break;
                            case 4:
                                str += "WQn";
                                break;
                            case 5:
                                str += "WKg";
                                break;
                        }
                        flag = true;
                    }
                }
                if (!flag) {
                    if ((i + k) % 2 == 0) {
                        str += " x ";
                    } else {
                        str += "   ";
                    }
                }
            }
            str += "|\n --- --- --- --- --- --- --- ---\n";
        }
        return str;
    }

}

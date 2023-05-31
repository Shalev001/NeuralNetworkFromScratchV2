/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package chess;

import java.util.ArrayList;

/**
 *
 * @author shale
 */
public class Knight extends Piece {

    public Knight(int xLoc, int yLoc, int pieceColor) {
        super(xLoc, yLoc, pieceColor);

        pieceNum = 2;

        pieceValue = 3;
    }
    
    public Piece clone(){
        Piece out = new Knight(pieceLocation[0],pieceLocation[1],pieceColour);
        out.setMoveCount(moveCount);
        return out;
    }

    public int[][] spacesBetween(int xLoc, int yLoc) {
        return null;
    }

    public boolean move(int xLoc, int yLoc, ArrayList<Piece> enemyPieces, ArrayList<Piece> alliedPieces) {

        if (!onBoard(xLoc, yLoc) || alliedPieceThere(xLoc, yLoc, alliedPieces)) {
            return false;
        }

        int xdiff = xLoc - pieceLocation[0];
        int ydiff = yLoc - pieceLocation[1];

        if ((Math.abs(xdiff) + Math.abs(ydiff) == 3) && xdiff != 0 && ydiff != 0) {

            if (enemyPieceThere(xLoc, yLoc, enemyPieces) != -1) {
                enemyPieces.remove(enemyPieceThere(xLoc, yLoc, enemyPieces));
            }

            pieceLocation[0] = xLoc;
            pieceLocation[1] = yLoc;
            moveCount++;
            return true;
        }

        return false;
    }

    public boolean canMove(int xLoc, int yLoc, ArrayList<Piece> enemyPieces, ArrayList<Piece> alliedPieces) {

        if (!onBoard(xLoc, yLoc) || alliedPieceThere(xLoc, yLoc, alliedPieces)) {
            return false;
        }

        int xdiff = xLoc - pieceLocation[0];
        int ydiff = yLoc - pieceLocation[1];

        if ((Math.abs(xdiff) + Math.abs(ydiff) == 3) && xdiff != 0 && ydiff != 0) {

            return true;
        }

        return false;
    }

}

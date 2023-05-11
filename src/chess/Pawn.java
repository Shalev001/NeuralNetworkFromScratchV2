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
public class Pawn extends Piece {

    public Pawn(int xLoc, int yLoc, int pieceColor) {
        super(xLoc, yLoc, pieceColor);

        pieceNum = 0;

        pieceValue = 1;
    }
    
    public int[][] spacesBetween(int xLoc,int yLoc){
        return null;
    }
    
    public boolean move(int xLoc, int yLoc, ArrayList<Piece> enemyPieces, ArrayList<Piece> alliedPieces) {

        if (!onBoard(xLoc, yLoc) || alliedPieceThere(xLoc, yLoc, alliedPieces)) {
            return false;
        }
        if (pieceColour == 1) {// for pawns the moves that can be done depend on the colour
            //default move
            if (pieceLocation[0] == xLoc && pieceLocation[1] == (yLoc - 1)) {

                if (enemyPieceThere(xLoc, yLoc, enemyPieces) != -1 || alliedPieceThere(xLoc, yLoc, alliedPieces)) {
                    return false;
                }

                pieceLocation[1] = yLoc;
                moveCount++;
                return true;
            } //first move can go two spaces
            else if (moveCount == 0 && pieceLocation[0] == xLoc && pieceLocation[1] == (yLoc - 2)) {

                if ((enemyPieceThere(xLoc, yLoc, enemyPieces) != -1) || (enemyPieceThere(xLoc, yLoc - 1, enemyPieces) != -1)
                        || (alliedPieceThere(xLoc, yLoc, alliedPieces)) || alliedPieceThere(xLoc, yLoc - 1, alliedPieces)) {
                    return false;
                }

                pieceLocation[1] = yLoc;
                moveCount++;
                return true;
            } //taking move
            else if ((pieceLocation[0] == xLoc + 1 || pieceLocation[0] == xLoc - 1)
                    && enemyPieceThere(xLoc, yLoc, enemyPieces) != -1 && pieceLocation[1] == (yLoc - 1)) {

                enemyPieces.remove(enemyPieceThere(xLoc, yLoc, enemyPieces));

                pieceLocation[0] = xLoc;
                pieceLocation[1] = yLoc;
                moveCount++;
                return true;
            }
        } else {
            //default move
            if (pieceLocation[0] == xLoc && pieceLocation[1] == (yLoc + 1)) {

                if (enemyPieceThere(xLoc, yLoc, enemyPieces) != -1 || alliedPieceThere(xLoc, yLoc, alliedPieces)) {
                    return false;
                }

                pieceLocation[1] = yLoc;
                moveCount++;
                return true;
            } //first move can go two spaces
            else if (moveCount == 0 && pieceLocation[0] == xLoc && pieceLocation[1] == (yLoc + 2)) {

                if (enemyPieceThere(xLoc, yLoc, enemyPieces) != -1 || (enemyPieceThere(xLoc, yLoc + 1, enemyPieces) != -1)
                        || (alliedPieceThere(xLoc, yLoc, alliedPieces)) || alliedPieceThere(xLoc, yLoc + 1, alliedPieces)) {
                    return false;
                }

                pieceLocation[1] = yLoc;
                moveCount++;
                return true;
            } //taking move
            else if ((pieceLocation[0] == xLoc + 1 || pieceLocation[0] == xLoc - 1)
                    && enemyPieceThere(xLoc, yLoc, enemyPieces) != -1 && pieceLocation[1] == (yLoc + 1)) {

                enemyPieces.remove(enemyPieceThere(xLoc, yLoc, enemyPieces));

                pieceLocation[0] = xLoc;
                pieceLocation[1] = yLoc;
                moveCount++;
                return true;
            }
        }

        return false;
    }

    //same as the move method but doesn't actually change any values
    public boolean canMove(int xLoc, int yLoc, ArrayList<Piece> enemyPieces, ArrayList<Piece> alliedPieces) {

        if (!onBoard(xLoc, yLoc) || alliedPieceThere(xLoc, yLoc, alliedPieces)) {
            return false;
        }
        if (pieceColour == 1) {// for pawns the moves that can be done depend on the colour
            //default move
            if (pieceLocation[0] == xLoc && pieceLocation[1] == (yLoc - 1)) {

                if (enemyPieceThere(xLoc, yLoc, enemyPieces) != -1 || alliedPieceThere(xLoc, yLoc, alliedPieces)) {
                    return false;
                }

                return true;
            } //first move can go two spaces
            else if (moveCount == 0 && pieceLocation[0] == xLoc && pieceLocation[1] == (yLoc - 2)) {

                if (enemyPieceThere(xLoc, yLoc, enemyPieces) != -1 || (enemyPieceThere(xLoc, yLoc - 1, enemyPieces) != -1)
                        || (alliedPieceThere(xLoc, yLoc, alliedPieces)) || alliedPieceThere(xLoc, yLoc - 1, alliedPieces)) {
                    return false;
                }

                return true;
            } //taking move
            else if ((pieceLocation[0] == xLoc + 1 || pieceLocation[0] == xLoc - 1)
                    && enemyPieceThere(xLoc, yLoc, enemyPieces) != -1 && pieceLocation[1] == (yLoc - 1)) {

                return true;
            }
        } else {
            //default move
            if (pieceLocation[0] == xLoc && pieceLocation[1] == (yLoc + 1)) {

                if (enemyPieceThere(xLoc, yLoc, enemyPieces) != -1 || alliedPieceThere(xLoc, yLoc, alliedPieces)) {
                    return false;
                }

                return true;
            } //first move can go two spaces
            else if (moveCount == 0 && pieceLocation[0] == xLoc && pieceLocation[1] == (yLoc + 2)) {

                if (enemyPieceThere(xLoc, yLoc, enemyPieces) != -1 || (enemyPieceThere(xLoc, yLoc + 1, enemyPieces) != -1)
                        || (alliedPieceThere(xLoc, yLoc, alliedPieces)) || alliedPieceThere(xLoc, yLoc + 1, alliedPieces)) {
                    return false;
                }

                return true;
            } //taking move
            else if ((pieceLocation[0] == xLoc + 1 || pieceLocation[0] == xLoc - 1)
                    && enemyPieceThere(xLoc, yLoc, enemyPieces) != -1 && pieceLocation[1] == (yLoc + 1)) {

                return true;
            }
        }

        return false;
    }
}

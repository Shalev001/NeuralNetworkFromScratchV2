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
public class King extends Piece {

    boolean inCheck;

    // variables to keep track of the allied rooks for casteling
    Rook KSideR;
    Rook QSideR;

    public King(int xLoc, int yLoc, int pieceColor) {
        super(xLoc, yLoc, pieceColor);

        pieceNum = 5;

        pieceValue = 0;

        inCheck = false;
        
        KSideR = null;
        QSideR = null;
    }

    public void setKSideR(Rook KSideR) {
        this.KSideR = KSideR;
    }

    public void setQSideR(Rook QSideR) {
        this.QSideR = QSideR;
    }
    
    public boolean isInCheck() {
        return inCheck;
    }

    public void setInCheck(boolean inCheck) {
        this.inCheck = inCheck;
    }

    public int[][] spacesBetween(int xLoc, int yLoc) {
        return null;
    }

    public boolean move(int xLoc, int yLoc, ArrayList<Piece> enemyPieces, ArrayList<Piece> alliedPieces) {

        if (!onBoard(xLoc, yLoc) || alliedPieceThere(xLoc, yLoc, alliedPieces) || !canMove(xLoc, yLoc, enemyPieces, alliedPieces)) {
            return false;
        }

        if (pieceColour == 1) {
            if (xLoc == 7 && yLoc == 1 && moveCount == 0 && KSideR.getMoveCount() == 0) {
                
                KSideR.move(6, 1, enemyPieces, alliedPieces);
                pieceLocation[0] = xLoc;
                pieceLocation[1] = yLoc;
                moveCount++;
                
            } else if (xLoc == 3 && yLoc == 1 && moveCount == 0 && QSideR.getMoveCount() == 0) {
                
                KSideR.move(4, 1, enemyPieces, alliedPieces);
                pieceLocation[0] = xLoc;
                pieceLocation[1] = yLoc;
                moveCount++;
                
            }
        } else {
            if (xLoc == 7 && yLoc == 8 && moveCount == 0 && KSideR.getMoveCount() == 0) {
                
                KSideR.move(6, 8, enemyPieces, alliedPieces);
                pieceLocation[0] = xLoc;
                pieceLocation[1] = yLoc;
                moveCount++;
                return true;
                
            } else if (xLoc == 3 && yLoc == 8 && moveCount == 0 && QSideR.getMoveCount() == 0) {
                
                KSideR.move(4, 8, enemyPieces, alliedPieces);
                pieceLocation[0] = xLoc;
                pieceLocation[1] = yLoc;
                moveCount++;
                return true;
            }
        }

        int xdiff = xLoc - pieceLocation[0];
        int ydiff = yLoc - pieceLocation[1];

        if ((Math.abs(xdiff) + Math.abs(ydiff) <= 2) && Math.abs(xdiff) < 2 && Math.abs(ydiff) < 2) {

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

        if (pieceColour == 1) {
            if (xLoc == 7 && yLoc == 1 && !alliedPieceThere(6, 1, alliedPieces)
                    && enemyPieceThere(xLoc, yLoc, enemyPieces) == -1 && !inCheck && moveCount == 0 && KSideR.getMoveCount() == 0) {
                return true;
            } else if (xLoc == 3 && yLoc == 1 && !alliedPieceThere(2, 1, alliedPieces) && !alliedPieceThere(4, 1, alliedPieces)
                    && enemyPieceThere(xLoc, yLoc, enemyPieces) == -1 && enemyPieceThere(2, yLoc, enemyPieces) == -1
                    && enemyPieceThere(4, yLoc, enemyPieces) == -1 && !inCheck && moveCount == 0 && QSideR.getMoveCount() == 0) {
                return true;
            }
        } else {
            if (xLoc == 7 && yLoc == 8 && !alliedPieceThere(6, 8, alliedPieces)
                    && enemyPieceThere(xLoc, yLoc, enemyPieces) == -1 && !inCheck && moveCount == 0 && KSideR.getMoveCount() == 0) {
                return true;
            } else if (xLoc == 3 && yLoc == 8 && !alliedPieceThere(2, 8, alliedPieces) && !alliedPieceThere(4, 8, alliedPieces)
                    && enemyPieceThere(xLoc, yLoc, enemyPieces) == -1 && enemyPieceThere(2, yLoc, enemyPieces) == -1
                    && enemyPieceThere(4, yLoc, enemyPieces) == -1 && !inCheck && moveCount == 0 && QSideR.getMoveCount() == 0) {
                return true;
            }
        }

        int xdiff = xLoc - pieceLocation[0];
        int ydiff = yLoc - pieceLocation[1];

        if ((Math.abs(xdiff) + Math.abs(ydiff) <= 2) && Math.abs(xdiff) < 2 && Math.abs(ydiff) < 2) {

            return true;
        }

        return false;
    }
}

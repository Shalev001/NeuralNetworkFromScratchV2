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
        
        double[] contents = {0,1,2,3,4,5,6,7};
        
        Vector test = new Vector(contents);
        
        System.out.println(Vector.merge(test.split(8)).toString());
        ChessBoard cb = new ChessBoard();
        
        System.out.println(cb.getLegalMoves().size());
        
    }
    
}

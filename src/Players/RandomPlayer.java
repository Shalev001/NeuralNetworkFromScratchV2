/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Players;

import chess.ChessBoard;

/**
 *
 * @author shale
 */
public class RandomPlayer implements Player{
    
    public void takeNextTurn(ChessBoard cb){
        
        cb.takeRandomMove();
        
    }
    
}

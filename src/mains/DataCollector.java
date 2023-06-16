/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mains;

import activationFunctions.Function;
import activationFunctions.ReLU;
import chess.ChessBoard;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import neuralnetwork.Network;

/**
 *
 * @author shale
 */
public class DataCollector {// this code is intended to generate training data

    public static void main(String[] args) {

        for (int i = 1; i <= 1000; i++) {

            System.out.println(i);

            String address = "C:\\Users\\shale\\OneDrive\\Desktop\\neuralNetworks\\chessBots\\RandomGameData\\game" + i + ".txt";

            File location = new File(address);

            PrintWriter writer;
            try {
                writer = new PrintWriter(
                        new BufferedWriter(
                                new FileWriter(location)));

                StringBuilder history = new StringBuilder();

                ChessBoard cb = new ChessBoard();

                while (!cb.checkMate() && !cb.staleMate()) {
                    cb.takeRandomMove();
                    history.append(cb.getBoardState()).append("\n");
                }

                int winner = (cb.staleMate()) ? 0 : ((cb.getTurn() == 0) ? 1 : -1);// stalemate = 0, white win = 1, black win = -1
                if (winner != 0) {
                    writer.println(winner);
                    writer.println(history);
                    writer.flush();
                    writer.close();
                } else {
                    i--;
                }
            } catch (IOException ex) {
                Logger.getLogger(DataCollector.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}

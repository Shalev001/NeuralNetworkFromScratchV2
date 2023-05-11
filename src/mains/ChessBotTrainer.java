/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package mains;

import MatrixVector.*;
import activationFunctions.Function;
import activationFunctions.ReLU;
import chess.ChessBoard;
import java.util.ArrayList;
import java.util.Arrays;
import neuralnetwork.Network;

/**
 *
 * @author shale
 */
public class ChessBotTrainer {
    
    public static void main(String[] args) {
        
        int numOWeights = 10;
        int numOBiases = 10;
        
        Function actiFunc = new ReLU();
        
        //8192
        int[] networkInfo = {65 , 8192, 4096, 4096};
        
        Network nnet1 = new Network(networkInfo);
        ArrayList<Vector[]> nnet1TurnHistory = new ArrayList<>();
        ArrayList<Integer> nnet1ChoiceHistory = new ArrayList<>();       
        
        nnet1.InitializeWeightsAsIdentities();
        nnet1.InitializeRandomBiases();
        
        Network nnet2 = new Network(networkInfo);
        ArrayList<Vector[]> nnet2TurnHistory = new ArrayList<>();
        ArrayList<Integer> nnet2ChoiceHistory = new ArrayList<>();
        
        nnet2.InitializeWeightsAsIdentities();
        nnet2.InitializeRandomBiases();
        
        ChessBoard cb = new ChessBoard();
        
        System.out.println("all structures initialized!");
        
        int rep = 0;
        
        while(!cb.checkMate(1) && !cb.checkMate(0)){
            
            rep++;
            
            boolean legalMove = true;
           
            Network currentPlayer;
            Network otherPlayer;
            ArrayList<Vector[]> currentTurnHistory;
            ArrayList<Integer> currentChoiceHistory;
            ArrayList<Vector[]> otherTurnHistory;
            ArrayList<Integer> otherChoiceHistory;
            
            //setting the appropriate network as the player for the turn
            if(cb.getTurn() == 0){
                currentPlayer = nnet2;//this also needs the turn history of each network
                otherPlayer = nnet1;
                currentTurnHistory = nnet2TurnHistory;
                currentChoiceHistory = nnet2ChoiceHistory;
                otherTurnHistory = nnet1TurnHistory;
                otherChoiceHistory = nnet1ChoiceHistory;
            }else{
                currentPlayer = nnet1;
                otherPlayer = nnet2;
                currentTurnHistory = nnet1TurnHistory;
                currentChoiceHistory = nnet1ChoiceHistory;
                otherTurnHistory = nnet2TurnHistory;
                otherChoiceHistory = nnet2ChoiceHistory;
            }
            
            int preTurnEnemyValue = cb.getEnemyPointTotal();
            
            Vector input = cb.toNNetInput(cb.getTurn());
            
            currentPlayer.setInput(input);
            
            //computing the networks choice of move
            currentPlayer.compute(actiFunc);
            
            
            Vector output = currentPlayer.getOutput();
            int choice = outputToChoice(output);
            
            int[] coordinates = ChessBoard.indexToCoordinates(choice);
            
            System.out.println("(" + (coordinates[0]+1) + "," + (coordinates[1]+1) + ") --> (" + (coordinates[2]+1) + "," + (coordinates[3]+1) + ")");
            
            //taking the move specified by the network
            if (!cb.takeNextTurn(coordinates[0]+1, coordinates[1]+1, coordinates[2]+1, coordinates[3]+1)){
                legalMove = false;
            }else{
                System.out.println(cb.toString());
            }
            
            //if the network chose an ilegal move it will be trained until it does not
            double reward;
            
            if (!legalMove){
                
                System.out.println(rep);
                
                
                reward = -10;
                               
                // making the chosen move 10% less likly given the same context
                //System.out.println(output[choice]);
                double[] temp = output.getContents().clone();
                temp[choice] *= (1 + reward/100);
                //System.out.println(currentPlayer.getOutput()[choice]);
                                
                double originalLoss = currentPlayer.cost(currentPlayer.getOutput(), new Vector(temp));
                
                //System.out.println("##########");
                

                currentPlayer.batchGradientDiscent(output,0.00000000001, actiFunc);
                
                currentPlayer.compute(actiFunc);
                
                //System.out.println(currentPlayer.getOutput()[choice]);
                
                double newLoss = currentPlayer.cost(currentPlayer.getOutput(), output);
                
                
                //System.out.println(currentPlayer.getOutput()[choice]);
                System.out.println(originalLoss + " -> " + newLoss);
                System.out.println("##########");
                
            }else{//in the proccess of recording turn history and training the neural network acording to rewards
                reward = preTurnEnemyValue - cb.getEnemyPointTotal();
                Vector[] turn = new Vector[2];
                turn[0] = input;
                turn[1] = output;
                currentTurnHistory.add(turn);
                currentChoiceHistory.add(choice);
                if (reward != 0){//training both networks based on the reward
                    adjustWeights(currentPlayer,currentTurnHistory,currentChoiceHistory,reward,numOWeights,numOBiases,actiFunc);
                    adjustWeights(otherPlayer,otherTurnHistory,otherChoiceHistory,-reward,numOWeights,numOBiases,actiFunc);
                }
            }
            
        }
        if (cb.checkMate(0)){
            System.out.println("white wins!");
            adjustWeights(nnet1,nnet1TurnHistory,nnet1ChoiceHistory,50,numOWeights,numOBiases,actiFunc);
            adjustWeights(nnet2,nnet2TurnHistory,nnet2ChoiceHistory,-50,numOWeights,numOBiases,actiFunc);
        }else if (cb.checkMate(1)){
            System.out.println("black wins!");
            adjustWeights(nnet2,nnet2TurnHistory,nnet2ChoiceHistory,50,numOWeights,numOBiases,actiFunc);
            adjustWeights(nnet1,nnet1TurnHistory,nnet1ChoiceHistory,-50,numOWeights,numOBiases,actiFunc);
        }
        
    }
    
    /**
     * a method to sample the output to make a choice
     * @param output the output given by the neural network
     * @return
     */
    public static int outputToChoice(Vector output){
        
        double sum = 0;
        double rand = Math.random();
        
        double[] temp = output.getContents().clone();
        
        for (int i = 0; i < output.getDimension(); i++) {
            sum += temp[i];
        }
        for (int i = 0; i < output.getDimension(); i++) {
            temp[i] /= sum; 
        }
        
        for (int i = 0; i < output.getDimension(); i++) {
            rand -= temp[i];
            if (rand < 0){
                return i;
            }
        }
        
        return output.getDimension() - 1;
    }
    
    public static double quadraticCurveMaxOne(int x, int num){
        
        return (x*x)/(num*num);
        
    }
    
    public static void adjustWeights(Network nnet, ArrayList<Vector[]> turnHistory, ArrayList<Integer> choiceHistory,
            double reward,int numOWeights,int numOBiases, Function actiFunc){
        
        for (int i = 0; i < choiceHistory.size(); i++) {
            Vector[] turn = turnHistory.get(i);
            int choice = choiceHistory.get(i);
            turn[1].setValue(choice, turn[1].getValue(choice) * (1 + reward/100));
            nnet.setInput(turn[0]);
            nnet.batchGradientDiscent(turn[1],quadraticCurveMaxOne(i,choiceHistory.size()), actiFunc);
        }
        
    }
}

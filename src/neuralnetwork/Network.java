package neuralnetwork;

import MatrixVector.*;
import activationFunctions.*;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.util.InputMismatchException;

/**
 * this network follows the format and techniques outlined in the following:
 * http://neuralnetworksanddeeplearning.com/chap2.html
 *
 * @author shale
 */
public class Network {

    int[] networkInfo;
    Vector[] Z; // Z = Z^l
    Vector[] values;
    Vector[] biases;
    Vector[] errors;
    Matrix[] weights;

    public Network(int[] networkInfo) {

        this.networkInfo = networkInfo;
        values = new Vector[networkInfo.length];
        Z = new Vector[networkInfo.length - 1];
        biases = new Vector[networkInfo.length - 1];//there is no reson for the first layer to have biases since it does not compute anything
        weights = new Matrix[networkInfo.length - 1];//same as above but last layer
        errors = new Vector[networkInfo.length - 1];

        for (int i = 0; i < networkInfo.length; i++) {
            values[i] = new Vector(networkInfo[i]);
        }
        for (int i = 0; i < networkInfo.length - 1; i++) {
            Z[i] = new Vector(networkInfo[i + 1]);
            biases[i] = new Vector(networkInfo[i + 1]);
            errors[i] = new Vector(networkInfo[i + 1]);
            weights[i] = new Matrix(networkInfo[i + 1], networkInfo[i]);
        }
    }

    private void setBias(int l, int j, double val) {
        biases[l].setValue(j, val);
    }

    private void setweight(int l, int j, int k, double val) {
        weights[l].setVal(j, k, val);
    }

    public void setInput(Vector input) throws VectorDimensionsDoNotMatchException {

        if (input.getDimension() != networkInfo[0]) {
            throw new VectorDimensionsDoNotMatchException();
        }

        values[0] = input;

    }

    public void InitializeWeightsAsIdentities() {
        for (var weight : weights) {
            weight.InitializeAsIdentity();
        }
    }

    public void InitializeRandomBiases() {
        for (var bias : biases) {
            for (int i = 0; i < bias.getDimension(); i++) {
                bias.setValue(i, Math.random());
            }
        }
    }

    public Vector getOutput() {
        return values[networkInfo.length - 1];
    }

    public void compute(Function actiFunc) {

        for (int i = 1; i < networkInfo.length; i++) {//not calculating the values for the first layer
            Z[i - 1] = weights[i - 1].multiply(values[i - 1]).add(biases[i - 1]);
            values[i] = Z[i - 1].applyFunction(actiFunc);
        }

    }

    public void backPropogate(Function actiFunc, Vector real, Vector expected) {

        //formula 1
        errors[networkInfo.length - 2] = delta_aCost(real, expected).HadamardProduct(Z[networkInfo.length - 2].applyDir(actiFunc));

        for (int i = networkInfo.length - 3; i > 0; i--) {

            //formula 2
            errors[i] = weights[i + 1].multiplyTranspose(errors[i + 1]).HadamardProduct(Z[i].applyDir(actiFunc));

        }
    }

    public double findWeightSlope(int l, int j, int k) {
        //formula 4
        return values[l].getValue(k) * errors[l].getValue(j);
    }

    public Vector findBiasSlope(int l) {
        //formula 3
        return errors[l];
    }

    public Vector delta_aCost(Vector real, Vector expected) {
        return real.subtract(expected);
    }

    public double cost(Vector expected, Vector real) {
        double val = real.subtract(expected).magnitude();
        return (val * val) / 2;
    }

    public double averageCost(Vector[] expected, Vector[] real) throws VectorDimensionsDoNotMatchException {

        if (expected.length != real.length) {
            throw new VectorDimensionsDoNotMatchException();
        }

        double sum = 0;

        for (int i = 0; i < expected.length; i++) {

            double val = real[i].subtract(expected[i]).magnitude();
            sum += (val * val) / 2;

        }

        return sum / expected.length;

    }

    public void batchGradientDiscent(Vector expected, double stepSize, Function actiFunc) { // only weights are being changed right now should be modified to change biases as well

        //System.out.println(cost(getOutput(),expected));
        for (int l = 0; l < networkInfo.length - 1; l++) {//for every layer
            for (int j = 0; j < networkInfo[l + 1]; j++) {//for every weight vector
                for (int k = 0; k < networkInfo[l]; k++) {//for every weight in the vector

                    compute(actiFunc);

                    backPropogate(actiFunc, getOutput(), expected);

                    double slope = findWeightSlope(l, j, k);

                    weights[l].setVal(j, k, weights[l].getVal(j, k) - (slope / (slope * slope + 1)) * stepSize);

                }
            }
        }

        for (int l = 0; l < biases.length; l++) {
            for (int j = 0; j < biases[l].getDimension(); j++) {

                compute(actiFunc);

                backPropogate(actiFunc, getOutput(), expected);

                double slope = findBiasSlope(l).getValue(j);

                biases[l].setValue(j, biases[l].getValue(j) - (slope / (slope * slope + 1)) * stepSize);

            }
        }
        //System.out.println(cost(getOutput(),expected));
    }

    public void partialGradientDiscent(Vector expected, int numWChanges, int numBChanges, double stepSize, Function actiFunc) { // only weights are being changed right now should be modified to change biases as well

        int[] rands = new int[numWChanges];

        int totalWeights = 0;

        for (int i = 0; i < networkInfo.length - 1; i++) {
            totalWeights += networkInfo[i] * networkInfo[i + 1];
        }

        for (int i = 0; i < numWChanges; i++) {
            rands[i] = (int) (Math.random() * totalWeights);
        }

        //System.out.println(cost(getOutput(),expected));
        for (int l = 0; l < networkInfo.length - 1; l++) {//for every layer
            for (int j = 0; j < networkInfo[l + 1]; j++) {//for every weight vector
                for (int k = 0; k < networkInfo[l]; k++) {//for every weight in the vector
                    for (int i = 0; i < numWChanges; i++) {
                        if (rands[i] == 0) {

                            compute(actiFunc);

                            backPropogate(actiFunc, getOutput(), expected);

                            double slope = findWeightSlope(l, j, k);

                            weights[l].setVal(j, k, weights[l].getVal(j, k) - (slope / (slope * slope + 1)) * stepSize);

                        }
                        rands[i]--;
                    }
                }
            }
        }

        rands = new int[numBChanges];

        int totalBiases = 0;

        for (int i = 0; i < biases.length; i++) {
            totalBiases += biases[i].getDimension();
        }

        for (int i = 0; i < numBChanges; i++) {
            rands[i] = (int) (Math.random() * totalBiases);
        }

        for (int l = 0; l < biases.length; l++) {
            for (int j = 0; j < biases[l].getDimension(); j++) {
                for (int i = 0; i < numBChanges; i++) {
                    if (rands[i] == 0) {

                        compute(actiFunc);

                        backPropogate(actiFunc, getOutput(), expected);

                        double slope = findBiasSlope(l).getValue(j);

                        biases[l].setValue(j, biases[l].getValue(j) - (slope / (slope * slope + 1)) * stepSize);

                    }
                    rands[i]--;
                }
            }
        }
        //System.out.println(cost(getOutput(),expected));
    }

    public void stocasticGradientDiscent(Vector expected, double learningSpeed, Function actiFunc) { // only weights are being changed right now should be modified to change biases as well

        compute(actiFunc);

        backPropogate(actiFunc, getOutput(), expected);

        for (int l = weights.length - 1; l >= 0; l--) {
            weights[l] = weights[l].subtract(((errors[l].toMatrix()).multiply(values[l].toMatrix().transpose())).multiplyScalar(learningSpeed));
        }

        for (int l = biases.length - 1; l >= 0; l--) {
            Vector temp = biases[l];
            biases[l] = biases[l].subtract(errors[l].multiplyScalar(learningSpeed));
        }
    }

    public void export(File file) throws IOException {
        FileOutputStream fileOutputStream = new FileOutputStream(file);
        ObjectOutputStream writer = new ObjectOutputStream(fileOutputStream);
        writer.writeInt(networkInfo.length);//number of layers
        for (int size : networkInfo) {
            writer.writeInt(size);// the size of each layer
        }
        for (Vector biasvec : biases) {
            for (double bias : biasvec.getContents()) {
                writer.writeDouble(bias);//every bias for every perceptron in the network
            }
        }
        for (Matrix weightMat : weights) {
            for (int i = 0; i < weightMat.getDimensions()[0]; i++) {
                for (int j = 0; j < weightMat.getDimensions()[1]; j++) {
                    writer.writeDouble(weightMat.getVal(i, j));//every weight
                }
            }
        }
        writer.flush();
        writer.close();
    }

    public static Network importf(File file) throws FileNotFoundException, IOException {

        Network output = null;

        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            ObjectInputStream reader = new ObjectInputStream(fileInputStream);

            int numLayers = reader.readInt();// getting the number of layers
            int[] networkInfo = new int[numLayers];

            for (int i = 0; i < numLayers; i++) {
                networkInfo[i] = reader.readInt();//getting the layer sizes and setting the array to match
            }

            output = new Network(networkInfo);

            for (int l = 0; l < networkInfo.length - 1; l++) {
                for (int j = 0; j < networkInfo[l]; j++) {

                    double biasVal = reader.readDouble();//getting all the biases

                    output.setBias(l, j, biasVal);
                }
            }

            for (int l = 0; l < networkInfo.length - 1; l++) {

                for (int j = 0; j < networkInfo[l + 1]; j++) {

                    for (int k = 0; k < networkInfo[l]; k++) {
                        
                        double weightVal = reader.readDouble();

                        output.setweight(l, j, k, weightVal);

                    }
                }
            }

            reader.close();

            return output;

        } catch (InputMismatchException e) {
            System.out.println("file not in the correct formatt");
            e.printStackTrace();
            return output;

        }

    }
}

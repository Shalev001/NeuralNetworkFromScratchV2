package neuralnetwork;

import MatrixVector.*;
import activationFunctions.*;

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
            Z[i] = new Vector(networkInfo[i]);
            biases[i] = new Vector(networkInfo[i + 1]);
            errors[i] = new Vector(networkInfo[i + 1]);
            weights[i] = new Matrix(networkInfo[i + 1], networkInfo[i]);
        }
    }

    public void setInput(Vector input) throws VectorDimensionsDoNotMatchException {

        if (input.getDimension() != networkInfo[0]) {
            throw new VectorDimensionsDoNotMatchException();
        }

        values[0] = input;

    }
    
    public void InitializeWeightsAsIdentities(){
        for (var weight : weights) {
            weight.InitializeAsIdentity();
        }
    }
    
    public void InitializeRandomBiases(){
        for (var bias : biases){
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
            Z[i - 1] = weights[i - 1].multiply(values[i - 1]).add(biases[i-1]);
            values[i] = Z[i - 1].applyFunction(actiFunc);
        }

    }

    public void backPropogate(Function actiFunc, Vector real, Vector expected) {

        //formula 1
        errors[networkInfo.length - 2] = delta_aCost(real, expected).HadamardProduct(Z[networkInfo.length - 2].applyDir(actiFunc));

        for (int i = networkInfo.length - 3; i > 0; i--) {

            errors[i] = weights[i + 1].multiply(errors[i + 1]).HadamardProduct(Z[i].applyDir(actiFunc));

        }
    }

    public double findWeightSlope(int l, int j, int k) {
        return values[l].getValue(k) * errors[l].getValue(j);
    }

    public Vector findBiasSlope(int l) {
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

        compute(actiFunc);
        
        backPropogate(actiFunc,getOutput(),expected);

        //System.out.println(cost(getOutput(),expected));
        
        for (int l = 0; l < networkInfo.length - 1; l++) {//for every layer
            for (int j = 0; j < networkInfo[l + 1]; j++) {//for every weight vector
                for (int k = 0; k < networkInfo[l]; k++) {//for every weight in the vector

                    weights[l].setVal(j, k, weights[l].getVal(j, k) -findWeightSlope(l,j,k) * stepSize);
                    
                }
            }
        }

        for (int l = 0; l < biases.length; l++) {
            for (int j = 0; j < biases[l].getDimension(); j++) {
                biases[l].setValue(j,biases[l].getValue(j) -findBiasSlope(l).getValue(j) * stepSize);
            }
        }
        //System.out.println(cost(getOutput(),expected));
    }
}

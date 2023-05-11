package MatrixVector;

import activationFunctions.*;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
/**
 *
 * @author shale
 */
public class Vector {

    private int dimension;
    private double[] contents;

    public Vector(double[] contents) {
        this.contents = contents;
        dimension = contents.length;
    }
    
    public Vector(int dimension){
        this.dimension = dimension;
        contents = new double[dimension];
    }

    public int getDimension() { // no setter for dimension since it is dependent on the contents
        return dimension;
    }

    public double[] getContents() {
        return contents;
    }

    public void setContents(double[] contents) {
        this.contents = contents;
        dimension = contents.length;
    }

    public void setValue(int index, double value) {
        contents[index] = value;
    }

    public double getValue(int index) {
        return contents[index];
    }

    public double dotProduct(Vector vector) throws VectorDimensionsDoNotMatchException {

        double result = 0;
        double[] vectorContents = vector.getContents();

        if (dimension != vector.getDimension()) {
            throw new VectorDimensionsDoNotMatchException();
        }
        for (int i = 0; i < dimension; i++) {
            result += contents[i] * vectorContents[i];
        }

        return result;
    }

    public static double dotProduct(Vector vector1, Vector vector2) throws VectorDimensionsDoNotMatchException {

        double result = 0;
        double[] vector1Contents = vector1.getContents();
        double[] vector2Contents = vector2.getContents();

        if (vector1.getDimension() != vector2.getDimension()) {
            throw new VectorDimensionsDoNotMatchException();
        }
        for (int i = 0; i < vector1.getDimension(); i++) {
            result += vector1Contents[i] * vector2Contents[i];
        }

        return result;
    }

    /**
     * an implementation of the Hadamard product (element-wise multiplication) 
     * @param vec
     * @return 
     */
    public Vector HadamardProduct(Vector vec){
        
        if (dimension != vec.getDimension()) {
            throw new VectorDimensionsDoNotMatchException();
        }
        
        Vector out = new Vector(dimension);
        
        for (int i = 0; i < dimension; i++) {
            out.setValue(i, contents[i] * vec.getValue(i));
        }
        
        return out;
    }
    
    public static double magnitude(Vector vector) {

        double squaresum = 0;

        for (double num : vector.getContents()) {
            squaresum += num * num;
        }

        return Math.sqrt(squaresum);

    }

    public double magnitude() {

        double squaresum = 0;

        for (double num : contents) {
            squaresum += num * num;
        }

        return Math.sqrt(squaresum);

    }

    public Vector add(Vector vector) {

        if (dimension != vector.getDimension()) {
            throw new VectorDimensionsDoNotMatchException();
        }
        
        double[] vectorContents = vector.getContents();
        double[] out = contents.clone();

        for (int i = 0; i < dimension; i++) {
            out[i] += vectorContents[i];
        }
        
        return new Vector(out);

    }

    public static Vector add(Vector vector1, Vector vector2) {

        double[] vector1Contents = vector1.getContents();
        double[] vector2Contents = vector2.getContents();
        double[] result = new double[vector1.getDimension()];

        if (vector1.getDimension() != vector2.getDimension()) {
            throw new VectorDimensionsDoNotMatchException();
        }

        for (int i = 0; i < vector1.getDimension(); i++) {
            result[i] = vector1Contents[i] + vector2Contents[i];
        }

        return new Vector(result);

    }

    public Vector subtract(Vector vector) {
        
        if (dimension != vector.getDimension()) {
            throw new VectorDimensionsDoNotMatchException();
        }
        
        double[] vectorContents = vector.getContents();
        double[] out = contents.clone();
        
        for (int i = 0; i < dimension; i++) {
            out[i] -= vectorContents[i];
        }

        return new Vector(out);
    }

    public static Vector subtract(Vector vector1, Vector vector2) {

        double[] vector1Contents = vector1.getContents();
        double[] vector2Contents = vector2.getContents();
        double[] result = new double[vector1.getDimension()];

        if (vector1.getDimension() != vector2.getDimension()) {
            throw new VectorDimensionsDoNotMatchException();
        }

        for (int i = 0; i < vector1.getDimension(); i++) {
            if (vector1Contents[i] - vector2Contents[i] != 0) {
                result[i] = vector1Contents[i] - vector2Contents[i];
            }
        }

        return new Vector(result);

    }
    
    public Vector applyFunction(Function func){
        
        double[] out = contents.clone();
        
        for (double entry : out) {
            entry = func.compute(entry);
        }
        
        return new Vector(out);
    }
    
    public Vector applyDir(Function func){
        
        double[] out = contents.clone();
        
        for (double entry : out) {
            entry = func.computeDir(entry);
        }
        
        return new Vector(out);
    }
    
    public Matrix toMatrix(){
        
        Matrix out = new Matrix(dimension,1);
        
        for (int i = 0; i < dimension; i++) {
            out.setVal(i, 0, contents[i]);
        }
        
        return out;
        
    }

    public String toString() {
        String str = "[";

        for (double num : contents) {
            str += num + ",";
        }

        str += "]";

        return str;
    }

}

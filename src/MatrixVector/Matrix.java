/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package MatrixVector;

/**
 *
 * @author shale
 */
public class Matrix {

    private double[][] contents;
    private int[] dimensions;
    boolean isTransposed;

    public Matrix(double[][] contents) {
        this.contents = contents;
        dimensions = new int[2];
        dimensions[0] = contents.length;
        dimensions[1] = contents[0].length;
        isTransposed = false;
    }

    public Matrix(int[] dimensions) {
        this.dimensions = dimensions;
        contents = new double[dimensions[0]][dimensions[1]];
    }

    public Matrix(int dimension1, int dimension2) {
        int[] temp = {dimension1, dimension2};
        dimensions = temp;
        contents = new double[dimension1][dimension2];
    }

    public double[][] getContents() {
        return contents;
    }

    public double getVal(int index1, int index2) {
        return contents[index1][index2];
    }

    public void setContents(double[][] contents) {
        this.contents = contents;
    }

    public void setVal(int index1, int index2, double val) {
        contents[index1][index2] = val;
    }

    public int[] getDimensions() {
        return dimensions;
    }

    public void setDimensions(int[] dimensions) {
        this.dimensions = dimensions;
    }

    public void InitializeAsIdentity() {

        int dimMin = (dimensions[0] < dimensions[1]) ? dimensions[0] : dimensions[1];

        for (int i = 0; i < dimMin; i++) {
            contents[i][i] = 1;
        }

    }

    public void InitializeAsRandom(double min, double max) {

        for (int i = 0; i < dimensions[0]; i++) {
            for (int j = 0; j < dimensions[1]; j++) {
                contents[i][j] = (Math.random() * (max - min)) + min;
            }
        }

    }

    public Matrix multiply(Matrix mat) throws MatrixDimensionsDoNotMatchException {

        if (dimensions[1] != mat.getDimensions()[0]) {
            throw new MatrixDimensionsDoNotMatchException();
        }

        Matrix out = new Matrix(dimensions[0], mat.getDimensions()[1]);

        for (int i = 0; i < dimensions[0]; i++) {
            for (int j = 0; j < mat.getDimensions()[1]; j++) {

                double sum = 0;

                for (int k = 0; k < dimensions[1]; k++) {

                    sum += contents[i][k] * mat.getVal(k, j);

                }

                out.setVal(i, j, sum);

            }
        }

        return out;

    }

    public Vector multiply(Vector vec) throws MatrixDimensionsDoNotMatchException {

        if (dimensions[1] != vec.getDimension()) {
            throw new MatrixDimensionsDoNotMatchException();
        }

        Vector out = new Vector(dimensions[0]);

        for (int i = 0; i < dimensions[0]; i++) {

            double sum = 0;

            for (int j = 0; j < dimensions[1]; j++) {
                sum += contents[i][j] * vec.getValue(j);
            }
            
            out.setValue(i, sum);
            
        }
        
        return out;

    }
    public Vector multiplyTranspose(Vector vec) throws MatrixDimensionsDoNotMatchException {

        if (dimensions[0] != vec.getDimension()) {
            throw new MatrixDimensionsDoNotMatchException();
        }

        Vector out = new Vector(dimensions[1]);

        for (int i = 0; i < dimensions[1]; i++) {

            double sum = 0;

            for (int j = 0; j < dimensions[0]; j++) {
                sum += contents[j][i] * vec.getValue(j);
            }
            
            out.setValue(i, sum);
            
        }
        
        return out;

    }

    public Matrix transpose() {

        Matrix out = new Matrix(dimensions[1], dimensions[0]);

        for (int i = 0; i < dimensions[1]; i++) {
            for (int j = 0; j < dimensions[0]; j++) {

                out.setVal(i, j, contents[j][i]);

            }
        }

        return out;

    }
    
    
    public Vector toVector() throws MatrixDimensionsDoNotMatchException {

        if (dimensions[1] != 1) {
            throw new MatrixDimensionsDoNotMatchException();
        }

        double[] vContents = new double[dimensions[0]];

        for (int i = 0; i < dimensions[0]; i++) {
            vContents[i] = contents[i][0];
        }

        return new Vector(vContents);
    }

    public String toString() {

        String str = "";

        for (int i = 0; i < dimensions[0]; i++) {
            for (int j = 0; j < dimensions[1]; j++) {
                str += contents[i][j] + " ";
            }
            str += "\n\n";
        }

        return str;
    }
}

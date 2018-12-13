package cs410.matrix;

public interface Matrix {

    public Matrix subtract(Matrix o);

    public Matrix add(Matrix o);

    public Matrix crossProduct(Matrix o);

    public Matrix makeUnitLength();

    public String toString();

    public Matrix times(Matrix o);

    public Matrix multiplyScalar(double scalar);

    public Matrix homogeneous(); 

    public double dotProduct(Matrix o);

    public void set(int i, int j, double value);

    public double get(int i, int j);

    public Matrix copy();

    public Matrix transpose();

    public double[][] getArray();

    public Matrix unHomogeneous();

    public Vector getColumn(int i);

    public double getDeterminant();

    public int columns();

    public int rows(); 
}

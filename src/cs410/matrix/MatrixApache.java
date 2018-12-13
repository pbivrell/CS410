package cs410.matrix;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.LUDecomposition;
import org.apache.commons.math3.linear.RealMatrix;

public class MatrixApache implements Matrix {

    public Array2DRowRealMatrix raw;

    public String toString(){
        double[][] raw =  this.getArray();
        String res = "";
        res += this.rows() + "," + this.columns() + "\n";
        for(double[] col: raw){
            for(double item: col){
                res += String.format("%.3f ", item);
            }
            res += "\n";
        }
        return res;
    }

    public static MatrixApache identity(int n) {
        double[][] d = new double[n][n];
        for(int i = 0; i < n; i++){
            for(int j = 0; j < n; j++){
                d[i][j] = (i == j) ? 1 : 0;
            }
        }
		return new MatrixApache(d);
	}


    public MatrixApache(double[] d){
        this.raw = new Array2DRowRealMatrix(d);
    }

    public MatrixApache(Vector v){
        this.raw = new Array2DRowRealMatrix(v.raw);
    }

	public MatrixApache(double[][] d) {
		this.raw = new Array2DRowRealMatrix(d);
	}

    public MatrixApache(RealMatrix m){
        this.raw = (Array2DRowRealMatrix) m.copy();
    }

    public MatrixApache(Array2DRowRealMatrix m) {
        this.raw = (Array2DRowRealMatrix) m.copy();
    }
    
    public MatrixApache times(Matrix o){
        assert o instanceof MatrixApache;
        MatrixApache m = (MatrixApache) o;
        return new MatrixApache(this.raw.multiply(m.raw));
    }

    public MatrixApache times(Vector v){
        return new MatrixApache(this.raw.multiply(new MatrixApache(v).raw));
    }

    public double[][] getArray(){
        return this.raw.getData();
    }

	@Override
	public MatrixApache subtract(Matrix o) {
		assert o instanceof MatrixApache;
        MatrixApache m = (MatrixApache) o;
        return new MatrixApache((Array2DRowRealMatrix)this.raw.add((m.raw).scalarMultiply(-1)));
	}

	@Override
	public MatrixApache add(Matrix o) {
	    assert o instanceof MatrixApache;
        MatrixApache m = (MatrixApache) o;
        
        return new MatrixApache(this.raw.add(m.raw));
	}

	@Override
	public MatrixApache crossProduct(Matrix o) {
		assert o instanceof MatrixApache;
        MatrixApache m = (MatrixApache) o;
        double u1, u2, u3, v1, v2, v3;
        u1 = this.raw.getEntry(0,0);
        u2 = this.raw.getEntry(0,1);
        u3 = this.raw.getEntry(0,2);
        v1 = m.raw.getEntry(0,0);
        v2 = m.raw.getEntry(0,1);
        v3 = m.raw.getEntry(0,2);

        double uvi, uvj, uvk;
        uvi = u2 * v3 - v2 * u3;
        uvj = v1 * u3 - u1 * v3;
        uvk = u1 * v2 - v1 * u2;
        return new MatrixApache(new double[] { uvi, uvj, uvk });
	}

	@Override
	public MatrixApache makeUnitLength() {
		double norm = this.raw.getNorm();
        if(norm == 0){
            return this.copy();
        }
        return this.multiplyScalar(1/norm);
	}

	@Override
	public MatrixApache multiplyScalar(double scalar) {
        return new MatrixApache(this.raw.scalarMultiply(scalar));
    }

	@Override
	public MatrixApache homogeneous() {
		Array2DRowRealMatrix m = new Array2DRowRealMatrix(4,4);
        m.setSubMatrix(this.raw.getData(), 0,0);
        m.setEntry(3,0,0); m.setEntry(3,1,0); m.setEntry(3,2,0); m.setEntry(3,3,1);
        return new MatrixApache(m);
	}

	@Override
	public void set(int i, int j, double value) {
		this.raw.setEntry(i,j, value);
	}

	@Override
	public double get(int i, int j) {
	    return this.raw.getEntry(i,j);
    }

    public Vector getColumn(int i){
        return new Vector(this.raw.getColumnMatrix(i).transpose().getData()[0]);
    }

    public double getDeterminant() {
        return new LUDecomposition(this.raw).getDeterminant();
    }


	@Override
	public MatrixApache copy() {
	    return new MatrixApache(this.raw);
    }

	@Override
	public MatrixApache transpose() {
	    return new MatrixApache(this.raw.transpose());
    }

    public MatrixApache unHomogeneous(){
        return new MatrixApache(this.raw.getSubMatrix(0, this.rows() -2,0, this.columns()-1));
    }

	@Override
	public int columns() {
		return this.raw.getColumnDimension();
	}

    public double dotProduct(Matrix o){
        assert o instanceof MatrixApache;
        MatrixApache m = (MatrixApache) o;
        double result = 0;
        double[][] this_col = this.getArray();
        double[][] o_col = m.getArray();
        for(int i = 0; i < this_col[0].length; i++){
            result += this_col[0][i] * o_col[0][i];
        }
        return result;
    }
	
    @Override
	public int rows() {
		return this.raw.getRowDimension();
	}

    public static void main(String[] args){
        Matrix m1 = new MatrixApache( new double[] { 2 ,3, 5 });
        Matrix m2 = new MatrixApache( new double[] { 8, 2, 1});
        System.out.println(m1.dotProduct(m2));
        return;
        /*
        System.out.println("Matrix Apache Test");
        System.out.println("Identity");
        System.out.println(MatrixApache.identity(4));
        System.out.println("Vector");
        System.out.println(new MatrixApache(new double[]{ 5.23, 9, -2.3}));
        System.out.println("Matrix");
	    System.out.println(new MatrixApache(new double[][] {{1,2,3},{0.01, 0.02, 0.03}, {591820, 591, 10}}));
        System.out.println("M1");
	    Matrix m1 = MatrixApache.identity(3);
        System.out.println(m1);
        System.out.println("M2");
        Matrix m2 = MatrixApache.identity(3).multiplyScalar(3);
        System.out.println(m2);
        System.out.println("M2 - M1");
        System.out.println(m2.subtract(m1));
        System.out.println("M1 + M1");
	    System.out.println(m1.add(m1));
        System.out.println("V1");
        Matrix v1 = new MatrixApache(new double[] {1,2,3});
        System.out.println(v1);
        System.out.println("V2");
        Matrix v2 = new MatrixApache(new double[] {4,5,6});
        System.out.println(v2);
	    System.out.println("V1 x V2");
        System.out.println(v1.crossProduct(v2));
        System.out.println("Unit Length V1");
        System.out.println(v1.makeUnitLength());
        System.out.println("M2 Homogenoeus");
        System.out.println(m2.homogeneous());
        System.out.println("COPY M2");
        System.out.println(m2.copy());
        System.out.println("Transpose M2");
        //System.out.println(m2.transpose());
        System.out.println("Rows");
        System.out.println(m2.rows());
        System.out.println("Cols");
        System.out.println(m2.columns());
        //Matrix A = new MatrixApache(double[][] { {3, 0, 0, 10}, {0, 3, 0, 0}, {0,0,3,10}, {0,0,0,1});
        //Matrix B = new MatrixApache(double[][] { {0.866, 0, 0.5, 0}, {0, 1, 0, 0}, {-0.5,0,0.866,0}, {1,1,1,1});
        */
    }
}

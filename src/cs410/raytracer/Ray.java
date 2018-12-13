package cs410.raytracer;

import cs410.matrix.Vector;

public class Ray{

    private Vector point;
    private Vector direction;
    private int row;
    private int col;

    public Ray(Vector point, Vector direction, int row, int col){
        this.point = point;
        this.direction = direction;
        this.row = row;
        this.col = col;
    }

    public Vector l(){
        return point;
    }

    public Vector d(){
        return direction;
    }

    public int getRow(){
        return this.row;
    }

    public int getCol(){
        return this.col;
    }

    public String toString(){
        return "Ray ("+ row+", " + col + ")\nPoint:\n" + point + "\nDirection:\n" + direction;
    }
}

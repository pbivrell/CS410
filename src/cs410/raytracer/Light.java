package cs410.raytracer;

import cs410.matrix.Matrix;
import cs410.matrix.MatrixApache;
import cs410.matrix.Vector;

public class Light {

    public final Vector position;
    public final double w;
    public final Vector brightness;

    public Light(Vector position, double w, Vector brightness){
        this.position = position;
        this.w = w;
        this.brightness = brightness;
    }

    public Light(double[] parts){
        this(new Vector(parts[0], parts[1], parts[2]), parts[3], new Vector(parts[4], parts[5], parts[6]));
    }
}

package cs410.raytracer;

import cs410.matrix.Matrix;
import cs410.matrix.Vector;
import cs410.matrix.MatrixApache;
import cs410.io.Error;

import java.util.ArrayList;


public class Sphere implements Obj {
    
    private Material material;
    private double r;
    private Vector center;

    public Intersection intersect(Ray ray){ 
        //System.out.println("Center:"+ center);
        //System.out.println("Ray start:"+ ray.l());
        //System.out.println("Ray direction:" + ray.d());
        //System.out.println(center);
        //System.out.println(ray.l());
        Vector Tv = center.subtract(ray.l());
        //System.out.println("Base to Center:"+ Tv);
        double v = Tv.dotProduct(ray.d());
        double csq = Tv.dotProduct(Tv);
        double disc = r * r - (csq - (v * v));
        double distance = v - Math.sqrt(disc);
        Vector Q = ray.l().add(ray.d().multiplyScalar(distance));
        Vector normal = Q.subtract(center).makeUnitLength();
        if(disc >  0){
            return new Intersection(material, normal, Q, distance, true);
        }else{
            return new Intersection(null, null, null, 999999, false);
        }
    }

    public Sphere(Material m, double radius, Vector center){
        this.material = m;
        this.r = radius;
        this.center = center;
    }

    public Sphere(double[] parts){
        if(parts.length != 13){
            Error.fatal("Creating sphere with invalid arguments expected [%d] got [%d].", 13, parts.length);
        }
        center = new Vector(parts[0], parts[1], parts[2]);
        r = parts[3];
        material = new Material(parts);
    }
}

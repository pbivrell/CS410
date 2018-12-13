package cs410.raytracer;

import cs410.debugging.Debug;
import cs410.matrix.Vector;
import cs410.matrix.Matrix;
import cs410.matrix.MatrixApache;
import cs410.io.LineReader;
import cs410.io.Error;

import java.lang.Double;
import java.lang.Math;
import java.lang.NumberFormatException;

import java.util.ArrayList;
import java.util.Arrays;


public class Model implements Obj {
    private static final boolean DEBUG_ENABLED = true;
    private static final Debug d = new Debug(DEBUG_ENABLED);
    private ArrayList<Face> faces;
    private ArrayList<Material> materials;
    private ArrayList<double[]> raw_vertices;
    private Matrix vertices;
    //private Vector center;
    //private double radius;

    /*public void getCenter(){
        double x = 0;
        double y = 0;
        double z = 0;
        for(double[] d : raw_vertices){
            x += d[0];
            y += d[1];
            z += d[2];
        }
        x /= raw_vertices.size();
        y /= raw_vertices.size();
        z /= raw_vertices.size();
        center = new Vector(x,y,z);
        System.out.println("Center: (" + x + " " + y + " " + z + ")");
        double distance = 0;
        for(double[] d : raw_vertices){
            double t_dis = Math.sqrt(Math.pow(x-d[0],2) + Math.pow(y-d[1],2) + Math.pow(z-d[2],2));
            if(t_dis > distance){
                distance = t_dis;
            }
        }
        radius = 2 * distance;
        System.out.println("Radius:" + radius);
    }*/

    public Intersection intersect(Ray ray){
        double t_min = 99999999;
        boolean hit = false;
        Face hit_face = null; 
        //Sphere general = new Sphere(null, radius, center);
        //if(!general.intersect(ray).hit){
        //    return new Intersection(null, null, null, 999999, false);
        //}
        
        for(Face face : faces){
            if(!face.built()){
                face.buildFace(vertices);
            }
            // Ray triangle intersection
            // http://www.cs.colostate.edu/~cs410/yr2018fa/more_resources/sage/cs410lec10n02.html
            //System.out.println(ray.l());
            Vector DV = ray.d();
            Vector YV = face.a().subtract(ray.l());
            Vector MMab = face.a().subtract(face.b());
            Vector MMac = face.a().subtract(face.c());
            Matrix MM = new MatrixApache(new double[][] { MMab.getArray(), MMac.getArray(), DV.getArray()}).transpose();
            d.print("A:",face.a());
            d.print("B:",face.b());
            d.print("C:",face.c());
            d.print("L:",ray.l());
            d.print("D:",ray.d());
            d.print("A - L:",YV);
            d.print("A - B:",MMab);
            d.print("A - C:",MMac);
            d.print("MM:\n", MM);
            double[] MM0 = MM.getColumn(0).getArray();
            double[] MM1 = MM.getColumn(1).getArray();
            double[] MM2 = MM.getColumn(2).getArray();
            Matrix MMs1 = new MatrixApache(new double[][] { YV.getArray(), MM1, MM2}).transpose();
            Matrix MMs2 = new MatrixApache(new double[][] { MM0, YV.getArray(),MM2}).transpose();
            Matrix MMs3 = new MatrixApache(new double[][] { MM0, MM1, YV.getArray()}).transpose();
            d.print("MM1:", MMs1);
            d.print("MM2:", MMs2);
            d.print("MM3:", MMs3);
            double detM = MM.getDeterminant();
            double detM1 = MMs1.getDeterminant();
            double detM2 = MMs2.getDeterminant();
            double detM3 = MMs3.getDeterminant();
            double beta = detM1/detM;
            double gamma = detM2/detM;
            double t = detM3/detM;
            //System.out.print("beta:" + beta + "gamma:" + gamma + "t:"+ t + "\n");
            if(beta >= 0 && gamma >= 0 && (beta + gamma) <= 1 && t > 0 && t < t_min){
                //System.out.println("Ray Hit " + ray.getRow() + " " + ray.getCol());
                //System.out.println(beta + " " + gamma + " " + t);
                //System.out.println(face);
                hit = true;
                t_min = t;
                hit_face = face;    
            } 
        }
        if(hit){
            //System.err.println("PAUL FIX THIS TO ACTUALLY RETURN POINT ON SURFACE");
            //System.out.println(hit_face);
            Vector normal = (hit_face.b().subtract(hit_face.a())).crossProduct((hit_face.c().subtract(hit_face.a()))).makeUnitLength();
            // Should this be ray.l?
            //System.out.println("N:" + normal);
            if(ray.d().dotProduct(normal) > 0){
                
                normal = normal.multiplyScalar(-1);
            }
            //System.out.println("N2:" + normal);
            return new Intersection(materials.get(hit_face.material()), normal, ray.l().add(ray.d().multiplyScalar(t_min)),t_min, true);
        }else{
            return new Intersection(null, null, null, 999999, false);
        }
    }

    public String toString(){
        String res = "# Blender v2.79 (sub 0) OBJ File\n# Generated by cs410.raytracer.Model (Paul Bivrell)\n";
        double[][] object = vertices.getArray();
        for(int i = 0; i < object[0].length; i++){
            res += String.format("v %.7f %.7f %.7f\n", object[0][i], object[1][i], object[2][i]);
        }
        res += faces;
        return res;
    }

    public Model(double wx, double wy, double wz, int theta, double scale, double tx, double ty, double tz, String file){
        faces = new ArrayList<Face>();
        raw_vertices = new ArrayList<double[]>();
        materials = new ArrayList<Material>();
        //long st = System.currentTimeMillis();
        //modelFile = file.substring(0, file.lastIndexOf("."));
        //d.print("Constructing Model with object file: ", file);
        ReadModelFile(file);
        //long e = System.currentTimeMillis();
        //System.out.println("READ FILE: " +  (e - st));
        //st = System.currentTimeMillis();
        //System.out.println(vertices);
        Matrix tMatrix = TransformationMatrix(wx, wy, wz, theta, scale, tx, ty, tz);
        //e = System.currentTimeMillis();
        //System.out.println("Transformation Time:" + (e - st));
        //:w
        //getCenter();
        raw_vertices = null;
        //d.print("Obj:\n",vertices.toString());
        //d.print("T:\n",tMatrix.toString());
        //System.out.println(vertices);
        //System.out.println(tMatrix);
        
        vertices = tMatrix.times(vertices).unHomogeneous();
        //vertices = vertices.unHomogeneous();
        //System.out.println("Vertices");
        //System.out.println(vertices);
        
        //d.print("RES:\n",vertices.toString());
    }


    public Matrix TransformationMatrix(double wx, double wy, double wz, int theta, double scale, double tx, double ty, double tz){
        Matrix scaleM = ScaleMatrix(scale);
        Matrix transM = TranslationMatrix(tx,ty,tz);
        Matrix rotM = RotationMatrix(wx, wy, wz, theta);
        rotM = rotM.homogeneous();
        d.print("ROT:\n", rotM.toString());
        d.print("Trans:\n",transM.toString());
        d.print("Scale:\n",scaleM.toString());
        return (transM.times(scaleM)).times(rotM);
    }

    public Matrix ScaleMatrix(double scale){
        Matrix scaleM = MatrixApache.identity(4);
        scaleM.set(0,0,scale);
        scaleM.set(1,1,scale);
        scaleM.set(2,2,scale);
        return scaleM;
    }

    public Matrix TranslationMatrix(double tx, double ty, double tz){
        Matrix transM = MatrixApache.identity(4);
        transM.set(0,3, tx);
        transM.set(1,3, ty);
        transM.set(2,3, tz);
        return transM;
    }

    public Matrix RotationMatrix(double wx, double wy, double wz, int theta){
        Vector wV = new Vector(wx, wy, wz);
        //Matrix wV = new MatrixApache(new double[]{wx, wy, wz}).transpose();
        d.print("In:\n", wV.toString());
        wV = wV.makeUnitLength();
        d.print("Z:",wV.toString());
        Vector uV = wV.copy();
        double uV0, uV1, uV2;
        uV0 = Math.abs(uV.get(0)); uV1 = Math.abs(uV.get(1)); uV2 = Math.abs(uV.get(2));
        d.print("COORDS:",uV0, uV1, uV2);

        int min = Math.min(uV0, uV1) == uV1 ? (Math.min(uV1, uV2) == uV2 ? 2 : 1) : (Math.min(uV0,uV2) == uV2 ? 2 :0);
        d.print("Min index:", min);

        uV.set(min, 1.0);
        d.print("X':",uV.toString());
        uV = wV.crossProduct(uV);
        d.print("X'':",uV.toString());
        uV = uV.makeUnitLength();
        d.print("X:",uV.toString());
        Vector vV = wV.crossProduct(uV);
        d.print("Y:",vV.toString());
        double[][] raw_matrix = new double[][] { uV.getArray(), vV.getArray(), wV.getArray()};
        Matrix rotM = new MatrixApache(raw_matrix);
        d.print("ROT:\n", rotM.toString());
        Matrix angleM = MatrixApache.identity(3);
        double cos_a = Math.cos(Math.toRadians(theta));
        double sin_a = Math.sin(Math.toRadians(theta));
        angleM.set(0, 0, cos_a); angleM.set(0,1, -sin_a);
        angleM.set(1, 0, sin_a); angleM.set(1,1, cos_a);
        d.print("AngleM:\n",angleM.toString());
        return (rotM.transpose().times(angleM)).times(rotM);
    }

    public void processVertex(String line){
        String[] parts = line.split(" ");
        if(parts.length != 4){
            Error.fatal("invalid line in file [%s]\n", line);
        }
        double x = 0;
        double y = 0;
        double z = 0;

        try {
            x = Double.parseDouble(parts[1]);
            y = Double.parseDouble(parts[2]);
            z = Double.parseDouble(parts[3]);
        }catch (NumberFormatException e){
            Error.fatal("invalid line in file [%s].\n", line);
        }

        raw_vertices.add(new double[] { x, y, z, 1 });
    }

    public void processFace(String line){
        String[] parts = line.split(" ");
        if(parts.length != 4){
            Error.fatal("invalid line in file [%s]\n", line);
            System.exit(-1);
        }
        int a = 0;
        int b = 0;
        int c = 0;
        int material = 0;

        try {
            a = Integer.parseInt(parts[1].split("//")[0]);
            b = Integer.parseInt(parts[2].split("//")[0]);
            c = Integer.parseInt(parts[3].split("//")[0]);
        }catch (NumberFormatException e){
           Error.fatal("invalid line in file [%s]\n", line);
        }

        faces.add(new Face(a,b,c,material));
    }

    public void processMaterial(String line){
        String[] parts = line.split(" ");
        if(parts.length != 2){
            Error.fatal("invalid line in file [%s]\n", line);
        }
        materials.add(Material.fromFile(parts[1]));
    }

    public void processLines(String line){
        if(line.startsWith("v ")){
            processVertex(line);      
        }else if(line.startsWith("f ")){
            processFace(line);
        }else if(line.startsWith("mtllib ")){
            processMaterial(line);
        }
    }

    public void ReadModelFile(String file){
        try{
            LineReader.read(file, this::processLines);
        }catch (Exception e){
            Error.fatal("could not read model file [%s]: reason [%s]\n",file,e);
        }
        this.vertices = new MatrixApache(raw_vertices.toArray(new double[0][])).transpose();
        d.print("Verticies: ", vertices.columns(), " ", vertices.rows());
        d.print("Materials: " ,materials);
        d.print("Faces: ", faces);
    }

}

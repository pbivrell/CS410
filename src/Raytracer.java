import cs410.raytracer.*;
import cs410.debugging.Debug;

import java.util.Scanner;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.FileNotFoundException;
import cs410.io.Writer;

public class Raytracer{

    private static boolean DEBUG_ENABLED = true;
    public static final String EXE_NAME = "Raytracer";

    public static void main(String[] args){
        if(args.length < 2){
            System.out.println("[Fatal Error]: usage: ./" + EXE_NAME + " driver_file output_file");
            return;
        }

        boolean distributed = args.length > 2;
        //Driver d = (distributed) ? new Driver(args[2]) : new Driver();

        if(args.length > 3){ Debug.set_global_debugging(true); }
        if(args.length > 4){ Debug.set_verbose_debugging(true); }

        System.out.printf("= Setting up Sceen =\n");
        long start = System.currentTimeMillis();
        Scene scene = new Scene();
        scene.processDriver(args[0]);
        long process = System.currentTimeMillis();
        System.out.printf("[%dms]\n\n= Raytracing =\n", process - start);
        Image i = scene.raytrace();
        long raytracing = System.currentTimeMillis();
        System.out.printf("[%dms]\n\n= Writing File [%s] =\n", raytracing - process, args[1]);
        try{
            Writer.write(i.toString(),args[1]);
        }catch(IOException e){
            System.out.println("Couldn't write to file: " + e);
        }
        long writing = System.currentTimeMillis();
        System.out.printf("[%dms]\n\nTotal Time [%dms]\n", writing - raytracing, writing - start);


        // Sleep for 1 second to allow connections to
        /*if(distributed){
          try{
          Thread.sleep(1000);
          }catch (InterruptedException e){
          System.err.println("[Warning]: Sleeping thread was interrupted");
          Thread.currentThread().interrupt();
          }
          distributed = driver.getConnections() >= 2;
          }*/
    }
}

import cs410.raytracer.*;
import cs410.debugging.Debug;
import java.util.Scanner;
import java.io.File;
import java.io.IOException;

public class Modeltoworld{

    private static boolean DEBUG_ENABLED = true;
    public static final String EXE_NAME = "Modeltoworld";

    public static void main(String[] args){
        if(args.length < 1){
            System.out.println("[Fatal Error]: usage: ./" + EXE_NAME + " driver_file");
            return;
        }           

        if(args.length > 1){
            Debug.set_global_debugging(true);
        }
        if(args.length > 2){
            Debug.set_verbose_debugging(true);
        }

        Scene scene = new Scene();
        //try { 
            //scene.processDriver(new Scanner(new File(args[0])));
        //}catch(IOException e){
        //    System.out.printf("[Fatal Error]: failed to read file [%s] reason: %s\n", args[0], e); 
        //    System.exit(-1);
        //}

        //scene.writeFile(args[0]);

    }
}

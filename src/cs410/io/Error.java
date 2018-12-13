package cs410.io;

public class Error{

    public static void fatal(String s, Object... args){
        System.err.println("\nFatal Error: [" + Thread.currentThread().getStackTrace()[2].toString() + "]");
        System.out.printf(s,args);
        System.exit(-1);
    }
}

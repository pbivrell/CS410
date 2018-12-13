package cs410.debugging;

public class Debug{

    private static boolean global_enabled = false;
    private final boolean enabled;
    private static boolean verbose_enabled = false;
    

    public static void set_global_debugging(boolean enabled){
        System.out.println("Global Debugging: " + global_enabled + "[" + Thread.currentThread().getStackTrace()[2].toString() + "]");
        global_enabled = enabled;
    }
    
    public static void set_verbose_debugging(boolean enabled){
        System.out.println("Global Debugging: " + global_enabled + "[" + Thread.currentThread().getStackTrace()[2].toString() + "]");
        verbose_enabled = enabled;
    }

    public Debug(boolean enabled){
        this.enabled = enabled && global_enabled;
    }

    public void print(Object... args){
        if(enabled){
            if(verbose_enabled){
                System.out.println("\n[" + Thread.currentThread().getStackTrace()[2].toString() + "]");
            }
            for(Object o : args) {
                System.out.print(o);
                System.out.print(" ");
            }
            System.out.println();
        }
    }

    public static void main(String[] args){
        Debug d = new Debug(true);
        d.print("Hey","HELLOL THERE", "TEST");
    }
}

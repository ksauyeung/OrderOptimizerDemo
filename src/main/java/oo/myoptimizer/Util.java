package oo.myoptimizer;

public class Util {

    public static void log(String message) {
        System.out.println(message);
    }

    public static void log(String message, Throwable e) {
        log(message);
        log(e);
    }

    public static void log(Throwable e) {
        e.printStackTrace();
    }


}

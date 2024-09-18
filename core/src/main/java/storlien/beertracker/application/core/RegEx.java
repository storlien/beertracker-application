package storlien.beertracker.application.core;

public class RegEx {

    public static void method1(String s) {
        System.out.println("Kjørt litt av method1");

        if (s.equals("s")) {
            throw new IllegalArgumentException("Method1 melder s");
        }

        else {
            System.out.println("All good i metode1");
        }
    }

    public static void method2(String s) {
        try {
            method1(s);
        }

        catch (Exception e) {
            System.out.println("Catch i metode2:");
            System.out.println(e);
        }
    }

    public static void main(String[] args) {

        method2("s");

    }
}
package io.costax.puzzlers;

public class SimpleQuestion {

    public static boolean yesOrNo(String s) {
        s = s.toLowerCase();
        if (s.equals("yes") || s.equals("y") || s.equals("t")) {
            s = "true";
        }

        // The Boolean.getBoolean method does not do what you think it does
        // this method is a mistake, it lies,
        // the Boolean.getBoolean method tries to get a boolean
        // from System.getProperty(String)
        return Boolean.getBoolean(s);
    }

    public static boolean yesOrNoClearAndReadable(String s) {
        s = s.toLowerCase();
        return s.equals("yes") || s.equals("y") || s.equals("true") || s.equals("t");
    }

    public static void main(String[] args) {
        // write your code here
        System.setProperty("true", "true");
        System.setProperty("false", "true");
        System.out.println(yesOrNo("true") + " " + yesOrNo("YeS"));
    }
}

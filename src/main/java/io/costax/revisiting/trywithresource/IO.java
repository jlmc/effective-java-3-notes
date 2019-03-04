package io.costax.revisiting.trywithresource;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class IO {

    public static void main(String[] args) {

        try (
                OutputStream out = new FileOutputStream("out.txt")
                //InputStream in = new FileInputStream("in.txt");
        ) {

            // so some stuffs

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

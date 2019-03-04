package io.costax.item9;

import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class ReadLines {


    private static final int BUFFER_SIZE = 5;

    public static final List<String> allLinesOffFile(String path) throws IOException {
        final URL url = ReadLines.class.getClassLoader().getResource(path);
        final Path path1 = Paths.get(url.getPath());
        final List<String> strings = Files.readAllLines(path1);
        strings.forEach(System.out::println);
        return strings;
    }

    public static final String firstLineOffFile(String path) throws IOException {
        final String defaultValue = "----";
        /*
        // BAD Code
        final BufferedReader br = new BufferedReader(new FileReader(path));

        try {
            return br.readLine();
        } finally {
            br.close();
        }
        */

        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            return br.readLine();
        } catch (IOException e) {
            return defaultValue;
        }

    }

    static void copy(String src, String dst) throws IOException {
        /*
        // BAD code
        InputStream in = new FileInputStream(src);

        try {

            OutputStream out = new FileOutputStream(dst);
            try {

                byte[] buffer = new byte[BUFFER_SIZE];
                int n;
                while ((n = in.read(buffer)) >= 0) {
                    out.write(buffer, 0, n);
                }


            } finally {
                out.close();
            }

        } finally {
            in.close();
        }
         */

        try (InputStream in = new FileInputStream(src);
             OutputStream out = new FileOutputStream(dst)
        ) {
            byte[] buffer = new byte[BUFFER_SIZE];
            int n;
            while ((n = in.read(buffer)) >= 0) {
                out.write(buffer, 0, n);
            }
        }
    }

    public static void main(String[] args) throws IOException {
        final String path = "files/lines.txt";
        allLinesOffFile(path);

        File lines = new File(ReadLines.class.getResource("/" + path).getFile());

        final String parent = lines.getParent();
        System.out.println(parent + "/destinantion-copy.txt");
        System.out.println(firstLineOffFile(lines.getPath()));

        copy(lines.getPath(), parent + "/destinantion-copy.txt");
    }
}

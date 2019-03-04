package io.costax.revisiting.trywithresource;

import io.costax.item9.ReadLines;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class AnagramGroupsInADictionary {

    private static String alphabetize(final String s) {
        final char[] chars = s.toCharArray();
        Arrays.sort(chars);
        return new String(chars);
    }

    public static void main(String[] args) throws IOException {

        //String dictionaryFileNameParam = args[0];
        //String minGroupSizeParam = args[1];
        String dictionaryFileNameParam = "files/dictionary.txt";
        String minGroupSizeParam = "3";

        //File dictionary = new File("/files/dictionary.txt");
        File dictionary = new File(AnagramGroupsInADictionary.class.getResource("/" + dictionaryFileNameParam).getFile());
        int minGroupSize = Integer.parseInt(minGroupSizeParam);

        Map<String, Set<String>> groups = new HashMap<>();

        try (Scanner s = new Scanner(dictionary)) { // Item 9

            while (s.hasNext()) {
                final String word = s.next();
                groups.computeIfAbsent(alphabetize(word), (unused) -> new TreeSet<>()).add(word);
            }
        }

        for (Set<String> group : groups.values()) {
            if (group.size() >= minGroupSize)
                System.out.println(group.size() + ": " + group);
        }
    }
}

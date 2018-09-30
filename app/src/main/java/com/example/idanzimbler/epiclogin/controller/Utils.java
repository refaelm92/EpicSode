package com.example.idanzimbler.epiclogin.controller;

public class Utils {

    /**
     * This static method is responsible to give the lexicographically next string of a given string.
     * for example
     * Input : geeks
     * Output : geekt
     * @param str
     * @return lexicographically next string
     */
    public static String nextWord(String str) {
        // if string is empty
        if (str == "")
            return "a";
        // Find first character from
        // right which is not z.
        int i = str.length() - 1;
        while (str.charAt(i) == 'z' && i >= 0)
            i--;
        // If all characters are 'z',
        // append an 'a' at the end.
        if (i == -1)
            str = str + 'a';

        // If there are some
        // non-z characters
        else
            str = str.substring(0, i) +
                    (char) ((int) (str.charAt(i)) + 1) +
                    str.substring(i + 1);
        return str;
    }
}

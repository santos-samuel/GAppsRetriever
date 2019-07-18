package com.example.mvpexample.model;

public class Cache {

    public static final int BLUE = -16776961;
    public static final int GREEN = -16711936;
    public static final int RED = -65536;
    public static final int YELLOW = -256;

    private static final int[] COLOR_ARRAY = {BLUE, GREEN, RED, YELLOW};
    private static final String[] STRING_ARRAY = {"AAA", "BBB", "CCC", "DDD"};

    public int getNextColor(int colorID) {
        for (int i = 0; i < COLOR_ARRAY.length; i++) {
            if (colorID == COLOR_ARRAY[i]) {
                if (i == COLOR_ARRAY.length - 1)
                    return COLOR_ARRAY[0];
                else
                    return COLOR_ARRAY[i+1];
            }
        }

        return BLUE;
    }

    public String getNextString(String oldString) {
        for (int i = 0; i < STRING_ARRAY.length; i++) {
            if (oldString.equals(STRING_ARRAY[i])) {
                if (i == STRING_ARRAY.length - 1)
                    return STRING_ARRAY[0];
                else
                    return STRING_ARRAY[i+1];
            }
        }

        return STRING_ARRAY[0];
    }
}

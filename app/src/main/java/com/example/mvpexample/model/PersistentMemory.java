package com.example.mvpexample.model;

public class PersistentMemory {

    public static final int BLUE = -16776961;
    public static final int GREEN = -16711936;
    public static final int RED = -65536;
    public static final int YELLOW = -256;
    public static final int UNSET = -1;

    private static final int[] COLOR_ARRAY = {BLUE, GREEN, RED, YELLOW};
    private static final String[] STRING_ARRAY = {"AAA", "BBB", "CCC", "DDD"};


    private int lastGivenColor = COLOR_ARRAY[0];       // default value
    private String lastGivenString = STRING_ARRAY[0];  // default value

    public int getNextColor(int colorID) {
        int retValue = UNSET;
        for (int i = 0; i < COLOR_ARRAY.length; i++) {
            if (colorID == COLOR_ARRAY[i]) {
                if (i == COLOR_ARRAY.length - 1) {
                    retValue = COLOR_ARRAY[0];
                    break;
                }
                else {
                    retValue = COLOR_ARRAY[i + 1];
                    break;
                }
            }
        }

        if (retValue == UNSET)
            retValue = BLUE;

        lastGivenColor = retValue;
        return retValue;
    }

    public String getNextString(String oldString) {
        String retValue = null;
        for (int i = 0; i < STRING_ARRAY.length; i++) {
            if (oldString.equals(STRING_ARRAY[i])) {
                if (i == STRING_ARRAY.length - 1) {
                    retValue = STRING_ARRAY[0];
                    break;
                }
                else {
                    retValue = STRING_ARRAY[i+1];
                    break;
                }
            }
        }

        if (retValue == null)
            retValue = STRING_ARRAY[0];

        lastGivenString = retValue;
        return retValue;
    }

    public String getLastGivenString(){
        return lastGivenString;
    }

    public int getLastGivenColor() {
        return lastGivenColor;
    }
}

package me.koenn.blockrpg.util;

/**
 * Simple class to automatically capitalise, translate color codes and format strings. Simply create an instance of the
 * class and call the <code>toString</code> method to get the formatted string.
 * <p>
 * Copyright (C) Koenn - All Rights Reserved Unauthorized copying of this file, via any medium is strictly prohibited
 * Proprietary and confidential Written by Koen Willemse, May 2017
 */
@SuppressWarnings("unused")
public class FancyString {

    private final String string;

    /**
     * Default constructor for a FancyString object.
     *
     * @param object String object to format.
     */
    public FancyString(Object object) {
        String objectString = object.toString().replaceAll("_", " ").toLowerCase();
        String[] arr = objectString.split(" ");
        StringBuilder sb = new StringBuilder();
        for (String anArr : arr) {
            sb.append(Character.toUpperCase(anArr.charAt(0))).append(anArr.substring(1)).append(" ");
        }
        this.string = sb.toString().trim();
    }

    @Override
    public String toString() {
        return this.string;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof FancyString && obj.toString().equals(this.toString());
    }
}

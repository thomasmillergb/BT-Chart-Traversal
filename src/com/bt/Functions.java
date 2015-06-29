package com.bt;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Thomas on 29/06/2015.
 */
public class Functions {
    public String stripNoise(String s){
        s=s.replaceAll("\\s+", " ").trim();
        String removeNoise = "([A-Za-z0-9]\\s*)+[A-Za-z0-9]";
        Pattern r = Pattern.compile(removeNoise);
        Matcher nm1 = r.matcher(s);
        if (nm1.find()) {
            s = nm1.group();
        }
        return s.toLowerCase();
    }
}

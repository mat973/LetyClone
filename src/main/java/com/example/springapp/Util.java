package com.example.springapp;

import javax.servlet.http.HttpServletRequest;
import java.util.regex.*;

public class Util {
    public static String resolveUrl(HttpServletRequest req, String localAddress) {
        String reqURL = req.getRequestURL().toString();
        Pattern pat = Pattern.compile("^https?://.+?(/|$)");
        Matcher m = pat.matcher(reqURL);
        String result;
        if (m.find()) {
            result = reqURL.substring(m.start(), m.end());
        } else {
            result = "http://localhost:8080/";
        }
        if (result.charAt(result.length() - 1) != '/'
        && localAddress.charAt(0) != '/') {
            result += '/';
        }
        result += localAddress;
        return result;
    }
}

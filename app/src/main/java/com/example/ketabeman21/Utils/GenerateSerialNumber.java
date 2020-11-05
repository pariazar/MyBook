package com.example.ketabeman21.Utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

public class GenerateSerialNumber {

    public static String createTransactionID(String uID) throws Exception{
        String s1, s2;
        s1 = uID;
        s2 = UUID.randomUUID().toString().replaceAll("-", "").toUpperCase();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < s1.length(); i++) {
            sb.append(s1.charAt(i));
            sb.append(s2.charAt(i));
        }
        s1 = sb.toString();
        s2 = UUID.randomUUID().toString().replaceAll("-", "").toUpperCase();

        for (int i = 0; i < s1.length(); i++) {
            sb.append(s1.charAt(i));
            sb.append(s2.charAt(i));
        }
        String result = sb.toString();
        return result;
    }

}

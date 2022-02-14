package com.schain.util;

import com.google.gson.GsonBuilder;

import java.security.Key;
import java.util.Base64;

/**
 *  This class is a utility class to convert our data into some formats like
 *  byte to hexadecimal format, convert public key to Base64 format,...
 */
public class CastUtil {
    private CastUtil() {

    }

    /**
     * Method to convert Encryption algorithm digested message into Hexadecimal format
     * @param input  Encryption message in byte[] format
     * @return digested message in hexadecimal format
     */
    public static String toHexFormat(byte[] input) {
        StringBuilder hex = new StringBuilder();
        for (byte b : input) {
            hex.append(String.format("%02X", b));
        }
        return hex.toString();
    }

    /**
     * Get base64 form of the public keys
     * @param key public key
     * @return base64 form of public key in string
     */
    public static String getBase64EncodedKey(Key key) {
        return Base64.getEncoder().encodeToString(key.getEncoded());
    }

    /**
     * Cast Given objects to JSON
     * @param o Objects need to be serialized
     * @return JSON
     */
    public static String getJson(Object o) {
        return new GsonBuilder().setPrettyPrinting().create().toJson(o);
    }
}

package com.schain.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * A class to encrypt inputs in SHA256 algorithm.
 * we use this class to encrypt Blockchain's blocks data.
 */
public class Sha256 {
    /**
     *  This is Logger object using to create logs.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(Sha256.class);

    private Sha256() {

    }

    /**
     * This method is responsible to digest an input using SHA256
     * @param input input that needs to be encrypted
     * @return returns hex format of the encrypted message
     */
    public static String digest(String input) {
        var inputInBytes = input.getBytes(StandardCharsets.UTF_8);
        try {
            var messageDigest = MessageDigest.getInstance("SHA256");
            var digestedInput = messageDigest.digest(inputInBytes);
            return CastUtil.toHexFormat(digestedInput);
        } catch (NoSuchAlgorithmException e) {
            LOGGER.debug("The encryption algorithm doesn't exist");
            e.printStackTrace();
            return null;
        }
    }
}

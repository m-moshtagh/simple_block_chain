package com.schain.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;

/**
 * class to create DigitalSignature using RSA & SHA256 algorithms in Java
 */
public class DigitalSignature {
    private static final Logger LOGGER = LoggerFactory.getLogger(DigitalSignature.class);

    private DigitalSignature() {

    }

    /**
     * Sign using Java Signature Class
     * @param privateKey private key of sender for encrypting the transaction
     * @param input the data
     * @return signature in byte[]
     */
    public static byte[] applyRsaSign(PrivateKey privateKey, String input) {
        Signature signature;
        var output = new byte[0];
        try {
            signature = Signature.getInstance("SHA256WithRSA");
            signature.initSign(privateKey);
            var inputInByte = input.getBytes(StandardCharsets.UTF_8);
            signature.update(inputInByte);
            output = signature.sign();
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.debug("Signing Failed!");
        }
        return output;
    }

    /**
     * Verify using Java Signature class
     * @param publicKey public key of sender for decrypting the data
     * @param data actual data that was encrypted
     * @param signature The signature to compare with computed one
     * @return true if verification passes
     */
    public static boolean verifyRsaSign(PublicKey publicKey, String data, byte[] signature) {
        try {
            Signature signatureVerifier = Signature.getInstance("SHA256WithRSA");
            signatureVerifier.initVerify(publicKey);
            signatureVerifier.update(data.getBytes(StandardCharsets.UTF_8));
            return signatureVerifier.verify(signature);
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.debug("Verification Failed! reconsider your signature!");
            throw new IllegalStateException();
        }
    }
}

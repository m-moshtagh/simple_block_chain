package com.schain.entity;

import com.schain.application.SimpleChain;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Wallet is used to store the fees. it creates a keypair in order to be unique
 */
public class Wallet {
    private static final Logger LOGGER = LoggerFactory.getLogger(Wallet.class);
    private PrivateKey privateKey;
    private PublicKey publicKey;
    private Map<String, TransactionOutput> UTXOs = new HashMap<>();

    public Wallet() {
        generateKeyPair();
    }


    /**
     * Generates key pair based on RSA algorithm
     */
    public void generateKeyPair() {
        try {
            var generator = KeyPairGenerator.getInstance("RSA");
            generator.initialize(2048, SecureRandom.getInstance("SHA1PRNG"));
            var keyPair = generator.generateKeyPair();
            this.privateKey = keyPair.getPrivate();
            this.publicKey = keyPair.getPublic();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            LOGGER.debug("The operation for Wallet key generation went wrong");
        }
    }

    public float getBalance() {
        var total = 0;
        for (Map.Entry<String, TransactionOutput> item : SimpleChain.UTXOs.entrySet()) {
            var UTXO = item.getValue();
            if (UTXO.isMine(publicKey)) {
                UTXOs.put(UTXO.getId(), UTXO);
                total += UTXO.getValue();
            }
        }
        return total;
    }

    public Transaction sendFunds(PublicKey recipient, float value) {
        if (getBalance() < value) {
            LOGGER.error("Not enough fund to send transaction");
            return null;
        }
        var inputs = new ArrayList<TransactionInput>();
        float total = 0;
        for (Map.Entry<String, TransactionOutput> item : UTXOs.entrySet()) {
            TransactionOutput UTXO = item.getValue();
            total += UTXO.getValue();
            inputs.add(new TransactionInput(UTXO.getId()));
            if (total > value) break;
        }
        Transaction newTransaction = new Transaction(publicKey, recipient, value, inputs);
        newTransaction.generateSignature(privateKey);

        for (TransactionInput input : inputs) {
            UTXOs.remove(input.getTransactionOutputId());
        }
        return newTransaction;
    }

    // Getters & Setters

    public PrivateKey getPrivateKey() {
        return privateKey;
    }

    public Wallet setPrivateKey(PrivateKey privateKey) {
        this.privateKey = privateKey;
        return this;
    }

    public PublicKey getPublicKey() {
        return publicKey;
    }

    public Wallet setPublicKey(PublicKey publicKey) {
        this.publicKey = publicKey;
        return this;
    }

    public Map<String, TransactionOutput> getUTXOs() {
        return UTXOs;
    }

    public Wallet setUTXOs(Map<String, TransactionOutput> UTXOs) {
        this.UTXOs = UTXOs;
        return this;
    }
}

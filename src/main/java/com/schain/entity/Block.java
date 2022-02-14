package com.schain.entity;

import com.schain.util.Sha256;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Chains of this Block class will construct our blockchain. Each block
 * has its own digital signature which is a hash constructed from:
 * -> previous block's signature + current block data + creation time of block
 */
public class Block {
    private static final Logger LOGGER = LoggerFactory.getLogger(Block.class);
    private final long creationTime;
    private String digitalSignature;
    private String previousSignature;
    // check integration of blocks
    private String merkleRoot;
    private List<Transaction> transactions = new ArrayList<>();
    private int nonce = 0;

    /**
     * constructor
     *
     * @param previousHash SHA256 signature of previous block
     */
    public Block(String previousHash) {
        this.previousSignature = previousHash;
        this.creationTime = System.currentTimeMillis();
        this.digitalSignature = calculateSignature();
    }

    /**
     * increment nonce in order to mine the block
     */
    public void incrementNonce() {
        nonce++;
    }

    /**
     * this method calculates the digital signature of the block by digesting:
     * previous signature & block creation time & block data & nonce(for mining purpose)
     *
     * @return digested message in SHA256 algorithm in hexadecimal order in a String
     */
    public String calculateSignature() {
        return Sha256.digest(
                previousSignature
                        + creationTime
                        + nonce
                        + merkleRoot);
    }

    /**
     *
     * @param transaction
     * @return
     */
    public boolean addTransaction(Transaction transaction) {
        //process transaction and check if valid, unless block is genesis block then ignore.
        if (transaction == null) return false;
        if (!"0".equals(this.previousSignature) && !transaction.processTransaction()) {
            LOGGER.error("Transaction Failed to process. Discarded!");
            return false;
        }
        this.transactions.add(transaction);
        LOGGER.info("Transaction Successfully submitted to block");
        return true;
    }

    // Setters & Getters

    public long getCreationTime() {
        return creationTime;
    }

    public String getDigitalSignature() {
        return digitalSignature;
    }

    public Block setDigitalSignature(String digitalSignature) {
        this.digitalSignature = digitalSignature;
        return this;
    }

    public String getPreviousSignature() {
        return previousSignature;
    }

    public Block setPreviousSignature(String previousSignature) {
        this.previousSignature = previousSignature;
        return this;
    }

    public String getMerkleRoot() {
        return merkleRoot;
    }

    public Block setMerkleRoot(String merkleRoot) {
        this.merkleRoot = merkleRoot;
        return this;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public Block setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
        return this;
    }

    public int getNonce() {
        return nonce;
    }

    public Block setNonce(int nonce) {
        this.nonce = nonce;
        return this;
    }
}

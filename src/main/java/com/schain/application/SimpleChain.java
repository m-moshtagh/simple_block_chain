package com.schain.application;

import com.schain.entity.*;
import com.schain.service.mining.MineBlock;
import com.schain.service.validation.BlockChainIntegrity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class SimpleChain {
    public static final List<Block> blockChain = new ArrayList<>();
    public static Map<String, TransactionOutput> UTXOs = new HashMap<>();
    public static final int DIFFICULTY = 4;
    public static final float MINIMUM_TRANSACTION = 0.1f;
    private static final Logger LOGGER = LoggerFactory.getLogger(SimpleChain.class);
    public static Transaction genesisTransaction;
    private static Scanner INPUT_READER = new Scanner(System.in);
    private static List<Wallet> walletList = new ArrayList<>();

    public static void main(String[] args) {

        Wallet walletA = new Wallet();
        Wallet walletB = new Wallet();
        Wallet coinBase = new Wallet();

        genesisTransaction = new Transaction(coinBase.getPublicKey(), walletA.getPublicKey(), 100f, null);
        genesisTransaction.generateSignature(coinBase.getPrivateKey());
        genesisTransaction.setTransactionId("0");
        genesisTransaction.getOutputs().add(new TransactionOutput(genesisTransaction.getRecipient(), genesisTransaction.getValue(), genesisTransaction.getTransactionId()));
        UTXOs.put(genesisTransaction.getOutputs().get(0).getId(), genesisTransaction.getOutputs().get(0));

        LOGGER.info("Creating and Mining Genesis Block...");

        Block genesis = new Block("0");
        genesis.addTransaction(genesisTransaction);
        addBlock(genesis);

        Block block1 = new Block(genesis.getDigitalSignature());
        LOGGER.info("\nWalletA's balance is: {}", walletA.getBalance());
        LOGGER.info("\nWalletA is Attempting to send funds (40) to WalletB...");
        block1.addTransaction(walletA.sendFunds(walletB.getPublicKey(), 40f));
        addBlock(block1);
        LOGGER.info("\nWalletA's balance is: {}", walletA.getBalance());
        LOGGER.info("WalletB's balance is: {}", walletB.getBalance());

        Block block2 = new Block(block1.getDigitalSignature());
        LOGGER.info("\nWalletA Attempting to send more funds (1000) than it has...");
        block2.addTransaction(walletA.sendFunds(walletB.getPublicKey(), 1000f));
        addBlock(block2);
        LOGGER.info("\nWalletA's balance is: {}", walletA.getBalance());
        LOGGER.info("WalletB's balance is: {}", walletB.getBalance());

        Block block3 = new Block(block2.getDigitalSignature());
        LOGGER.info("\nWalletB is Attempting to send funds (20) to WalletA...");
        block3.addTransaction(walletB.sendFunds( walletA.getPublicKey(), 20));
        LOGGER.info("\nWalletA's balance is: {}", walletA.getBalance());
        LOGGER.info("WalletB's balance is: {}", walletB.getBalance());

        BlockChainIntegrity.isBlockChainValid(blockChain);
    }
    public static void addBlock(Block newBlock) {
        MineBlock.mine(newBlock, DIFFICULTY);
        blockChain.add(newBlock);
    }
}

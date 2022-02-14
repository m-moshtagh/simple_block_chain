package com.schain.entity;

import com.schain.application.SimpleChain;
import com.schain.util.CastUtil;
import com.schain.util.DigitalSignature;
import com.schain.util.Sha256;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.List;

/**
 * Each Transaction will contain public key of sender and recipient, value of the transaction, signature of the
 * transaction to see if it's valid or not, inputs which refers to the previous transactions that prove the sender
 * has funds to send, outputs which shows the amount relevant addresses receive in this transaction. this output
 * will be used as input in new transactions.
 */
public class Transaction {
    private static final Logger LOGGER = LoggerFactory.getLogger(Transaction.class);
    private static int sequence = 0;
    private String transactionId;
    private PublicKey sender;
    private PublicKey recipient;
    private float value;
    private byte[] signature;
    private List<TransactionInput> inputs;
    private List<TransactionOutput> outputs = new ArrayList<>();

    public Transaction(PublicKey from, PublicKey to, float value, List<TransactionInput> inputs) {
        this.sender = from;
        this.recipient = to;
        this.value = value;
        this.inputs = inputs;
    }

    public static int getSequence() {
        return sequence;
    }

    public static void setSequence(int sequence) {
        Transaction.sequence = sequence;
    }

    /**
     * calculate a hash from sender & recipient pub keys + value of transaction and a sequence to make each
     * transaction unique.
     *
     * @return digested SHA256 string
     */
    private String calculateTransactionHash() {
        sequence++;
        return Sha256.digest(
                CastUtil.getBase64EncodedKey(sender)
                        + CastUtil.getBase64EncodedKey(recipient)
                        + value
                        + sequence
        );
    }

    /**
     * sign our transaction
     *
     * @param privateKey private key of sender
     */
    public void generateSignature(PrivateKey privateKey) {
        var data = CastUtil.getBase64EncodedKey(sender)
                + CastUtil.getBase64EncodedKey(recipient)
                + value;
        signature = DigitalSignature.applyRsaSign(privateKey, data);
    }

    /**
     * verify the signature to prevent fraud
     *
     * @return true if the verification passes
     */
    public boolean verifySignature() {
        var data = CastUtil.getBase64EncodedKey(sender)
                + CastUtil.getBase64EncodedKey(recipient)
                + value;
        return DigitalSignature.verifyRsaSign(sender, data, signature);
    }

    public boolean processTransaction() {
        if (!verifySignature()) {
            LOGGER.error("Transaction Signature verification failed!");
            return false;
        }
        for (TransactionInput i : inputs) {
            i.setUTXO(SimpleChain.UTXOs.get(i.getTransactionOutputId()));
        }

        if (getInputsValue() < SimpleChain.MINIMUM_TRANSACTION) {
            LOGGER.error("Transaction inputs are too small: {}", getInputsValue());
            LOGGER.error("Please enter amount greater than: {}", SimpleChain.MINIMUM_TRANSACTION);
            return false;
        }

        var leftOver = getInputsValue() - value;
        transactionId = calculateTransactionHash();
        outputs.add(new TransactionOutput(this.recipient, value, transactionId));
        outputs.add(new TransactionOutput(this.sender, leftOver, transactionId));

        for (TransactionOutput o : outputs) {
            SimpleChain.UTXOs.put(o.getId(), o);
        }

        for (TransactionInput i : inputs) {
            if (i.getUTXO() == null) continue;
            SimpleChain.UTXOs.remove(i.getUTXO().getId());
        }

        return true;
    }

    public float getInputsValue() {
        float total = 0;
        for (TransactionInput i : inputs) {
            if (i.getUTXO() == null) continue; //if Transaction can't be found skip it
            total += i.getUTXO().getValue();
        }
        return total;
    }

    //returns sum of outputs:
    public float getOutputsValue() {
        float total = 0;
        for (TransactionOutput o : outputs) {
            total += o.getValue();
        }
        return total;
    }

    // Setters & Getters
    public String getTransactionId() {
        return transactionId;
    }

    public Transaction setTransactionId(String transactionId) {
        this.transactionId = transactionId;
        return this;
    }

    public PublicKey getSender() {
        return sender;
    }

    public Transaction setSender(PublicKey sender) {
        this.sender = sender;
        return this;
    }

    public PublicKey getRecipient() {
        return recipient;
    }

    public Transaction setRecipient(PublicKey recipient) {
        this.recipient = recipient;
        return this;
    }

    public float getValue() {
        return value;
    }

    public Transaction setValue(float value) {
        this.value = value;
        return this;
    }

    public byte[] getSignature() {
        return signature;
    }

    public Transaction setSignature(byte[] signature) {
        this.signature = signature;
        return this;
    }

    public List<TransactionInput> getInputs() {
        return inputs;
    }

    public Transaction setInputs(List<TransactionInput> inputs) {
        this.inputs = inputs;
        return this;
    }

    public List<TransactionOutput> getOutputs() {
        return outputs;
    }

    public Transaction setOutputs(List<TransactionOutput> outputs) {
        this.outputs = outputs;
        return this;
    }
}

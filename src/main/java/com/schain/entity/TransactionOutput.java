package com.schain.entity;

import com.schain.util.CastUtil;
import com.schain.util.Sha256;

import java.security.PublicKey;

public class TransactionOutput {
    private String id;
    private PublicKey recipient;
    private float value;
    private String parentTransactionId;

    public TransactionOutput(PublicKey recipient, float value, String parentTransactionId) {
        this.recipient = recipient;
        this.value = value;
        this.parentTransactionId = parentTransactionId;
        this.id = Sha256.digest(
                CastUtil.getBase64EncodedKey(recipient)
                        + value
                        + parentTransactionId
        );
    }

    public boolean isMine(PublicKey publicKey) {
        return publicKey == recipient;
    }

    public String getId() {
        return id;
    }

    public TransactionOutput setId(String id) {
        this.id = id;
        return this;
    }

    public PublicKey getRecipient() {
        return recipient;
    }

    public TransactionOutput setRecipient(PublicKey recipient) {
        this.recipient = recipient;
        return this;
    }

    public float getValue() {
        return value;
    }

    public TransactionOutput setValue(float value) {
        this.value = value;
        return this;
    }

    public String getParentTransactionId() {
        return parentTransactionId;
    }

    public TransactionOutput setParentTransactionId(String parentTransactionId) {
        this.parentTransactionId = parentTransactionId;
        return this;
    }
}

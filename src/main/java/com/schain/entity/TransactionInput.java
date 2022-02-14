package com.schain.entity;

public class TransactionInput {
    private String transactionOutputId;
    private TransactionOutput UTXO;

    public TransactionInput(String transactionOutputId) {
        this.transactionOutputId = transactionOutputId;
    }

    public String getTransactionOutputId() {
        return transactionOutputId;
    }

    public TransactionInput setTransactionOutputId(String transactionOutputId) {
        this.transactionOutputId = transactionOutputId;
        return this;
    }

    public TransactionOutput getUTXO() {
        return UTXO;
    }

    public TransactionInput setUTXO(TransactionOutput UTXO) {
        this.UTXO = UTXO;
        return this;
    }
}

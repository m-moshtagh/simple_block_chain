package com.schain.util;

import com.schain.entity.Transaction;

import java.util.ArrayList;
import java.util.List;

public class MerkleTree {
    private MerkleTree() {

    }

    /**
     *
     * @param transactions
     * @return
     */
    public static String getMerkleRoot(List<Transaction> transactions) {
        int count = transactions.size();
        var previousTreeLayer = new ArrayList<String>();
        for (var transaction : transactions) {
            previousTreeLayer.add(transaction.getTransactionId());
        }
        ArrayList<String> treeLayer = previousTreeLayer;
        while (count > 1) {
            treeLayer = new ArrayList<>();
            for (var i = 1; i < previousTreeLayer.size(); i+=2) {
                treeLayer.add(Sha256.digest(previousTreeLayer.get(i - 1) + previousTreeLayer.get(i)));
            }
            count = treeLayer.size();
            previousTreeLayer = treeLayer;
        }
        return (treeLayer.size() == 1) ? treeLayer.get(0) : "";
    }
}

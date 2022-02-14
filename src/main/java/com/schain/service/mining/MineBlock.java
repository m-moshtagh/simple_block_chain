package com.schain.service.mining;

import com.schain.entity.Block;
import com.schain.util.MerkleTree;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * class to Mine a block
 */
public class MineBlock {

    private static final Logger LOGGER = LoggerFactory.getLogger(MineBlock.class);

    private MineBlock() {

    }

    /**
     * Mining a block is heavy computational job, here we just try to point to a glimpse of it. In here we need to compute
     * a hash for the block so that we meet the difficulty to proof the work and get the prize.
     * The job here is to increment a nonce value and make the hash so that the first digits of the hash are 0. like if
     * the difficulty is 5 we may get:
     * 00000B278EABI47428AD1BE7244E7A749D7DA5D1A708A86C9081D83661C3D043 -> this is a valid block
     *
     * @param block      block we want to mine.
     * @param difficulty difficulty to proof of work
     */
    public static void mine(Block block, int difficulty) {
        String merkleRoot = MerkleTree.getMerkleRoot(block.getTransactions());
        String target = new String(new char[difficulty]).replace('\0', '0'); //Create a string with difficulty * "0"
        String digitalSignature = block.getDigitalSignature();
        block.setMerkleRoot(merkleRoot);
        while (!digitalSignature.substring(0, difficulty).equals(target)) {
            block.incrementNonce();
            digitalSignature = block.calculateSignature();
        }
        block.setDigitalSignature(digitalSignature);
        LOGGER.info("Block Mined: {}", block.getDigitalSignature());
    }
}

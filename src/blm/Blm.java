package blm;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.BitSet;

public class Blm {
    private BitSet bitSet;
    private int size;
    private MessageDigest md1, md2, md3;

    public Blm(int bits) {
        this.size = bits;
        this.bitSet = new BitSet(size);
        try {
            md1 = MessageDigest.getInstance("MD5");
            md2 = MessageDigest.getInstance("SHA-1");
            md3 = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Hash algo not found", e);
        }   
    }

    private int hash(byte[] data, MessageDigest md) {
        byte[] hash = md.digest(data);
        int h = 0;
        for (byte b : hash) {
            h = (h << 5) - h + (b & 0xFF);
        }
        return Math.abs(h % size);
    }

    public void add(byte[] data) {
        bitSet.set(hash(data, md1));
        bitSet.set(hash(data, md2));
        bitSet.set(hash(data, md3));
    }

    public boolean contains(byte[] data) {
        return bitSet.get(hash(data, md1)) &&
               bitSet.get(hash(data, md2)) &&
               bitSet.get(hash(data, md3));
    }
}

package proc;

import java.math.BigInteger;
import java.util.concurrent.ThreadLocalRandom;

import org.bouncycastle.math.ec.ECPoint;
import org.bouncycastle.util.encoders.Hex;

import ecc.Secp256k1;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;

public class LuckRnd {
    public static void main(String[] args) {
        if (args.length != 2 || !args[0].equals("-r")) {
            System.out.println("Usage: java -cp \"libs/*;bin\" Main -m luckrnd -r lower:upper");
            return;
        }

        String[] bounds = args[1].split(":");
        if (bounds.length != 2) {
            System.out.println("Invalid range format. Use lower:upper");
            return;
        }

        try {
            BigInteger lowerBound = new BigInteger(bounds[0]);
            BigInteger upperBound = new BigInteger(bounds[1]);

            if (lowerBound.compareTo(upperBound) >= 0) {
                System.out.println("Lower bound must be less than upper bound.");
                return;
            }

            BigInteger range = upperBound.subtract(lowerBound);
            boolean found = false;
            String targetPrefix = "e0b8a2";

            long startTime = System.nanoTime();

            while (!found) {
                BigInteger randomNumber = lowerBound.add(new BigInteger(range.bitLength(), ThreadLocalRandom.current()).mod(range));

                if (randomNumber.compareTo(lowerBound) >= 0 && randomNumber.compareTo(upperBound) <= 0) {
                    ECPoint publicKey = Secp256k1.getPublicKey(randomNumber);
                    String compressedPubKey = Secp256k1.toCompressedPublicKey(publicKey);
                    String hashPubkey = Secp256k1.hash160(new BigInteger(compressedPubKey, 16).toByteArray());
                    // String hashInt = Secp256k1.hash160(Hex.decode(hashPubkey));
                    if (hashPubkey.startsWith(targetPrefix)) {
                        long endTime = System.nanoTime();
                        long executionTime = (endTime - startTime) / 1_000_000;
                        System.out.println("Found: " + randomNumber);
                        System.out.println("CompressPubkey: " + compressedPubKey);
                        System.out.println("Hash160: " + hashPubkey);
                        System.out.println("Time Exc: " + executionTime + " ms");
                        found = true;
                    }
                }
            }
        } catch (NoSuchAlgorithmException e) {
            System.out.println("Invalid number format. Ensure both bounds are valid integers.");
        }
    }
}

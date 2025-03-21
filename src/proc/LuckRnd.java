package proc;

import java.math.BigInteger;
import java.util.concurrent.ThreadLocalRandom;

import org.bouncycastle.math.ec.ECPoint;

import ecc.Secp256k1;

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

            while (!found) {
                BigInteger randomNumber = lowerBound.add(new BigInteger(range.bitLength(), ThreadLocalRandom.current()).mod(range));

                if (randomNumber.compareTo(lowerBound) >= 0 && randomNumber.compareTo(upperBound) <= 0) {
                    ECPoint publicKey = Secp256k1.getPublicKey(randomNumber);
                    String compressedPubKey = Secp256k1.toCompressedPublicKey(publicKey);
                    System.out.println("Found Random Number: " + randomNumber);
                    System.out.println("CompressPubkey: " + compressedPubKey);
                    found = true;
                }
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid number format. Ensure both bounds are valid integers.");
        }
    }
}

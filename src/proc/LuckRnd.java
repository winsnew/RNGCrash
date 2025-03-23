package proc;

import java.math.BigInteger;
import java.util.concurrent.ThreadLocalRandom;

import org.bouncycastle.math.ec.ECPoint;
import org.bouncycastle.util.encoders.Hex;
 
import com.google.common.hash.BloomFilter;
import com.google.common.hash.Funnels;

import ecc.Secp256k1;
import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;

public class LuckRnd {
    public static void main(String[] args) {
        if (args.length != 2 || !args[0].equals("-r")) {
            System.out.println("Usage: java -cp \"libs/*;bin\" Main -m luckrnd -r lower:upper");
            return;
        }

        String rangeArg = args[1];
        int ramGB = 2;

        for (int i = 2; i < args.length; i++) {
            if (args[i].equals("-k") && i + 1 < args.length) {
                try {
                    ramGB = Integer.parseInt(args[i + 1]);
                    if (ramGB < 1 || ramGB > 4) {
                        System.out.println("Invalid RAM Value. Use -k 2 or 4");
                        return;
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Invalid RAM Format.");
                    return;
                }
            }
        }

        String[] bounds = rangeArg.split(":");
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
            String targetPrefix = "e0b8a2baee1b77fc";

            long startTime = System.nanoTime();
            OperatingSystemMXBean osBean = ManagementFactory.getPlatformMXBean(OperatingSystemMXBean.class);

            int bloomSize = (ramGB == 2) ? 500_000_000 : 1_000_000_000;
            System.out.println("Using Bloom Filter with " + ramGB + "GB RAM (" + bloomSize + " bits)");
            
            BloomFilter<byte[]> bloomFilter = BloomFilter.create(
                Funnels.byteArrayFunnel(), bloomSize
            );

            while (!found) {
                BigInteger randomNumber = lowerBound.add(new BigInteger(range.bitLength(), ThreadLocalRandom.current()).mod(range));

                if (randomNumber.compareTo(lowerBound) >= 0 && randomNumber.compareTo(upperBound) <= 0) {
                    if (bloomFilter.mightContain(randomNumber.toByteArray())) {
                        continue;
                    }

                    bloomFilter.put(randomNumber.toByteArray());

                    ECPoint publicKey = Secp256k1.getPublicKey(randomNumber);
                    String compressedPubKey = Secp256k1.toCompressedPublicKey(publicKey);
                    String hashPubkey = Secp256k1.hash160(new BigInteger(compressedPubKey, 16).toByteArray());
                    // String hashInt = Secp256k1.hash160(Hex.decode(hashPubkey));
                    double cpuLoad = osBean.getSystemLoadAverage() * 100;
                    System.out.printf("\rCheck: %s | CPU Usage: %.2f%%", randomNumber, cpuLoad);

                    if (hashPubkey.startsWith(targetPrefix)) {
                        long endTime = System.nanoTime();
                        long executionTime = (endTime - startTime) / 1_000_000;
                        System.out.println("Found: " + randomNumber);
                        System.out.println("CompressPubkey: " + compressedPubKey);
                        System.out.println("Hash160: " + hashPubkey);
                        System.out.println("Time Exc: " + executionTime + " ms");
                        System.out.printf("Final CPU usage: %.2f%%\n", osBean.getSystemLoadAverage() * 100);
                        found = true;
                    }
                }
            }
        } catch (NoSuchAlgorithmException e) {
            System.out.println("Invalid number format. Ensure both bounds are valid integers.");
        }
    }
}

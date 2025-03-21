package proc;

import ecc.Secp256k1;
import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import utils.BinMod;
import utils.PkToPb;

public class bins {
    public static void bin(String[] args) {
        try {
            String pkbiner = "11001111111111111111111111111111111111111111111111111111111111111111";
            int flipCount = 33;
            boolean found = false;
            
            while (!found) {
                String modifiedBinary = BinMod.flipBits(pkbiner, flipCount);
                BigInteger paddedKeyInt = new BigInteger(modifiedBinary, 2);
                String paddedHex = paddedKeyInt.toString(16).toUpperCase();
                
                String compressedPubkey = PkToPb.convertToCmpPublicKey(modifiedBinary);
                byte[] pubKeyBytes = new BigInteger(compressedPubkey, 16).toByteArray();
                String pubKeyHash160 = Secp256k1.hash160(pubKeyBytes);
                
                if (pubKeyHash160.startsWith("e0b")) {
                    System.out.println("Found Matching Public Key Hash160!");
                    System.out.println("Modified Binary  : " + modifiedBinary);
                    System.out.println("Public Key Hash160: " + pubKeyHash160);
                    found = true;
                }
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

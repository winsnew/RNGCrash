import ecc.Secp256k1;
import java.math.BigInteger;
import java.security.NoSuchAlgorithmException;
import proc.BinMod;
import utils.PkToPb;

public class Main {
    public static void main(String[] args) {
        try {
            String pkbiner = "11111111111111111111111111111111111111111111111111111111111111111111";
            int flipCount = 33;
            boolean found = false;
            
            while (!found) {
                String modifiedBinary = BinMod.flipBits(pkbiner, flipCount);
                BigInteger paddedKeyInt = new BigInteger(modifiedBinary, 2);
                String paddedHex = paddedKeyInt.toString(16).toUpperCase();
                
                String compressedPubkey = PkToPb.convertToCmpPublicKey(modifiedBinary);
                byte[] pubKeyBytes = new BigInteger(compressedPubkey, 16).toByteArray();
                String pubKeyHash160 = Secp256k1.hash160(pubKeyBytes);
                
                if (pubKeyHash160.startsWith("e0b8a2")) {
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
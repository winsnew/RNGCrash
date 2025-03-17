package utils;
import ecc.Secp256k1;
import java.math.BigInteger;
import org.bouncycastle.math.ec.ECPoint;

public class PkToPb {
    public static String convertToCmpPublicKey(String binaryPrivateKey) {
        BigInteger privateKey = new BigInteger(binaryPrivateKey, 2);

        ECPoint publicKey = Secp256k1.getPublicKey(privateKey);
        return Secp256k1.toCompressedPublicKey(publicKey);
    }
}

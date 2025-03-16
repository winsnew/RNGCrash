import java.math.BigInteger;
import java.security.SecureRandom;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import java.security.KeyFactory;
import java.security.Security;
import java.security.spec.ECPoint;
import java.security.spec.ECParameterSpec;
import java.security.spec.ECGenParameterSpec;
import java.security.interfaces.ECPublicKey;
import java.security.spec.ECPublicKeySpec;

public class CompressedPublicKeyGenerator {
    public static void main(String[] args) throws Exception {
        Security.addProvider(new BouncyCastleProvider());
        
        SecureRandom random = new SecureRandom();
        BigInteger privateKey = new BigInteger(256, random);
        
        ECGenParameterSpec ecSpec = new ECGenParameterSpec("secp256k1");
        KeyFactory keyFactory = KeyFactory.getInstance("EC", "BC");
        ECParameterSpec ecParameterSpec = keyFactory.getKeySpec(
                keyFactory.generatePublic(new ECPublicKeySpec(new ECPoint(BigInteger.ZERO, BigInteger.ZERO), null)), 
                ECParameterSpec.class);

        ECPoint publicPoint = ecParameterSpec.getGenerator().multiply(privateKey);
        ECPublicKeySpec pubKeySpec = new ECPublicKeySpec(publicPoint, ecParameterSpec);
        ECPublicKey publicKey = (ECPublicKey) keyFactory.generatePublic(pubKeySpec);
        
        String compressedPublicKey = compressPublicKey(publicKey);
        System.out.println("Compressed Public Key: " + compressedPublicKey);
    }

    private static String compressPublicKey(ECPublicKey publicKey) {
        BigInteger x = publicKey.getW().getAffineX();
        BigInteger y = publicKey.getW().getAffineY();
        
        return (y.testBit(0) ? "03" : "02") + x.toString(16);
    }
}

package ecc;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import org.bouncycastle.math.ec.ECPoint;
import org.bouncycastle.util.encoders.Hex;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import java.security.Security;


public class Secp256k1 {
    static {
        Security.addProvider(new BouncyCastleProvider());
    }

    private static final BigInteger P = new BigInteger("FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFEFFFFFC2F", 16);
    private static final BigInteger A = BigInteger.ZERO;
    private static final BigInteger B = new BigInteger("7", 16);
    private static final BigInteger GX = new BigInteger("79BE667EF9DCBBAC55A06295CE870B07029BFCDB2DCE28D959F2815B16F81798", 16);
    private static final BigInteger GY = new BigInteger("483ADA7726A3C4655DA4FBFC0E1108A8FD17B448A68554199C47D08FFB10D4B8", 16);
    private static final BigInteger N = new BigInteger("FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFEBAAEDCE6AF48A03BBFD25E8CD0364141", 16);

    private static final org.bouncycastle.math.ec.ECCurve.Fp curve = 
        new org.bouncycastle.math.ec.ECCurve.Fp(P,A,B);

        private static final ECPoint G = curve.decodePoint(Hex.decode("0479BE667EF9DCBBAC55A06295CE870B07029BFCDB2DCE28D959F2815B16F81798"
 + "483ADA7726A3C4655DA4FBFC0E1108A8FD17B448A68554199C47D08FFB10D4B8"));

        public static ECPoint getPublicKey(BigInteger privateKey) {
            if (privateKey.compareTo(BigInteger.ONE) < 0 || privateKey.compareTo(N) >= 0) {
                privateKey = privateKey.mod(N);
            }
            return G.multiply(privateKey);
        }
    
        public static String toCompressedPublicKey(ECPoint pubKey) {
            ECPoint normalizedPoint = pubKey.normalize(); 
            BigInteger x = normalizedPoint.getAffineXCoord().toBigInteger();
            BigInteger y = normalizedPoint.getAffineYCoord().toBigInteger();
            return (y.testBit(0) ? "03" : "02") + x.toString(16);
        }
    
        public static String hash160(byte[] data) throws NoSuchAlgorithmException {
            MessageDigest sha256 = MessageDigest.getInstance("SHA-256");
            byte[] sha256Hash = sha256.digest(data);
    
            MessageDigest ripemd160 = MessageDigest.getInstance("RIPEMD160");
            return bytesToHex(ripemd160.digest(sha256Hash));
        }
    
        public static String bytesToHex(byte[] bytes) {
            StringBuilder sb = new StringBuilder();
            for (byte b : bytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        }
}

package Cryptology;
import java.math.BigInteger;
import java.security.*;

import javax.crypto.spec.DHParameterSpec;
import javax.crypto.spec.DHPublicKeySpec;

/**
 * Diffie-Hellman protocol is used for key exchange over an unsecure channel.
 * This algorithm guarantees that even if the channel is being eavesdropped,
 * the attack will not be able to reconstruct the exchanged key.
 * Key can then be used to symmetric cryptography.
 *
 * Algorithm is based on associativeness of the power operation:
 *      (A^B)^C = (A^C)^B
 *
 * Communication is:
 *  1. Participants publically agree on a common modulo M and base Z
 *  2. Each participant generates their private key -- an exponent coprime to M
 *  3. Each participant powers the base to the private exponent a passes the result to the next participant
 *  4. The algorithm terminates, when each of the original bases is processes by each participant.
 *
 *  @author kkmonlee
 */
public class DiffieHellman {

    public final static int pValue = 47;

    public final static int gValue = 71;

    public final static int XaValue = 9;

    public final static int XbValue = 14;

    public static void main(String[] args) throws Exception {
        BigInteger p = new BigInteger(Integer.toString(pValue));
        BigInteger g = new BigInteger(Integer.toString(gValue));
        BigInteger Xa = new BigInteger(Integer.toString(XaValue));
        BigInteger Xb = new BigInteger(Integer.toString(XbValue));

        createKey();

        int bitLength = 512; // 512 bits
        SecureRandom rnd = new SecureRandom();
        p = BigInteger.probablePrime(bitLength, rnd);
        g = BigInteger.probablePrime(bitLength, rnd);

        createSpecificKey(p, g);
    }

    public static void createKey() throws Exception {
        KeyPairGenerator kpg = KeyPairGenerator.getInstance("DiffieHellman");

        kpg.initialize(512);
        KeyPair kp = kpg.generateKeyPair();
        KeyFactory keyFactory = KeyFactory.getInstance("DiffieHellman");

        DHPublicKeySpec keySpec = (DHPublicKeySpec) keyFactory.getKeySpec(kp.getPublic(), DHPublicKeySpec.class);
    }

    public static void createSpecificKey(BigInteger p, BigInteger g) throws Exception {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("DiffieHellman");

        DHParameterSpec param = new DHParameterSpec(p, g);
        keyPairGenerator.initialize(param);
        KeyPair kp = keyPairGenerator.generateKeyPair();

        KeyFactory keyFactory = KeyFactory.getInstance("DiffieHellman");

        DHPublicKeySpec keySpec = (DHPublicKeySpec) keyFactory.getKeySpec(kp.getPublic(), DHPublicKeySpec.class);
    }

}

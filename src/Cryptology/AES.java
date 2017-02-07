package Cryptology;
import java.security.*;
import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;

import sun.misc.BASE64Encoder;

public class AES {
    public static void main(String[] args) {

        String strDataToEncrypt = new String();
        String strCipherText = new String();
        String strDecryptedText = new String();

        try {
            /**
             * Generate an AES key using KeyGenerator
             * Initialize the key size to 128-bits (16 bytes)
             */
            KeyGenerator keyGen = KeyGenerator.getInstance("AES");
            keyGen.init(128);
            SecretKey secretKey = keyGen.generateKey();

            /**
             * Generate an Initialization Vector using:
             * (i)  SecureRandom to generate random bits.
             *      Size of the Vector matches the blocksize of the cipher (128-bits)
             * (ii) Construct appropriate parameter specification objects for the data
             *      to pass through cipher's init() method
             */
            final int AES_KEYLENGTH = 128;
            byte[] iv = new byte[AES_KEYLENGTH / 8];
            SecureRandom prng = new SecureRandom();
            prng.nextBytes(iv);

            /**
             * Create a cipher by specifying:
             * a. Algorithm name
             * b. Mode
             * c. Padding
             *
             * In this case it is AES, CBC, PKCS5/7
             */
            Cipher aesCipherEnc = Cipher.getInstance("AES/CBC/PKCS7PADDING");

            /**
             * Initialize the cipher for encryption
             */
            aesCipherEnc.init(Cipher.ENCRYPT_MODE, secretKey, new IvParameterSpec(iv));

            /**
             * Encrypt the data:
             * a. Declare data
             * b. Convert input text to bytes
             * c. Encrypt bytes using doFinal
             */
            strDataToEncrypt = "This text is to be encrypted.";
            byte[] byteDataToEncrypt = strDataToEncrypt.getBytes();
            byte[] byteCipherText = aesCipherEnc.doFinal(byteDataToEncrypt);

            strCipherText = new BASE64Encoder().encode(byteCipherText);
            System.out.println("Cipher text is: " + strCipherText);

            /**
             * Decrypt the data:
             * a. Initialize new instance of cipher
             * b. Decrypt the cipher bytes using doFinal
             */
            Cipher aesCipherDec = Cipher.getInstance("AES/CBC/PKCS7PADDING");

            aesCipherDec.init(Cipher.DECRYPT_MODE, secretKey, new IvParameterSpec(iv));
            byte[] byteDecryptedText = aesCipherDec.doFinal(byteCipherText);
            strDecryptedText = new String(byteDecryptedText);
            System.out.println("Decrypted text is: " + strDecryptedText);

        } catch (NoSuchAlgorithmException noSuchAlg) {
            System.out.println("No such algorithm exists. " + noSuchAlg);
        } catch (NoSuchPaddingException noSuchPad) {
            System.out.println(" No Such Padding exists " + noSuchPad);
        } catch (InvalidKeyException invalidKey) {
            System.out.println(" Invalid Key " + invalidKey);
        } catch (BadPaddingException badPadding) {
            System.out.println(" Bad Padding " + badPadding);
        } catch (IllegalBlockSizeException illegalBlockSize) {
            System.out.println(" Illegal Block Size " + illegalBlockSize);
        } catch (InvalidAlgorithmParameterException invalidParam) {
            System.out.println(" Invalid Parameter " + invalidParam);
        }
    }
}

package Cryptology;
import javax.swing.plaf.basic.BasicSplitPaneUI;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
/**
 * Vernam cipher is a symmetrical stream cipher, in which the open text is XORed with a prearranged random input
 * (noise) of the same length
 *
 * @author kkmonlee
 */

enum Bit {
    ZERO, ONE;

    Bit xor(Bit bit) {
        if (this == bit) {
            return Bit.ZERO;
        }
        return Bit.ONE;
    }
};

public class VernamCipher {

    private VernamCipher() {
        init();
    }

    private void init() {

    }

    public static Bit[] encrypt(Bit[] key, Bit[] plainText) {
        Bit[] cipherText = new Bit[plainText.length];

        for (int i = 0; i < plainText.length; i++) {
            cipherText[i] = plainText[i].xor(key[i]);
        }

        return cipherText;
    }

    public static Bit[] decrypt(Bit[] key, Bit[] cipherText) {
        Bit[] plainText = new Bit[cipherText.length];

        for (int i = 0; i < plainText.length; i++) {
            plainText[i] = cipherText[i].xor(key[i]);
        }

        return plainText;
    }

    public static String Bits2String(Bit[] input) {
        String output = "";

        for (int i = 0; i < input.length; i++) {
            output += input[i] == Bit.ZERO ? "0" : "1";
        }

        return output;
    }

    public static void writeBitsToFile(String filename, Bit[] export) {
        try {
            FileOutputStream inputStream = new FileOutputStream(new File(filename));

            for (int i = 0; i < export.length; i++) {
                inputStream.write(String.valueOf(export[i] == Bit.ZERO ? "0" : "1").getBytes());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Bit[] readBitsFromFile(String filename) {
        int ch = -1;

        StringBuffer inputStr = new StringBuffer("");
        try {
            FileInputStream inputStream = new FileInputStream(new File(filename));

            while ((ch = inputStream.read()) != -1) inputStr.append((char) ch);
            inputStream.close();

            if (inputStr.length() > 0) {
                Bit[] inputBits = new Bit[inputStr.length()];
                for (int i = 0; i < inputStr.length(); i++) {
                    inputBits[i] = inputStr.charAt(i) == '1' ? Bit.ONE : Bit.ZERO;
                }

                return inputBits;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static void main(String[] args) {
        String operation = null;
        String key = null;
        String input = null;
        String output = null;
        Bit[] inputBits = null, keyStream = null;

        if (args.length == 4) {
            if ((operation = args[0]) == "e" || (operation = args[0]) == "d") {
                System.err.println("Operation argument" + " must be e for encrypt or d for decrypt.");

                System.exit(1);
            }

            key = args[1];

            input = args[2];
            if (!(new File(input)).exists()) {
                System.err.println("Input argument" + " input file must exist.");
                System.exit(1);
            }

            output = args[3];
            if ((new File(output)) == null) {
                System.err.println("Output argument" + " output file already exists");
                System.exit(1);
            }
        }
        else {
            System.err.println("");
            System.exit(1);
        }

        try {
            /**
             * Read in input
             */
            inputBits = readBitsFromFile(input);
                if (inputBits == null) {
                    System.err.println("There was no input in the file. ");
                    System.exit(1);
                }
                /**
                 * Generate random key
                 */
            keyStream = readBitsFromFile(key);
            // Checks if there are bits in the file
            if (keyStream == null) {
                // no key bits found
                keyStream = new Bit[inputBits.length];
                SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
                final int STREAM_SIZE = inputBits.length;

                for (int i = 0; i < STREAM_SIZE; i++) {
                    keyStream[i] = (random.nextBoolean()) ? Bit.ZERO : Bit.ONE;
                }
                // save key bits to file
                writeBitsToFile(key, keyStream);
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        Bit[] result = null;
        int case_op = (operation.equals("e") ? 1 : 2);
        switch (case_op) {
            case 1: // encrypt
            {
                Bit[] cipherText = result;
                result = VernamCipher.encrypt(keyStream, inputBits);

                System.out.println("Key: " + VernamCipher.Bits2String(keyStream));
                System.out.println("Plaintext: " + VernamCipher.Bits2String(inputBits));
                System.out.println("Ciphertext: " + VernamCipher.Bits2String(cipherText));
                break;
            }
            case 2: // decrypt
            {
                Bit[] plainText = result;
                result = VernamCipher.decrypt(keyStream, inputBits);

                System.out.println("Key: "+ VernamCipher.Bits2String(keyStream));
                System.out.println("CipherText: "+ VernamCipher.Bits2String(inputBits));
                System.out.println("PlainText: "+ VernamCipher.Bits2String(plainText));
                break;
            }
        }

        /**
         * Write to output file
         */

        writeBitsToFile(output, result);
    }


}

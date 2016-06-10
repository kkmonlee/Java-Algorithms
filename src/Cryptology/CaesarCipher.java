package Cryptology;

/**
 * Caesar cipher replaces every character of the open text with another character by a fixed number of positions
 * further down the alphabet
 *
 * @author kkmonlee
 *
 * Formula:
 *  C_i = (T_i + k) (mod m)
 *  C_i = ith character of the closed text
 *  T_i = ith character of the open text
 *  k = shift
 *  m = length of the alphabet
 */
public class CaesarCipher {
    /**
     * Shift constant
     */
    public static final int SHIFT = 3;

    /**
     * Encrypts using Caesar cipher
     * @param s string containing only uppercase characters
     * @return encrypted string (closed text)
     */
    public static String encrypt(String s) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) < 65 || s.charAt(i) > 90) {
                throw new IllegalArgumentException("" + "String does not contain only uppercase characters.");
            }
            // Modularly add shift
            char encrypted = s.charAt(i) + SHIFT > 90 ? (char) ((s.charAt(i) + SHIFT) - 26) : (char) (s.charAt(i) + SHIFT);
            builder.append(encrypted);
        }

        return builder.toString();
    }
}

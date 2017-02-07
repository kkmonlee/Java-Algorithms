package Mathematics;

import java.util.HashMap;
import java.util.Map;

/**
 * Calculates the Shannon entropy H of any given input string.
 * Given random discrete variable X which is a string of N (total characters)
 * consisting of n different characters
 *
 * H_2(X) = -Sum_(i = 1)^n count_i / N log_2 (count_i / N)
 *
 * where count_i is the count of character n_i.
 *
 * Help from: http://worrydream.com/refs/Shannon%20-%20A%20Mathematical%20Theory%20of%20Communication.pdf
 *
 */
public class ShannonEntropy {

    @SuppressWarnings("boxing")
    public static double getEntropy(String s) {

        int n = 0;
        Map<Character, Integer> occ = new HashMap<>();

        for (int i = 0; i < s.length(); i++) {
            char ix = s.charAt(i);
            if (occ.containsKey(ix)) {
                occ.put(ix, occ.get(ix) + 1);
            } else {
                occ.put(ix, 1);
            }
            n++;
        }

        double e = 0.0;
        for (Map.Entry<Character, Integer> entry : occ.entrySet()) {
            //char ix = entry.getKey();
            double p = (double) entry.getValue() / n;
            e += p * log2(p);
        }

        return -e;
    }

    private static double log2(double a) {
        return Math.log(a) / Math.log(2);
    }

    public static void main(String[] args) {
        String[] str = {
                "1231234124",
                "asdqwdqw3e2",
                "aaaaaaUJJJJJJJJJ",
                "kkmonlee",
                "Atul",
                "hf-9wdh-9wehf-9"
        };

        for (String ss : str) {
            double entropy = getEntropy(ss);
            System.out.printf("Shannon entropy of %40s: %.12f%n", "\"" + ss + "\"", entropy);
        }

        return;
    }
}

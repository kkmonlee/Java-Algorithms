/**
 * Created by Atul Anand Sinha on 31 May 2016.
 */

public class Problem142 {
    boolean[] isSquare;

    String run() {
        int sumLimit = 10;
        // Raise limit until sum is found
        while (true) {
            isSquare = new boolean[sumLimit];
            for (int i = 0; i * i < sumLimit; i++) {
                isSquare[i * i] = true;
            }

            int sum = findSum(sumLimit);
            if (sum != -1) {
                sum = sumLimit;
                break;
            }
            sumLimit *= 10;
        }

        // Lower limit until no sum is found
        while (true) {
            int sum = findSum(sumLimit);
            if (sum == -1)
                return Integer.toString(sumLimit);
            sumLimit = sum;
        }
    }

    int findSum(int limit) {
        for (int a = 1; a * a < limit; a++) {
            for (int b = a - 1; b > 0; b--) {
                if ((a + b) % 2 != 0)  // Need them to be both odd or both even so that we get integers for x and y
                    continue;
                int x = (a * a + b * b) / 2;
                int y = (a * a - b * b) / 2;
                if (x + y + 1 >= limit)  // Because z >= 1
                    continue;

                int zlimit = Math.min(y, limit - x - y);
                double c = Math.sqrt(y) + 1;
                for (int d = intValue(c);d * d - y < zlimit; d++) {
                    int z = d * d - y;
                    if (isSquare[x + z] && isSquare[x - z] && isSquare[y - z])
                        return x + y + z;
                }
            }
        }
        return -1;
    }

    int intValue(double num) {
        Double d = num;
        return d.intValue();
    }
}

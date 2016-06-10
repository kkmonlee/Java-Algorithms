package Others;

import java.math.BigInteger;

/**
 * Created by Atul Anand Sinha on 05 June 2016.
 */
public class Ackerman {

    public static BigInteger ack(BigInteger m, BigInteger n) {
        return m.equals(BigInteger.ZERO)
                ? n.add(BigInteger.ONE)
                : ack(m.subtract(BigInteger.ONE),
                n.equals(BigInteger.ZERO) ? BigInteger.ONE : ack(m, n.subtract(BigInteger.ONE)));
    }

}

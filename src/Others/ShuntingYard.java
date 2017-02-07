package Others; /**
 * Created by Atul Anand Sinha on 05 June 2016.
 */
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class ShuntingYard {

    private enum Operator {
        ADD(1), SUBTRACT(2), MULTIPLY(3), DIVIDE(4);
        final int precedence;

        Operator(int p) {
            precedence = p;
        }
    }

    private static Map<String, Operator> ops = new HashMap<String, Operator>() {{
        put("+", Operator.ADD);
        put("-", Operator.SUBTRACT);
        put("*", Operator.MULTIPLY);
        put("/", Operator.DIVIDE);
    }};

    private static boolean isHigherPrec(String op, String sub) {
        return (ops.containsKey(sub)) && ops.get(sub).precedence >= ops.get(op).precedence;
    }

    @NotNull
    public static String postFix(String infix) {
        StringBuilder output = new StringBuilder();
        Deque<String> stack = new LinkedList<>();

        for (String token : infix.split("\\s")) {

            // operator
            if (ops.containsKey(token)) {
                while (!stack.isEmpty() && isHigherPrec(token, stack.peek())) {
                    output.append(stack.pop()).append(' ');
                }
                stack.push(token);
            }

            // opening bracket
            else if (token.equals("(")) {
                stack.push(token);
            }

            // closing bracket
            else if (token.equals(")")) {
                while (!stack.peek().equals("(")) {
                    output.append(stack.pop()).append(' ');
                }
                stack.pop();
            }

            // digit
            else {
                output.append(token).append(' ');
            }
        }

        while (!stack.isEmpty()) {
            output.append(stack.pop()).append(' ');
        }
        return output.toString();
    }

}

package oop.project.cli;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Map;
import java.util.Optional;

public class Scenarios {

    /**
     * Parses the arguments of a command and calls on the required method. This method is designed to allow for addition, subtraction,
     * square root calculation, command calculation. Commands are expected in Unix form
     *
     * @param command The full command line input to be parsed, excluding null or empty.
     * @return A {@code Map<String, Object>} with the parsed arguments and their respective values.
     * @throws IllegalArgumentException if the command is unknown or improperly formatted.
     */
    public static Map<String, Object> parse(String command) {
        var split = command.split(" ", 2);
        var base = split[0];
        var arguments = split.length == 2 ? split[1] : "";
        return switch (base) {
            case "add" -> add(arguments);
            case "sub" -> sub(arguments);
            case "sqrt" -> sqrt(arguments);
            case "calc" -> calc(arguments);
            case "date" -> date(arguments);
            default -> throw new IllegalArgumentException("Unknown command.");
        };
    }

    /**
     * Designed to handle the add command. Command requires two space seperated int arguments
     * and then adds them together.
     *  - {@code left: <your integer type>}
     *  - {@code right: <your integer type>}
     *  @param arguments two ints space seperated in string form.
     *  @return A {@code Map<String, Object>} with keys "left" and "right" representing the operands,
     *    and their sum under the key "result".
     *  @throws IllegalArgumentException if parse fails or incorrect amount of args.
     */
    private static Map<String, Object> add(String arguments) {
        int left = 0; //or BigInteger, etc.
        int right = 0;
        String[] elements = arguments.split(" ");
        if(elements.length == 2) {
            left = Integer.parseInt(elements[0]);
            right = Integer.parseInt(elements[1]);
            return Map.of("left", left, "right", right);
        }
        else {
            throw new IllegalArgumentException("Need two arguments");
        }
    }

    /**
     * Designed to handle the sub command. Command takes alteast and atmost two arguments. If only given
     * one, the left defaults to 0.0.
     * @param arguments A string with named arguments "--left" and "--right" with their values.
     * @return A {@code Map<String, Object>} with keys "left" and "right" and their values.
     *         If "left" is not specified, it's not included in the map.
     * @throws IllegalArgumentException if atleast one argument is not provided or parsing fails due to invalid input.
     */
    static Map<String, Object> sub(String arguments) {
        if (arguments == null || arguments.trim().isEmpty()) {
            throw new IllegalArgumentException("Arguments cannot be null or empty");
        }

        Optional<Double> left = Optional.empty();
        double right = 0.0;
        boolean rightSet = false;

        String[] elements = arguments.trim().split(" ");
        int i = 0;
        while (i < elements.length) {
            if (elements[i].equals("--left") && i + 1 < elements.length) {
                try {
                    left = Optional.of(Double.parseDouble(elements[i + 1]));
                    i += 2;
                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException("Left value is not a number");
                }
            } else if (elements[i].equals("--right") && i + 1 < elements.length) {
                try {
                    right = Double.parseDouble(elements[i + 1]);
                    rightSet = true;
                    i += 2;
                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException("Right value is not a number");
                }
            } else {
                throw new IllegalArgumentException("Invalid argument or missing value: " + elements[i]);
            }
        }

        if (!rightSet) {
            throw new IllegalArgumentException("Right operand is required but not provided");
        }

        if(!left.isEmpty()) {
            return Map.of("left", left.orElse(null), "right", right);
        }

        return Map.of("left", left, "right", right);
    }

    /**
     * This method is designed to handle the sqrt command. Command takes in a singular non-negative integer to be used as the radicand and takes it square root.
     * @param arguments Non-negative int as a string to be used as radicand.
     * @return A {@code Map<String, Object>} with a key "number" associated with the input number
     *  and "result" for the square root value.
     * @throws IllegalArgumentException if arg string contains negative int or is not int.
     *
     */
    static Map<String, Object> sqrt(String arguments) {
        int number = Integer.parseInt(arguments);
        if(number >= 0) {
            return Map.of("number", number);
        }
        else {
            throw new IllegalArgumentException("The number passed in is negative");
        }
    }

    /**
     * This method handles the calc command. Takes in subcommands/ nested commands to be used for more calculations.
     * @param arguments A string containing a supported subcommand
     * @return A {@code Map<String, Object>} with the key "subcommand" associated with the recognized operation.
     * @throws IllegalArgumentException if arg contains any subcommand not recognized/supported.
     */
    static Map<String, Object> calc(String arguments) {
        String subcommand = "";
        if(arguments.equals("add") || arguments.equals("div") || arguments.equals("sqrt") || arguments.equals("sub")) {
            subcommand = arguments;
            return Map.of("subcommand", subcommand);
        }
        else {
            throw new IllegalArgumentException("Not any of the functions");
        }
    }

    /**
     * The method designed to handle the data metod, takes in a string in date format to return a {@code LocalDate} object. The input string should
     * be in the 'yyyy-mm-dd' format.
     *
     * @param arguments A string representing a date in 'yyyy-mm-dd' format.
     * @return A {@code Map<String, Object>} with a key "date" associated with the parsed {@code LocalDate}.
     * @throws DateTimeParseException if the formating of the date string is wrong.
     */
    static Map<String, Object> date(String arguments) {
        LocalDate date = LocalDate.parse(arguments);
        return Map.of("date", date);
    }


}

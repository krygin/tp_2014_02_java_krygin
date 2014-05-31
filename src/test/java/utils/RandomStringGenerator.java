package utils;

import java.util.Random;

/**
 * Created by Ivan on 29.05.2014 in 21:50.
 */
public class RandomStringGenerator {
    private final static String POSSIBLE_SYMBOLS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
    private static Random random = new Random();

    public static String getString(int length) {
        StringBuilder string = new StringBuilder();
        for (int i = 0; i < length; i++) {
            string.append(POSSIBLE_SYMBOLS.charAt(random.nextInt(POSSIBLE_SYMBOLS.length())));
        }
        return string.toString();
    }
}

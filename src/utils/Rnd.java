package utils;
import java.util.Random;

public class Rnd {
    private static final Random random = new Random();
    public static int generateRandomNumber(int min, int max) {
        return random.nextInt((max - min) + 1) + min;
    }
}

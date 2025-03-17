import utils.Rnd;

public class Main {
    public static void main(String[] args) {
        int rndNumber = Rnd.generateRandomNumber(1, 100);
        System.out.println("Random: " + rndNumber);
    }
}
import java.util.Arrays;
import proc.LuckRnd;
import proc.bins;

public class Main {
    public static void main(String[] args) {
        if (args.length > 1 && args[0].equals("-m")) {
            switch (args[1].toLowerCase()) {
                case "luckrnd":
                    LuckRnd.main(Arrays.copyOfRange(args, 2, args.length));
                    break;
                case "bins":
                    bins.bin(Arrays.copyOfRange(args, 2, args.length));
                    break;
                default:
                    System.out.println("Unknown module: " + args[1]);
            }
        } else {
            System.out.println("Usage: java -cp \"libs/*; bin\" Main -m [luckrnd|bins]");
        }
    }
}
package proc;

import java.util.HashSet;
import java.util.Random;

public class BinMod {
    public static String flipBits(String pkbiner, int flipCount) {
        String fixedPrefix = "1111";
        String pkbinerWithoutPrefix = pkbiner.substring(fixedPrefix.length());
        
        if (pkbinerWithoutPrefix.length() < flipCount) {
            throw new IllegalArgumentException("Jumlah bit untuk diubah lebih besar dari panjang string tanpa prefix");
        }
        
        char[] bitArray = pkbinerWithoutPrefix.toCharArray();
        Random rand = new Random();
        HashSet<Integer> flippedIndices = new HashSet<>();
        
        while (flippedIndices.size() < flipCount) {
            int index = rand.nextInt(bitArray.length);
            if (bitArray[index] == '1' && !flippedIndices.contains(index)) {
                bitArray[index] = '0';
                flippedIndices.add(index);
            }
        }
        
        return fixedPrefix + new String(bitArray);
    }
}

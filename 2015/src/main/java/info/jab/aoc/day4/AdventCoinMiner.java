package info.jab.aoc.day4;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;
import java.util.stream.Stream;

public class AdventCoinMiner {

    public int findLowestNumber(String secretKey, Boolean isPart1) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            
            String prefix = isPart1 ? "00000" : "000000";
            return Stream.iterate(1, n -> n + 1)
                .filter(number -> {
                    String input = secretKey + number;
                    md.reset();
                    md.update(input.getBytes());
                    String hash = HexFormat.of().formatHex(md.digest());
                    return hash.startsWith(prefix);
                })
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Not found solution"));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("MD5 algorithm not available: " + e.getMessage(), e);
        }
    }
}

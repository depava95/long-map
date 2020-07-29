package de.comparus.opensource.longmap;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

public class Main {

    /** --jdk1.8.0_231--
     * LongMap writing time: 38
     * HashMap writing time: 53
     * LongMap reading time: 14
     * HashMap reading time: 9
     * LongMap containsKey time: 13
     * HashMap containsKey time: 9
     * HashMap containsValue time: 8597
     * LongMap containsValue time: 1490
     * LongMap removing time: 10
     * HashMap removing time: 12
    * */

    public static void main(String[] args) {

        LongMap<String> longMap = new LongMapImpl<>();
        Map<Long, String> hashMap = new HashMap<>();

        Random random = new Random();
        List<Long> randomNumbers = LongStream.generate(random::nextLong)
                .limit(100_000)
                .boxed()
                .collect(Collectors.toList());

        long longMapStartWrite = System.currentTimeMillis();
        for (Long randomNumber : randomNumbers) {
            longMap.put(randomNumber, String.valueOf(randomNumber));
        }
        System.out.println("LongMap writing time: " + (System.currentTimeMillis() - longMapStartWrite));

        long hashMapStartWrite = System.currentTimeMillis();
        for (Long randomNumber : randomNumbers) {
            hashMap.put(randomNumber, String.valueOf(randomNumber));
        }
        System.out.println("HashMap writing time: " + (System.currentTimeMillis() - hashMapStartWrite));

        long longMapStartRead = System.currentTimeMillis();
        for (Long randomNumber : randomNumbers) {
            longMap.get(randomNumber);
        }
        System.out.println("LongMap reading time: " + (System.currentTimeMillis() - longMapStartRead));

        long hashMapStartRead = System.currentTimeMillis();
        for (Long randomNumber : randomNumbers) {
            hashMap.get(randomNumber);
        }
        System.out.println("HashMap reading time: " + (System.currentTimeMillis() - hashMapStartRead));

        long longMapStartContainsKey = System.currentTimeMillis();
        for (Long randomNumber : randomNumbers) {
            longMap.containsKey(randomNumber);
        }
        System.out.println("LongMap containsKey time: " + (System.currentTimeMillis() - longMapStartContainsKey));

        long hashMapStartContainsKey = System.currentTimeMillis();
        for (Long randomNumber : randomNumbers) {
            hashMap.containsKey(randomNumber);
        }
        System.out.println("HashMap containsKey time: " + (System.currentTimeMillis() - hashMapStartContainsKey));

        long longMapStartContainsValue = System.currentTimeMillis();
        for (Long randomNumber : randomNumbers.stream().limit(10_000).collect(Collectors.toList())) {
            longMap.containsValue(String.valueOf(randomNumber));
        }
        System.out.println("LongMap containsValue time: " + (System.currentTimeMillis() - longMapStartContainsValue));

        long hashMapStartContainsValue = System.currentTimeMillis();
        for (Long randomNumber : randomNumbers.stream().limit(10_000).collect(Collectors.toList())) {
            hashMap.containsValue(String.valueOf(randomNumber));
        }
        System.out.println("HashMap containsValue time: " + (System.currentTimeMillis() - hashMapStartContainsValue));

        long longMapStartRemove = System.currentTimeMillis();
        for (Long randomNumber : randomNumbers) {
            longMap.remove(randomNumber);
        }
        System.out.println("LongMap removing time: " + (System.currentTimeMillis() - longMapStartRemove));

        long hashMapStartRemove = System.currentTimeMillis();
        for (Long randomNumber : randomNumbers) {
            hashMap.remove(randomNumber);
        }
        System.out.println("HashMap removing time: " + (System.currentTimeMillis() - hashMapStartRemove));
    }
}

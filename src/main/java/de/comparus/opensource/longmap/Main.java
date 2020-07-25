package de.comparus.opensource.longmap;

import java.util.HashMap;
import java.util.Map;

public class Main {

    public static void main(String[] args) {
        LongMap<String> longMap = new LongMapImpl<>();
        Map<Long, String> hashMap = new HashMap<>();

        long longMapStartWrite = System.currentTimeMillis();
        for (long i = 0; i < 100_000L; i++) {
            longMap.put(i, String.valueOf(i));
        }
        System.out.println("LongMap writing time: " + (System.currentTimeMillis() - longMapStartWrite));

        long hashMapStartWrite = System.currentTimeMillis();
        for (long i = 0; i < 100_000L; i++) {
            hashMap.put(i, String.valueOf(i));
        }
        System.out.println("HashMap writing time: " + (System.currentTimeMillis() - hashMapStartWrite));

        long longMapStartRead = System.currentTimeMillis();
        for (long i = 0; i < 100_000L; i++) {
            longMap.get(i);
        }
        System.out.println("LongMap reading time: " + (System.currentTimeMillis() - longMapStartRead));

        long hashMapStartRead = System.currentTimeMillis();
        for (long i = 0; i < 100_000L; i++) {
            hashMap.get(i);
        }
        System.out.println("HashMap reading time: " + (System.currentTimeMillis() - hashMapStartRead));

        long longMapStartContains = System.currentTimeMillis();
        for (long i = 0; i < 10_000L; i++) {
            longMap.containsKey(i);
            longMap.containsValue(String.valueOf(i));
        }
        System.out.println("LongMap containing(key/value) time: " + (System.currentTimeMillis() - longMapStartContains));

        long hashMapStartContains = System.currentTimeMillis();
        for (long i = 0; i < 10_000L; i++) {
            hashMap.containsKey(i);
            hashMap.containsValue(String.valueOf(i));
        }
        System.out.println("HashMap containing(key/value) time: " + (System.currentTimeMillis() - hashMapStartContains));
    }
}

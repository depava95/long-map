package de.comparus.opensource.longmap;

import java.util.HashMap;
import java.util.Map;

public class Main {

    public static void main(String[] args) {
        LongMap<String> longMap = new LongMapImpl<>(String.class);
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

        long longMapStartContainsKey = System.currentTimeMillis();
        for (long i = 0; i < 10_000L; i++) {
            longMap.containsKey(i);
        }
        System.out.println("LongMap containsKey time: " + (System.currentTimeMillis() - longMapStartContainsKey));

        long hashMapStartContainsKey = System.currentTimeMillis();
        for (long i = 0; i < 10_000L; i++) {
            hashMap.containsKey(i);
        }
        System.out.println("HashMap containsKey time: " + (System.currentTimeMillis() - hashMapStartContainsKey));

        long longMapStartContainsValue = System.currentTimeMillis();
        for (long i = 0; i < 10_000L; i++) {
            longMap.containsValue(String.valueOf(i));
        }
        System.out.println("LongMap containsValue time: " + (System.currentTimeMillis() - longMapStartContainsValue));

        long hashMapStartContainsValue = System.currentTimeMillis();
        for (long i = 0; i < 10_000L; i++) {
            hashMap.containsValue(String.valueOf(i));
        }
        System.out.println("HashMap containsValue time: " + (System.currentTimeMillis() - hashMapStartContainsValue));

    }
}

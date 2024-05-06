package me.shubhamjain.codesamples.collections.map;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ConcurrentHashMapInternals {

    public static void main(String[] args) {
        Map<String, Integer> map = new ConcurrentHashMap<>();
        // Concurrent HashMap doesn't allow neither null keys nor null values while hashmap does allow null keys and values
        map.put("Shubham", 4);
        map.put("SK", 6);
        System.out.println(map);

    }
}

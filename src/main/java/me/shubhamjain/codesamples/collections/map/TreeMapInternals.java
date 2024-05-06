package me.shubhamjain.codesamples.collections.map;

import java.util.Map;
import java.util.TreeMap;

public class TreeMapInternals {
    public static void main(String[] args) {

        Map<Integer, Integer> treeMap = new TreeMap<>();
        treeMap.put(1,8);
        treeMap.put(9,8);
        treeMap.put(6,2);
        treeMap.put(2,8);
        treeMap.keySet().stream().forEach(System.out::println);
    }
}

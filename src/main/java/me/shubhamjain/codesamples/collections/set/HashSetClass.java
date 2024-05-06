package me.shubhamjain.codesamples.collections.set;

import java.util.HashSet;
import java.util.Set;

public class HashSetClass {

    class Constants {
        public static final Set<String> s = Set.of("Shubham", "RJ");

        public static Set<String> getSet() {
            return Set.of("Shubham", "RJ");
        }
    }

    public static void main(String[] args) {
        Set<Integer> s = new HashSet<>();
        Set<String> s1 = Constants.s;
        Set<String> s2 = Constants.s;
        Set<String> s3 = Constants.getSet();
        Set<String> s4 = Constants.getSet();
        System.out.println(s1 == s2);
        System.out.println(s3 == s4);
    }
}

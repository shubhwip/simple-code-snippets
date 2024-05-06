package me.shubhamjain.codesamples.constants;

public class LogsConstants {
    public static final boolean DEBUG_ENABLED = Boolean.parseBoolean(System.getenv().getOrDefault("DEBUG_ENABLED", "false"));
    public static boolean isDebugEnabled() {
        return DEBUG_ENABLED;
    }
}

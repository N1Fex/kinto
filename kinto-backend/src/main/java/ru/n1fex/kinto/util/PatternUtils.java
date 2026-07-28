package ru.n1fex.kinto.util;

public class PatternUtils {
    public static final String PASSWORD_REGEXP = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d).{8,}$";
    public static final String USERNAME_REGEXP = "^[A-Za-z0-9_]{3,64}$";
}

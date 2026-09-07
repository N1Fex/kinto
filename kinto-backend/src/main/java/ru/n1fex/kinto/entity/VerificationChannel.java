package ru.n1fex.kinto.entity;

public enum VerificationChannel {
    EMAIL("email"),
    PHONE("phone");

    private final String channel;
    VerificationChannel(String channel) {
        this.channel = channel;
    }

    @Override
    public String toString() {
        return channel;
    }
}

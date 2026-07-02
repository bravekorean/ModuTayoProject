package com.group.express.config.JWT;

public enum expiredTime {
    ACCESS_TOKEN_EXPIRE_TIME(1000 * 60 * 60 * 24);

    private final long time;

    expiredTime(long time) {
        this.time = time;
    }

    public long getTime() {
        return time;
    }
}

package com.CustomMessagingProtocol.Utilities;

import java.util.UUID;

public class IdGenerator {

    public static String getRandomId() {
        return UUID.randomUUID().toString().split("-")[0];
    }

    public static void main(String[] args) {
        System.out.println(IdGenerator.getRandomId());
    }
}

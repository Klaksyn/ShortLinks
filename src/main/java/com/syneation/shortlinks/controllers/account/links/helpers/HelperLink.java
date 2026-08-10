package com.syneation.shortlinks.controllers.account.links.helpers;

import java.util.concurrent.ThreadLocalRandom;

public class HelperLink {

    private static final String ALPHABET = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

    public static String generateLink(final int len) {

        ThreadLocalRandom random = ThreadLocalRandom.current();
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < len; i++) {
            int rndIndex = random.nextInt(ALPHABET.length());
            sb.append(ALPHABET.charAt(rndIndex));
        }

        return sb.toString();
    }

}

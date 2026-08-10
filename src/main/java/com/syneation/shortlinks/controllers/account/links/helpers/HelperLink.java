package com.syneation.shortlinks.controllers.account.links.helpers;

import jakarta.servlet.http.HttpServletRequest;

import java.util.Locale;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class HelperLink {

    private static final String ALPHABET = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

    public static String generateLink(final String protocol,
                                      final HttpServletRequest request,
                                      final int len) {
        if (!"http".equals(protocol) && !"https".equals(protocol)) {
            return "[ERROR] please change protocol to http or https";
        }

        String currentProtocol = protocol + "://";

        ThreadLocalRandom random = ThreadLocalRandom.current();
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < len; i++) {
            int rndIndex = random.nextInt(ALPHABET.length());
            sb.append(ALPHABET.charAt(rndIndex));
        }

        return currentProtocol + request.getServerName() + "/" +
                sb.toString();
    }

}

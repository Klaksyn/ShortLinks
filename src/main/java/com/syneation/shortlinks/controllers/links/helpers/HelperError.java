package com.syneation.shortlinks.controllers.links.helpers;

import com.syneation.shortlinks.controllers.user.Role;

public class HelperError {

    public static String getErr(
            ErrorLink err,
            Role role,
            Exception e
    ) {
        String action = switch (err) {
            case activate -> "активации";
            case deactivate -> "деактивации";
            case delete -> "удаления";
        };

        return role == Role.PROGRAMMER
                ? "Ошибка " + action + " ссылки: " + e.getMessage()
                : "Ошибка " + action + " ссылки!";
    }

}

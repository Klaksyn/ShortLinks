package com.syneation.shortlinks.controllers.links.helpers;

import com.syneation.shortlinks.Security.UserPrincipal;
import com.syneation.shortlinks.controllers.links.LinksService;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.concurrent.ThreadLocalRandom;


public class HelperLink {

    private static final String ALPHABET = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

    private final UserPrincipal userPrincipal;
    private final LinksService linksService;
    private final RedirectAttributes redirectAttributes;

    public HelperLink(
            UserPrincipal userPrincipal,
            LinksService linksService,
            RedirectAttributes redirectAttributes
    ) {
        this.userPrincipal = userPrincipal;
        this.linksService = linksService;
        this.redirectAttributes = redirectAttributes;
    }

    public static String generateLink(final short len) {

        ThreadLocalRandom random = ThreadLocalRandom.current();
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < len; i++) {
            int rndIndex = random.nextInt(ALPHABET.length());
            sb.append(ALPHABET.charAt(rndIndex));
        }

        return sb.toString();
    }

    //===================================
    // Manager Links
    //===================================
    private String handleAction(
            Runnable action,
            String successMessage,
            ErrorLink errorType
    ) {
        try {
            action.run();
            redirectAttributes.addFlashAttribute("success", successMessage);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error",
                    HelperError.getErr(errorType, userPrincipal.getRole(), e));
        }

        return "redirect:/profile/links";
    }

    public String deleteLink(Long idLink) {
        return handleAction(() -> linksService.delete(idLink),
                "Ссылка удалена!", ErrorLink.delete);
    }

    public String deactivateLink(Long idLink) {
        return handleAction(() -> linksService.deactivate(idLink),
                "Ссылка деактивирована!", ErrorLink.deactivate);
    }

    public String activateLink(Long idLink) {
        return handleAction(() -> linksService.activate(idLink),
                "Ссылка активирована!", ErrorLink.activate);
    }

}

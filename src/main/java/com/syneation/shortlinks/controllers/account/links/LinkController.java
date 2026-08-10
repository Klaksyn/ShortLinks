package com.syneation.shortlinks.controllers.account.links;

import com.syneation.shortlinks.Security.UserPrincipal;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LinkController {

    @GetMapping("/profile/links")
    public String ownLinkPage(
            Model model,
            @AuthenticationPrincipal UserPrincipal userPrincipal
            ) {

        if (userPrincipal != null) {
            model.addAttribute("name", userPrincipal.getUsername());
            return "account/links/links";
        }

        return "redirect:/login";
    }

}

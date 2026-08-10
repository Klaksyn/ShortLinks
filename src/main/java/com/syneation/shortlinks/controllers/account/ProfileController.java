package com.syneation.shortlinks.controllers.account;

import com.syneation.shortlinks.Security.UserPrincipal;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class ProfileController {

    @GetMapping("/profile")
    public String profilePage(
            Model model,
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        if (userPrincipal != null) {
            String name = userPrincipal.getUsername();
            model.addAttribute("name", name);
            model.addAttribute("success", true);
            return "account/profile";
        }
        return "redirect:/login";
    }
}

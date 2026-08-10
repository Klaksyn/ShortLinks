package com.syneation.shortlinks.controllers.auth;

import com.syneation.shortlinks.Security.UserPrincipal;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

    @GetMapping("/login")
    public String loginPage(
            Model model,
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {

        if (userPrincipal != null)  {
            model.addAttribute("name", userPrincipal.getUsername());
            return "redirect:/profile";
        }

        model.addAttribute("success", false);
        return "auth/login";
    }

}

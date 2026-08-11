package com.syneation.shortlinks.controllers.account.links;

import com.syneation.shortlinks.Repository.LinksRepository;
import com.syneation.shortlinks.Security.UserPrincipal;
import com.syneation.shortlinks.entity.Links;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class LinkController {

    private LinksRepository linksRepo;

    public LinkController(LinksRepository linksRepo) {
        this.linksRepo = linksRepo;
    }

    @GetMapping("/profile/links")
    public String ownLinkPage(
            Model model,
            @AuthenticationPrincipal UserPrincipal userPrincipal
            ) {

        if (userPrincipal != null) {
            List<Links> linksList = linksRepo.findLinksByCreator_Id(userPrincipal.getUsers().getId());

            model.addAttribute("name", userPrincipal.getUsername());
            model.addAttribute("linksList", linksList);

            return "account/links/links";
        }

        return "redirect:/login";
    }

}

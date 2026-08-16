package com.syneation.shortlinks.controllers.links;

import com.syneation.shortlinks.Security.UserPrincipal;
import com.syneation.shortlinks.controllers.links.helpers.HelperLink;
import com.syneation.shortlinks.controllers.links.model.Links;
import com.syneation.shortlinks.controllers.user.Role;
import com.syneation.shortlinks.controllers.user.Users;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Date;
import java.util.List;

@Controller
public class LinkController {

    private final LinksRepository linksRepo;
    private final LinksService linksService;

    @Value("${redirector.base-url}")
    private String redirectorBaseUrl;

    public LinkController(
            LinksRepository linksRepo,
            LinksService linksService
    ) {
        this.linksRepo = linksRepo;
        this.linksService = linksService;
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

    //====================================
    // new links
    //====================================
    @GetMapping("/profile/links/new")
    public String createLinkPage(
            Model model,
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        if (userPrincipal != null) {
            if (!model.containsAttribute("linksDto")) {
                LinksDto linksDto = new LinksDto();
                model.addAttribute("linksDto", linksDto);
            }

            model.addAttribute("name", userPrincipal.getUsername());
            model.addAttribute("currentId", userPrincipal.getUsers().getId());

            return "account/links/new";
        }

        return "redirect:/login";
    }

    @PostMapping("/profile/links/new")
    public String createLink(
            @Valid @ModelAttribute LinksDto linksDto,
            BindingResult bindingResult,
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            RedirectAttributes redirectAttributes)
    {

        if (userPrincipal == null) {
            return "redirect:/login";
        }

        if (bindingResult.hasErrors()) {
            if (userPrincipal.getRole() == Role.PROGRAMMER) {
                redirectAttributes.addFlashAttribute("error",
                        "[DEBUG] [ERROR]: " + bindingResult.getAllErrors());
            }


            redirectAttributes.addFlashAttribute("name",
                    userPrincipal.getUsername());
            redirectAttributes.addFlashAttribute(
                    "org.springframework.validation.BindingResult.linksDto",
                    bindingResult
            );

            redirectAttributes.addFlashAttribute("linksDto", linksDto);

            return "redirect:/profile/links/new";
        }

        String new_link = HelperLink.generateLink(linksDto.getLenUrl());

        try {
            Users user = userPrincipal.getUsers();
            Links newLinks = new Links();

            newLinks.setOriginal_link(linksDto.getOriginal_link());
            newLinks.setNew_link(new_link);
            newLinks.setCreated_at(new Date());
            newLinks.setUpdated_at(new Date());
            newLinks.setCreator(user);

            linksRepo.save(newLinks);

            redirectAttributes.addFlashAttribute("linksDto",
                    new LinksDto());
            String newLink = redirectorBaseUrl + "/" + new_link;
            redirectAttributes.addFlashAttribute("new_link", newLink);
            redirectAttributes.addFlashAttribute("name",
                    userPrincipal.getUsername());
            redirectAttributes.addFlashAttribute("success",
                    "Ссылка успешно создана!");


        } catch (Exception e) {
            if (userPrincipal.getRole() == Role.PROGRAMMER) {
                redirectAttributes.addFlashAttribute("error",
                        e.getMessage());
            } else {
                redirectAttributes.addFlashAttribute("error",
                        "ERROR: произошла ошибка создания ссылки");
            }
            return "redirect:/profile/links/new";
        }

        return "redirect:/profile/links/new";
    }

    //====================================
    // Delete Links
    // and
    // Activate / Deactivate Links
    //====================================
    @PostMapping("/profile/links/manager")
    public String managerLink(
            @RequestParam Long idLink,
            @RequestParam String action,
            RedirectAttributes redirectAttributes,
            @AuthenticationPrincipal UserPrincipal userPrincipal) {

        if (userPrincipal == null) {
            return "redirect:/login";
        }

        HelperLink hLink = new HelperLink(userPrincipal, linksService, redirectAttributes);

        return switch (action) {
            case "cancel" -> "redirect:/profile/links";
            case "delete" -> hLink.deleteLink(idLink);
            case "deactivate" -> hLink.deactivateLink(idLink);
            case "activate" -> hLink.activateLink(idLink);
            default -> {
                redirectAttributes.addFlashAttribute("error",
                        "Произошла неизвестная ошибка!");
                yield  "redirect:/profile/links";
            }
        };
    }



}

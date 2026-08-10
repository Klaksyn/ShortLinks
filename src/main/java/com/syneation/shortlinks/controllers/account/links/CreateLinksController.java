package com.syneation.shortlinks.controllers.account.links;

import com.syneation.shortlinks.Repository.LinksRepository;
import com.syneation.shortlinks.Repository.UserRepository;
import com.syneation.shortlinks.Security.UserPrincipal;
import com.syneation.shortlinks.controllers.account.links.helpers.HelperLink;
import com.syneation.shortlinks.dto.links.LinksDto;
import com.syneation.shortlinks.entity.Links;
import com.syneation.shortlinks.entity.Users;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.hibernate.id.uuid.Helper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Date;

@Controller
public class CreateLinksController {

    @Autowired
    private LinksRepository linksRepo;
    @Autowired
    private UserRepository userRepository;

    @GetMapping("/profile/links/new")
    public String createLinkPage(
            Model model,
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        if (userPrincipal != null) {
            LinksDto linksDto = new LinksDto();

            model.addAttribute("name", userPrincipal.getUsername());
            model.addAttribute("linksDto", linksDto);
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
            HttpServletRequest request,
            RedirectAttributes redirectAttributes)
    {

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("error",
                    "Неизвестная Ошибка!");
            redirectAttributes.addFlashAttribute("name",
                    userPrincipal.getUsername());
            return "redirect:/profile/links/new";
        }

        String new_link = HelperLink.generateLink("http", request, 14);

        try {
            Links newLinks = new Links();
            newLinks.setOriginal_link(linksDto.getOriginal_link());
            newLinks.setNew_link(new_link);
            newLinks.setCreated_at(new Date());
            newLinks.setUpdated_at(new Date());

            Users user = userRepository.findById(linksDto.getCreator())
                    .orElseThrow(() -> new RuntimeException("Пользователь не найден"));
            newLinks.setCreator(user);

            linksRepo.save(newLinks);

            redirectAttributes.addFlashAttribute("linksDto",
                    new LinksDto());
            redirectAttributes.addFlashAttribute("new_link", new_link);
            redirectAttributes.addFlashAttribute("name",
                    userPrincipal.getUsername());
            redirectAttributes.addFlashAttribute("success",
                    "Ссылка успешно создана!");


        } catch (Exception e) {
            if (userPrincipal != null &&
                    ("programmer".equals(userPrincipal.getRole())
                            || "admin".equals(userPrincipal.getRole()))) {
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

}

package com.syneation.shortlinks.controllers.auth;

import com.syneation.shortlinks.Repository.UserRepository;
import com.syneation.shortlinks.Security.UserPrincipal;
import com.syneation.shortlinks.dto.auth.RegisterDto;
import com.syneation.shortlinks.entity.Users;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Date;

@Controller
public class RegisterController {

    @Autowired
    private UserRepository userRepo;

    @GetMapping("/register")
    public String registerPage(
            Model model,
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {

        if (userPrincipal != null)  {
            model.addAttribute("name", userPrincipal.getUsername());
            return "redirect:/profile";
        }

        model.addAttribute("registerDto", new RegisterDto());
        model.addAttribute("success", false);

        return "auth/register";
    }

    @PostMapping("/register")
    public String createUser(
            Model model,
            @Valid @ModelAttribute RegisterDto registerDto,
            BindingResult bindingResult
    ) {
        if (!registerDto.getPassword().equals(registerDto.getConfirmPassword())) {
            bindingResult.addError(new FieldError("registerDto", "confirmPassword",
                    "Пароль и подтверждение пароля не совпадают!"));
        }

        if (userRepo.existsByEmail(registerDto.getEmail())) {
            bindingResult.addError(
                    new FieldError("registerDto", "email",
                            "Почта уже используется!")
            );
        }

        if (bindingResult.hasErrors()) {
            return "auth/register";
        }

        try {

            var bCryptEncoder = new BCryptPasswordEncoder();

            Users newUser = new Users();
            newUser.setFirstName(registerDto.getFirstName());
            newUser.setLastName(registerDto.getLastName());
            newUser.setLogin(registerDto.getLogin());
            newUser.setEmail(registerDto.getEmail());
            newUser.setPhone(registerDto.getPhone());
            newUser.setPhone("000000000000");
            newUser.setRole("user");
            newUser.setCreatedAt(new Date());
            newUser.setUpdatedAt(new Date());
            newUser.setPassword(bCryptEncoder.encode(registerDto.getPassword()));

            userRepo.save(newUser);

            model.addAttribute("registerDto", new RegisterDto());
            model.addAttribute("success", true);

            return "redirect:/login?success=true";

        } catch (Exception e) {
            bindingResult.addError(
                    new FieldError("registerDto", "firstName",
                            e.getMessage())
            );

            return "auth/register";
        }

    }

}

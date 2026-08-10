package com.syneation.shortlinks.Service;

import com.syneation.shortlinks.Repository.UserRepository;
import com.syneation.shortlinks.entity.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.syneation.shortlinks.Security.UserPrincipal;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepo;

    public UserService(UserRepository userRepo) {
        this.userRepo = userRepo;
    }

    @Override
    public UserDetails loadUserByUsername(String login)
            throws UsernameNotFoundException {

        Users users =  userRepo.findByLogin(login)
                .orElseThrow(() ->
                        new UsernameNotFoundException("Пользователь с таким логином не найден: %s!"
                                .formatted(login)));

        return new UserPrincipal(users);
    }

}

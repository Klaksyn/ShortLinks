package com.syneation.shortlinks.Repository;


import com.syneation.shortlinks.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<Users, Long> {

    Optional<Users> findByLogin(String login);
    boolean existsByEmail(String email);

}

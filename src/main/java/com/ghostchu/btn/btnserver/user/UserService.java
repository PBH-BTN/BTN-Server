package com.ghostchu.btn.btnserver.user;

import cn.dev33.satoken.stp.StpUtil;
import com.ghostchu.btn.btnserver.exception.AccessDeniedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository repository;

    public UserEntity me() throws AccessDeniedException {
        return getUserById(StpUtil.getLoginIdAsInt()).orElseThrow(AccessDeniedException::new);
    }

    public UserEntity save(UserEntity user) {
        return repository.save(user);
    }

    public Optional<UserEntity> getUserById(Integer id) {
        return repository.findById(id);
    }

    public UserEntity getUserByGithubIdentifier(String githubIdentifier) {
        return repository.findByGithubIdentifier(githubIdentifier);
    }

    public UserEntity getUserByEmail(String email) {
        return repository.findByEmail(email);
    }

}

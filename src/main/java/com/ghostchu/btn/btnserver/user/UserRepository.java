package com.ghostchu.btn.btnserver.user;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<UserEntity, Integer> {
    UserEntity findByGithubIdentifier(String githubIdentifier);

    UserEntity findByEmail(String email);
}

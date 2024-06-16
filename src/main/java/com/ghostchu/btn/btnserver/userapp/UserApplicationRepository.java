package com.ghostchu.btn.btnserver.userapp;

import com.ghostchu.btn.btnserver.user.UserEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface UserApplicationRepository extends CrudRepository<UserApplicationEntity, Integer> {
    List<UserApplicationEntity> findByUser(UserEntity user);

    UserApplicationEntity findByAppIdAndAppSecret(String appId, String appSecret);

    List<UserApplicationEntity> findByAppId(String appId);

    List<UserApplicationEntity> findByUser_Id(Integer id);
}

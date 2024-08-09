package com.ghostchu.btn.btnserver.userapp;

import com.ghostchu.btn.btnserver.user.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserApplicationService {
    @Autowired
    private UserApplicationRepository repository;

    public Optional<UserApplicationEntity> getUserApplication(Integer id) {
        return repository.findById(id);
    }

    public List<UserApplicationEntity> listUserApps(UserEntity user){
        return repository.findByUser(user);
    }




    public void deleteUserApp(Integer userAppId){
        repository.deleteById(userAppId);
    }

    public UserApplicationEntity saveUserApp(UserApplicationEntity userApp){
        return repository.save(userApp);
    }

    public UserApplicationEntity generateUserApp(UserEntity user, String comment){
        UserApplicationEntity userApp = new UserApplicationEntity();
        userApp.setUser(user);
        userApp.setAppId(UUID.randomUUID().toString());
        userApp.setAppSecret(UUID.randomUUID().toString());
        userApp.setComment(comment);
        userApp.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        userApp = repository.save(userApp);
        return userApp;
    }

    public UserApplicationEntity getUserApplication(String appId, String appSecret) {
        return repository.findByAppIdAndAppSecret(appId, appSecret);
    }
}

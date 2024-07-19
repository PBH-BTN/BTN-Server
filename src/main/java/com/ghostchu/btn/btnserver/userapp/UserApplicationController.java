package com.ghostchu.btn.btnserver.userapp;

import com.ghostchu.btn.btnserver.exception.AccessDeniedException;
import com.ghostchu.btn.btnserver.user.UserEntity;
import com.ghostchu.btn.btnserver.user.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Controller()
@RequestMapping("/userapps")
public class UserApplicationController {
    @Autowired
    private UserApplicationService service;
    @Autowired
    private UserService userService;
    @Autowired
    private ModelMapper modelMapper;

    @GetMapping("")
    public String viewUserApps(Model model) throws AccessDeniedException {
        List<UserApplicationEntity> entityList = service.listUserApps(userService.me());
        model.addAttribute("userapps", entityList.stream().map(e->modelMapper.map(e,UserApplicationEntityDto.class)).toList());
        return "userapps/index";
    }

    @GetMapping("/create")
    public String creatingUserApps(Model model) throws AccessDeniedException {
        return "userapps/create";
    }

    @PostMapping("/create")
    public String createUserApps(Model model, @RequestParam("comment") String comment) throws AccessDeniedException{
        UserApplicationEntity entity = service.generateUserApp(userService.me(), comment);
        model.addAttribute("userapp", modelMapper.map(entity, UserApplicationEntityDto.class));
        return "userapps/created";
    }

    @GetMapping("/reset")
    public String reset(Model model, @RequestParam("id") Integer id) throws AccessDeniedException {
        UserEntity user = userService.me();
        UserApplicationEntity entity = service.getUserApplication(id).orElseThrow();
        if(!Objects.equals(entity.getUser().getId(), user.getId())){
            throw new AccessDeniedException("你不能重置不属于你的 UserApp");
        }
        entity.setAppSecret(UUID.randomUUID().toString());
        entity = service.saveUserApp(entity);
        model.addAttribute("userapp", modelMapper.map(entity, UserApplicationEntityDto.class));
        return "userapps/created";
    }
}

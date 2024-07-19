//package com.ghostchu.btn.btnserver.userapp;
//
//import com.ghostchu.btn.btnserver.exception.AccessDeniedException;
//import com.ghostchu.btn.btnserver.user.UserEntity;
//import com.ghostchu.btn.btnserver.user.UserService;
//import org.modelmapper.ModelMapper;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//import java.util.Map;
//import java.util.Objects;
//import java.util.UUID;
//
//@RestController()
//@RequestMapping("/api")
//public class UserApplicationApiController {
//    @Autowired
//    private UserApplicationService service;
//    @Autowired
//    private UserService userService;
//    @Autowired
//    private ModelMapper modelMapper;
//
//    @GetMapping("/userapps")
//    public List<UserApplicationEntityDto> list() throws AccessDeniedException {
//        UserEntity user = userService.me();
//        //noinspection SimplifyStreamApiCallChains
//        return service.listUserApps(user).stream()
//                .map((element) -> modelMapper.map(element, UserApplicationEntityDto.class))
//                .map(dto->{
//                    dto.setAppSecret("<censored>");
//                    return dto;
//                })
//                .toList();
//    }
//
//    @PostMapping("/userapps")
//    public UserApplicationEntityDto create(@RequestBody Map<String, String> comment) throws AccessDeniedException {
//        UserEntity user = userService.me();
//        UserApplicationEntity entity = service.generateUserApp(user, comment.get("comment"));
//        return modelMapper.map(entity, UserApplicationEntityDto.class);
//    }
//
//    @PostMapping("/userapps/{id}/reset")
//    public UserApplicationEntityDto reset(@PathVariable Integer id) throws AccessDeniedException {
//        UserEntity user = userService.me();
//        UserApplicationEntity entity = service.getUserApplication(id).orElseThrow();
//        if(!Objects.equals(entity.getUser().getId(), user.getId())){
//            throw new AccessDeniedException("You can't reset the UserApplication that not belongs to you");
//        }
//        entity.setAppSecret(UUID.randomUUID().toString());
//        entity = service.saveUserApp(entity);
//        return modelMapper.map(entity, UserApplicationEntityDto.class);
//    }
//
//    @DeleteMapping("/userapps/{id}")
//    public void delete(@PathVariable Integer id) throws AccessDeniedException {
//        UserEntity user = userService.me();
//        UserApplicationEntity entity = service.getUserApplication(id).orElseThrow();
//        if(!Objects.equals(entity.getUser().getId(), user.getId())){
//            throw new AccessDeniedException("You can't delete the UserApplication that not belongs to you");
//        }
//        service.deleteUserApp(entity.getId());
//    }
//}

package com.ghostchu.btn.btnserver.user;

import com.ghostchu.btn.btnserver.exception.AccessDeniedException;
import com.ghostchu.btn.btnserver.exception.ResourceNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/user")
@Slf4j
public class UserController {
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private UserService userService;
    @GetMapping("/me")
    public UserEntityDto me() throws AccessDeniedException {
       return modelMapper.map(userService.me(), UserEntityDto.class);
    }

    @GetMapping("/profile/{id}")
    public UserEntityDto profile(@PathVariable Integer id) throws ResourceNotFoundException {
        Optional<UserEntity> entityOptional = userService.getUserById(id);
        if(entityOptional.isEmpty()){
            throw new ResourceNotFoundException("The user that you given not found");
        }
        return modelMapper.map(entityOptional.get(), UserEntityDto.class);
    }
}

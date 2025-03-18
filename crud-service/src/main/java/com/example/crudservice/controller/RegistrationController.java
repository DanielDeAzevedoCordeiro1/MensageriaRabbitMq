package com.example.crudservice.controller;


import com.example.crudservice.dto.UserDTO;
import com.example.crudservice.dto.UserRegisteredEvent;
import com.example.crudservice.model.User;
import com.example.crudservice.service.producer.ConfirmRegisterProducer;
import com.example.crudservice.service.user.CreateUserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/register")
public class RegistrationController {

    private final CreateUserService createUserService;
    private final ConfirmRegisterProducer confirmRegisterProducer;

    public RegistrationController(CreateUserService createUserService, ConfirmRegisterProducer confirmRegisterProducer) {
        this.createUserService = createUserService;
        this.confirmRegisterProducer = confirmRegisterProducer;
    }

    @PostMapping("/create")
    public ResponseEntity<UserRegisteredEvent> createUser(@RequestBody User user) {
        var savedUser = createUserService.register(user);
        UserRegisteredEvent userRegisteredEvent = new UserRegisteredEvent(savedUser.getName(),savedUser.getEmail(),savedUser.getCreatedAt());
        confirmRegisterProducer.sendMessageToExchange(userRegisteredEvent);
        return new ResponseEntity<>(userRegisteredEvent, HttpStatus.CREATED);
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        return new ResponseEntity<>(createUserService.getAllUsers(), HttpStatus.OK);
    }

}

package com.user.service.controller;


import com.user.service.dto.UserDto;
import com.user.service.Service.UserService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping
    public ResponseEntity<?> createUser(@RequestBody UserDto userDto) {
        return userService.saveUser(userDto);
    }

    @GetMapping
    public ResponseEntity<?> getAllUsers() {
        return userService.getAll();
    }



    @GetMapping("/{uId}")
  //  @CircuitBreaker(name = "ratingHotelBreaker",fallbackMethod = "ratingHotelFallBack")
    @Retry(name = "ratingHotelService",fallbackMethod = "ratingHotelFallBack")
    public ResponseEntity<?> getUserById(@PathVariable String uId) {
        return userService.getUserById(uId);
    }

    //creating fallback method for circuit breaker or retry
    public ResponseEntity<?> ratingHotelFallBack(String uId, Throwable ex) {
        log.info("Fallback is Executed because Service is Down {}", ex.getMessage());
        return new ResponseEntity<>(
                Collections.singletonMap("Please wait for Some Time", "Service is temporarily unavailable. Please try again later."),
                HttpStatus.SERVICE_UNAVAILABLE
        );
    }


}

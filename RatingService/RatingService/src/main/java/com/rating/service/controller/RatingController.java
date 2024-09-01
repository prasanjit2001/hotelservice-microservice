package com.rating.service.controller;

import com.rating.service.dto.RatingDto;
import com.rating.service.service.RatingService;
import com.rating.service.response.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/ratings")
@RequiredArgsConstructor
@Slf4j
public class RatingController {

    @Autowired
    private  RatingService ratingService;

    @PostMapping
    public ResponseEntity<?> createRating(@RequestBody RatingDto ratingDto) {
        log.info("Saving rating: {}", ratingDto);
        return ratingService.saveRating(ratingDto);
    }

    @GetMapping
    public ResponseEntity<?> getAllRatings() {
        return ratingService.getAllRatings();
    }

    @GetMapping("/user/{uId}")
    public ResponseEntity<?> getRatingByUserId(@PathVariable String uId) {
        return ratingService.getRatingByUserId(uId);
    }

    @GetMapping("/hotel/{hId}")
    public ResponseEntity<?> getRatingByHotelId(@PathVariable String hId) {
        return ratingService.getRatingByHotelId(hId);
    }

}

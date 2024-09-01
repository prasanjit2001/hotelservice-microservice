package com.rating.service.service;

import com.rating.service.dto.RatingDto;
import com.rating.service.entity.Rating;
import com.rating.service.repository.RatingRepository;
import com.rating.service.response.Response;
import com.rating.service.response.ResponseUtil;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
public class RatingService {

    @Autowired
    private RatingRepository ratingRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private ResponseUtil responseUtil;

    public ResponseEntity<?> saveRating(RatingDto ratingDto) {
        Rating rating = modelMapper.map(ratingDto, Rating.class);
        String randomRatingId = UUID.randomUUID().toString();
        rating.setRatingId(randomRatingId);
        Rating savedRating = ratingRepository.save(rating);
        RatingDto savedRatingDto = modelMapper.map(savedRating, RatingDto.class);
        Response response = responseUtil.successResponse("Rating saved successfully", savedRatingDto);
        log.info("Saving rating: {}", ratingDto);
        log.info("Saved rating: {}", savedRatingDto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    public ResponseEntity<?> getAllRatings() {
        List<Rating> ratings = ratingRepository.findAll();
        List<RatingDto> ratingDtos = ratings.stream()
                .map(rating -> modelMapper.map(rating, RatingDto.class))
                .collect(Collectors.toList());
        Response response = responseUtil.successResponse("Fetched all ratings", ratingDtos);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    public ResponseEntity<?> getRatingByUserId(String uId) {
        List<Rating> ratings = ratingRepository.findByUIdCustom(uId);
        if (ratings.isEmpty()) {
            Response errorResponse = responseUtil.notFoundResponse("No Ratings Found For The Given User ID");
            return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
        }
        List<RatingDto> ratingDtos = ratings.stream()
                .map(rating -> modelMapper.map(rating, RatingDto.class))
                .collect(Collectors.toList());
        Response response = responseUtil.successResponse("Ratings found for user", ratingDtos);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    public ResponseEntity<?> getRatingByHotelId(String hId) {
        List<Rating> ratings = ratingRepository.findByHotelIdCustom(hId);
        if (ratings.isEmpty()) {
            Response errorResponse = responseUtil.notFoundResponse("No Ratings Found For The Given Hotel ID");
            return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
        }
        List<RatingDto> ratingDtos = ratings.stream()
                .map(rating -> modelMapper.map(rating, RatingDto.class))
                .collect(Collectors.toList());
        Response response = responseUtil.successResponse("Ratings found for hotel", ratingDtos);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}

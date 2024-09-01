package com.user.service.external.service;

import com.user.service.entity.Rating;
import com.user.service.response.RatingResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "Rating-Service")
public interface RatingService {


    @PostMapping("/ratings")
    public RatingResponse createRating(Rating values);

}

package com.user.service;

import com.user.service.entity.Rating;
import com.user.service.external.service.RatingService;
import com.user.service.response.RatingResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class UserServiceApplicationTests {
//	@Autowired
// private RatingService ratingService;
	@Test
	void contextLoads() {
	}
//	@Test
//  void createRating(){
//	  Rating rating=Rating.builder().
//			  rating(10)
//					  .uId("")
//							  .hid("")
//									  .feedback("This is created using feign client").build();
//
//  RatingResponse savedRating=ratingService.createRating(rating);
//	  System.out.println("new rating created");
//  }
}

package com.rating.service.repository;

import com.rating.service.entity.Rating;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RatingRepository extends MongoRepository<Rating, String> {

 @Query("{ 'uId': ?0 }")
 List<Rating> findByUIdCustom(String uId);

 @Query("{ 'hotelId': ?0 }")
 List<Rating> findByHotelIdCustom(String hotelId);
}



package com.user.service.Service;

import com.user.service.dto.UserDto;
import com.user.service.entity.Hotel;
import com.user.service.entity.Rating;
import com.user.service.entity.User;
import com.user.service.external.service.HotelService;
import com.user.service.repository.UserRepository;
import com.user.service.response.HotelResponse;
import com.user.service.response.Response;
import com.user.service.response.ResponseUtil;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private ResponseUtil responseUtil;
    @Autowired
    private RestTemplate restTemplate;
   @Autowired
    private HotelService hotelService;


    public ResponseEntity<?> saveUser(UserDto userDto) {
        User user = modelMapper.map(userDto, User.class);
        String randomUserId = UUID.randomUUID().toString();
        user.setUId(randomUserId);
        User savedUser = userRepository.save(user);
        UserDto savedUserDto = modelMapper.map(savedUser, UserDto.class);
        Response response = responseUtil.successResponse("User saved successfully", savedUserDto);
        log.info("Saving user: {}", userDto);
        log.info("Saved user: {}", savedUserDto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    public ResponseEntity<?> getAll() {
        List<User> users = userRepository.findAll();
        List<UserDto> userDtos = users.stream()
                .map(user -> modelMapper.map(user, UserDto.class))
                .collect(Collectors.toList());
        Response response = responseUtil.successResponse("Fetched all users", userDtos);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

//    public ResponseEntity<?> getUserById(String uId) {
//        log.info("Fetching user with ID: {}", uId);
//
//        // Fetch user from the repository
//        Optional<User> userOptional = userRepository.findById(uId);
//
//        if (!userOptional.isPresent()) {
//            log.error("User not found for ID: {}", uId);
//            Response errorResponse = responseUtil.notFoundResponse("No User Found By The Given ID");
//            return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
//        }
//
//        // Fetch ratings from the ratings service
//        ResponseEntity<Map> responseEntity = restTemplate.getForEntity("http://Rating-Service/ratings/user/" + uId, Map.class);
//        Map<String, Object> responseBody = responseEntity.getBody();
//
//        if (responseBody != null) {
//            // Extract the 'result' field which is a List<Map<String, Object>> of ratings
//            Object result = responseBody.get("result");
//            if (result instanceof List) {
//                List<Map<String, Object>> ratingsList = (List<Map<String, Object>>) result;
//                log.info("Ratings list: {}", ratingsList);
//
//                // Convert List of Maps to List of Rating objects
//                List<Rating> ratings = ratingsList.stream()
//                        .map(ratingMap -> {
//                            Rating rating = new Rating();
//
//                            // Fetch hotel details using hotelId
//                            String hotelId = (String) ratingMap.get("hotelId");
//                            log.info("Fetching hotel details for hotelId: {}", hotelId);
//
//                            if (hotelId != null && !hotelId.isEmpty()) {
//                                try {
//                                    ResponseEntity<Map> hotelResponse = restTemplate.getForEntity("http://Hotel-Service/hotels/" + hotelId, Map.class);
//                                    Map<String, Object> hotelMap = hotelResponse.getBody();
//                                    if (hotelMap != null && hotelMap.containsKey("result")) {
//                                        Map<String, Object> hotelResult = (Map<String, Object>) hotelMap.get("result");
//                                        if (hotelResult != null) {
//                                            log.info("Hotel details for hotelId {}: {}", hotelId, hotelResult);
//                                            Hotel hotel = new Hotel();
//                                            hotel.setHid((String) hotelResult.get("hid"));
//                                            hotel.setName((String) hotelResult.get("name"));
//                                            hotel.setLocation((String) hotelResult.get("location"));
//                                            hotel.setAbout((String) hotelResult.get("about"));
//                                            rating.setHotel(hotel);
//                                        } else {
//                                            log.warn("No hotel details found for hotelId: {}", hotelId);
//                                        }
//                                    } else {
//                                        log.warn("Hotel details not found in response for hotelId: {}", hotelId);
//                                    }
//                                } catch (Exception e) {
//                                    log.error("Error fetching hotel details for hotelId: {}", hotelId, e);
//                                }
//                            } else {
//                                log.warn("hotelId is null or empty for ratingId: {}", ratingMap.get("ratingId"));
//                            }
//
//                            // Set rating details
//                            rating.setRatingId((String) ratingMap.get("ratingId"));
//                            rating.setHid((String) ratingMap.get("hotelId"));
//                            rating.setRating((Integer) ratingMap.get("rating"));
//                            rating.setFeedback((String) ratingMap.get("feedback"));
//                            rating.setUId((String) ratingMap.get("uId"));
//
//                            return rating;
//                        })
//                        .collect(Collectors.toList());
//
//                // Map user to UserDto and set ratings
//                User user = userOptional.get();
//                UserDto userDto = modelMapper.map(user, UserDto.class);
//                userDto.setRatings(ratings);
//                Response response = responseUtil.successResponse("User found", userDto);
//                return new ResponseEntity<>(response, HttpStatus.OK);
//            } else {
//                log.error("Unexpected response format from ratings service: {}", responseBody);
//            }
//        } else {
//            log.error("No response body received from the ratings service.");
//        }
//
//        Response errorResponse = responseUtil.notFoundResponse("No User Found By The Given ID");
//        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
//    }

    public ResponseEntity<?> getUserById(String uId) {
        log.info("Fetching user with ID: {}", uId);
        Optional<User> userOptional = userRepository.findById(uId);

        if (!userOptional.isPresent()) {
            log.error("User not found for ID: {}", uId);
            Response errorResponse = responseUtil.notFoundResponse("No User Found By The Given ID");
            return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
        }

        // Let the exception propagate to the circuit breaker
        ResponseEntity<Map> responseEntity = restTemplate.getForEntity("http://Rating-Service/ratings/user/" + uId, Map.class);
        Map<String, Object> responseBody = responseEntity.getBody();

        if (responseBody != null) {
            Object result = responseBody.get("result");
            if (result instanceof List) {
                List<Map<String, Object>> ratingsList = (List<Map<String, Object>>) result;
                log.info("Ratings list: {}", ratingsList);
                if (ratingsList.isEmpty()) {
                    log.error("No ratings found for user ID: {}", uId);
                    Response errorResponse = responseUtil.notFoundResponse("No Ratings Found for the Given User ID");
                    return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
                }

                List<Rating> ratings = ratingsList.stream()
                        .map(ratingMap -> {
                            Rating rating = new Rating();
                            String hotelId = (String) ratingMap.get("hotelId");
                            log.info("Fetching hotel details for hotelId: {}", hotelId);

                            if (hotelId != null && !hotelId.isEmpty()) {
                                try {
                                    HotelResponse hotelResponse = hotelService.getHotel(hotelId);
                                    Hotel hotel = hotelResponse != null ? hotelResponse.getResult() : null;
                                    if (hotel != null) {
                                        log.info("Hotel details for hotelId {}: {}", hotelId, hotel);
                                        rating.setHotel(hotel);
                                    } else {
                                        log.warn("No hotel details found for hotelId: {}", hotelId);
                                    }
                                } catch (Exception e) {
                                    log.error("Error fetching hotel details for hotelId: {}", hotelId, e);
                                    // Let this exception propagate so that circuit breaker can catch it
                                    throw e;
                                }
                            } else {
                                log.warn("hotelId is null or empty for ratingId: {}", ratingMap.get("ratingId"));
                            }

                            rating.setRatingId((String) ratingMap.get("ratingId"));
                            rating.setHid((String) ratingMap.get("hotelId"));
                            rating.setRating((Integer) ratingMap.get("rating"));
                            rating.setFeedback((String) ratingMap.get("feedback"));
                            rating.setUId((String) ratingMap.get("uId"));

                            return rating;
                        })
                        .collect(Collectors.toList());

                User user = userOptional.get();
                UserDto userDto = modelMapper.map(user, UserDto.class);
                userDto.setRatings(ratings);
                Response response = responseUtil.successResponse("User found", userDto);
                return new ResponseEntity<>(response, HttpStatus.OK);
            } else {
                log.error("Unexpected response format from ratings service: {}", responseBody);
            }
        } else {
            log.error("No response body received from the ratings service.");
        }

        // Fallback to this response if no user or ratings found
        Response errorResponse = responseUtil.notFoundResponse("No User Found By The Given ID");
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }







}

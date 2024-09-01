package com.hotel.service.service;

import com.hotel.service.dto.HotelDto;

import com.hotel.service.entity.Hotel;
import com.hotel.service.repository.HotelRepository;
import com.hotel.service.response.Response;
import com.hotel.service.response.ResponseUtil;
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
public class HotelService {

    @Autowired
    private HotelRepository hotelRepository;
    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private ResponseUtil responseUtil;

    public ResponseEntity<?> createHotel(HotelDto hotelDto){
        Hotel hotel=modelMapper.map(hotelDto,Hotel.class);
        String randomHotelId = UUID.randomUUID().toString();
        hotel.setHId(randomHotelId);
        Hotel createHotel=hotelRepository.save(hotel);
        HotelDto createHotelDto=modelMapper.map(createHotel,HotelDto.class);
        Response response = responseUtil.successResponse("Hotel saved successfully", createHotelDto);
        log.info("Saving user: {}", hotelDto);
        log.info("Saved user: {}", createHotelDto);
        return new ResponseEntity<>(response, HttpStatus.CREATED);

    }
    public ResponseEntity<?> getAll() {
        List<Hotel> hotels = hotelRepository.findAll();
        List<HotelDto> hotelDtos = hotels.stream()
                .map(hotel -> modelMapper.map(hotel, HotelDto.class))
                .collect(Collectors.toList());
        Response response = responseUtil.successResponse("Fetched all Hotels", hotelDtos);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    public ResponseEntity<?> getHotelById(String hId) {
        Optional<Hotel> hotelOptional = hotelRepository.findById(hId);
        if (hotelOptional.isPresent()) {
            Hotel hotel = hotelOptional.get();
            HotelDto hotelDto = modelMapper.map(hotel, HotelDto.class);
            Response response = responseUtil.successResponse("Hotel found", hotelDto);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        Response errorResponse = responseUtil.notFoundResponse("No Hotel Found By The Given ID");
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }
}

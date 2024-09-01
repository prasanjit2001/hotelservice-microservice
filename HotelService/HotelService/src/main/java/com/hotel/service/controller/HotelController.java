package com.hotel.service.controller;

import com.hotel.service.dto.HotelDto;
import com.hotel.service.service.HotelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/hotels")
public class HotelController {
    @Autowired
  private HotelService hotelService;

@PostMapping
public ResponseEntity<?>createHotel(@RequestBody HotelDto hotelDto){
    return hotelService.createHotel(hotelDto);
}

@GetMapping
public ResponseEntity<?>getAll(){
    return hotelService.getAll();
}
@GetMapping("/{hId}")
public ResponseEntity<?>getHotelById(@PathVariable String hId){
    return hotelService.getHotelById(hId);
}
}

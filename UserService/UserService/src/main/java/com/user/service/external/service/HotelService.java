package com.user.service.external.service;


import com.user.service.response.HotelResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "Hotel-Service")
public interface HotelService {

    @GetMapping("/hotels/{hId}")
    HotelResponse getHotel(@PathVariable String hId);
}

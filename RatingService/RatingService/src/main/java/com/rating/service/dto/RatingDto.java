package com.rating.service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RatingDto {
    private String ratingId;
    @JsonProperty("uId")
    private String uId;
    private String hotelId;
    private int rating;
    private String feedback;
}


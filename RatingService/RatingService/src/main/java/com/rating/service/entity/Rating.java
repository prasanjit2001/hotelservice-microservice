package com.rating.service.entity;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document("user_ratings")
public class Rating {

    @Id
    private String ratingId;

    @JsonProperty("uId") // Specify the field name as "uId" in MongoDB
    private String uId;

    private String hotelId;
    private int rating;
    private String feedback;
}


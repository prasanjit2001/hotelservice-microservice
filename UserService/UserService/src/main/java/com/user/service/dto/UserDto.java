package com.user.service.dto;

import com.user.service.entity.Rating;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    private String uId;
    private String name;
    private String email;
    private String about;
    private List<Rating>ratings=new ArrayList<>();
}

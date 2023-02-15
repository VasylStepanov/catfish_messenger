package org.application.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserProfileDto {

    private String description;

    private Integer userId;

    public UserProfileDto(String description){
        this.description = description;
    }
}

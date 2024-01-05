package com.example.spring.controller.DTO;

import com.example.spring.model.User;
import lombok.Data;

@Data
public class ResponseDTO {

    private BearerToken bearerToken ;
    private User user ;

    public ResponseDTO(BearerToken bearerToken , User user) {
        this.bearerToken = bearerToken ;
        this.user = user ;
    }
}

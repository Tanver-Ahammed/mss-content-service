package com.mss.model.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ApiResponseWrapper<T> {

    private T response;
    private String statusCode;

}

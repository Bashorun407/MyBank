package com.akinovapp.mybank.response;

import lombok.Data;

@Data
public class ResponsePojo <T>{
    String statusCode;
    String message;
    Boolean success = true;
    T data;
}

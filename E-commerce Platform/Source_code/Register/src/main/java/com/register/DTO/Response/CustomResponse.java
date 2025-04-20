package com.register.DTO.Response;

public interface CustomResponse {
    String getMessage(); // Method to get the message from the response

    void setMessage(String message); // Method to set the message in the response

    int getStatus(); // Method to get the status of the response

    void setStatus(int status); // Method to set the status in the response
}

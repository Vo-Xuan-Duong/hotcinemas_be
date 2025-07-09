package com.example.hotcinemas_be.dtos.requests;

import java.time.LocalDateTime;

public class ShowtimeSeatRequest {

    private long userId; // ID of the user
    private LocalDateTime heldUntil; // ISO-8601 format for held until time, if applicable
}

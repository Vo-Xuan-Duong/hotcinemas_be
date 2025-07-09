package com.example.hotcinemas_be.dtos.requests;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GenreRequest {
    private String genreName;// Tên thể loại phim, không được trùng với các thể loại đã có trong hệ thống
}

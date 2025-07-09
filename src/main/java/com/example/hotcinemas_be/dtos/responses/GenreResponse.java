package com.example.hotcinemas_be.dtos.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GenreResponse {
    private Long genreId; // Mã định danh thể loại phim
    private String name; // Tên thể loại phim, không được trùng với các thể loại đã có trong hệ thống
}

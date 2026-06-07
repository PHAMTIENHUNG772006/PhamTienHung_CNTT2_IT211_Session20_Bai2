package com.re.session20.controller;

import com.re.session20.model.dto.response.ApiDataResponse;
import com.re.session20.model.dto.response.ArtworkResponse;
import com.re.session20.service.ArtworkService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/gallery/artworks")
@RequiredArgsConstructor
public class ArtworkController {

    private final ArtworkService artworkService;

    @GetMapping
    public ResponseEntity<ApiDataResponse<List<ArtworkResponse>>> getAll() {

        return ResponseEntity.ok(
                new ApiDataResponse<>(
                        true,
                        "Lấy danh sách artwork thành công",
                        artworkService.getAllArtworks(),
                        null,
                        HttpStatus.OK
                )
        );
    }
}
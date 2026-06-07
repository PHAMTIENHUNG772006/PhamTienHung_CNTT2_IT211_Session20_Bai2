package com.re.session20.service;

import com.re.session20.model.dto.response.ArtworkResponse;

import java.util.List;

public interface ArtworkService {

    List<ArtworkResponse> getAllArtworks();

}
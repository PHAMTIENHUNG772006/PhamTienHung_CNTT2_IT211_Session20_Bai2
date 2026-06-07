package com.re.session20.service.impl;

import com.re.session20.model.dto.response.ArtworkResponse;
import com.re.session20.model.entity.Artwork;
import com.re.session20.repository.ArtworkRepository;
import com.re.session20.service.ArtworkService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ArtworkServiceImpl implements ArtworkService {

    private final ArtworkRepository artworkRepository;

    @Override
    public List<ArtworkResponse> getAllArtworks() {

        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        String username =
                authentication.getName();

        boolean isAdmin =
                authentication.getAuthorities()
                        .stream()
                        .anyMatch(
                                authority ->
                                        authority.getAuthority()
                                                .equals("ROLE_ADMIN")
                        );

        List<Artwork> artworks =
                artworkRepository.findAll();

        if (isAdmin) {

            return artworks.stream()
                    .map(this::toResponse)
                    .toList();
        }

        return artworks.stream()
                .filter(artwork ->
                        artwork.getIsPublished()
                                || artwork.getOwner()
                                .getUsername()
                                .equals(username)
                )
                .map(this::toResponse)
                .toList();
    }

    private ArtworkResponse toResponse(
            Artwork artwork
    ) {

        return ArtworkResponse.builder()
                .id(artwork.getId())
                .title(artwork.getTitle())
                .description(artwork.getDescription())
                .published(artwork.getIsPublished())
                .owner(
                        artwork.getOwner()
                                .getUsername()
                )
                .build();
    }
}
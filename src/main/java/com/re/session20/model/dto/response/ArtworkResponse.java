package com.re.session20.model.dto.response;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ArtworkResponse {

    private Long id;

    private String title;

    private String description;

    private Boolean published;

    private String owner;
}
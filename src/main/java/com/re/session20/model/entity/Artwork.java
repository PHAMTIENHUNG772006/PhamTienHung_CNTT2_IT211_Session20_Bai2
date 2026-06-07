package com.re.session20.model.entity;

import jakarta.persistence.*;
import lombok.*;


@Entity
@Table(name = "artworks")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Artwork {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String description;

    private Boolean isPublished;

    @ManyToOne
    @JoinColumn(name = "owner_id")
    private Account owner;
}
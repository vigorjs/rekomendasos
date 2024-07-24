package com.virgo.rekomendasos.utils.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PostDto {

    @JsonProperty("title")
    private String title;

    @JsonProperty("description")
    private String description;

    @JsonProperty("picture")
    private String picture;

    @JsonProperty("star_review")
    private Integer star_review;

    @JsonProperty("user_id")
    private Integer user_id;

    @JsonProperty("place_id")
    private String place_id;
}

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

    @JsonProperty("rating")
    private Integer rating;

    @JsonProperty("user_id")
    private Integer userId;

    @JsonProperty("place_id")
    private String placeId;
}

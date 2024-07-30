package com.virgo.rekomendasos.utils.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
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
    @NotNull
    private String title;

    @NotNull
    @JsonProperty("description")
    private String description;

    @JsonProperty("picture")
    private String picture;

    @NotNull
    @Max(5)
    @Min(0)
    @JsonProperty("rating")
    private Integer rating;

    @JsonProperty("user_id")
    @NotNull
    private Integer userId;

    @JsonProperty("place_id")
    @NotNull
    private String placeId;
}

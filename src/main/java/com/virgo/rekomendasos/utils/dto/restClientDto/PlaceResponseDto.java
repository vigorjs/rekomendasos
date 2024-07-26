package com.virgo.rekomendasos.utils.dto.restClientDto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PlaceResponseDto {

    @JsonProperty("type")
    private String type;

    @JsonProperty("features")
    private List<Features> features;

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class Features {
        @JsonProperty("properties")
        private Properties properties;

        @Getter
        @Setter
        @AllArgsConstructor
        @NoArgsConstructor
        @Builder
        public static class Properties {

            @JsonProperty("place_id")
            private String placeId;

            @JsonProperty("name")
            private String name;

            @JsonProperty("lat")
            private double latitude;

            @JsonProperty("lon")
            private double longitude;
        }
    }

}

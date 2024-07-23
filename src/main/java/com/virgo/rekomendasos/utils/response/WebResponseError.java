package com.virgo.rekomendasos.utils.response;

import lombok.*;
import org.springframework.http.HttpStatus;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WebResponseError<T> {
    private String message;
    private HttpStatus status;
    private List<T> errors;
}
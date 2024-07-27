package com.virgo.rekomendasos.utils.responseWrapper;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class WebResponse<T> {
    private String message;
    private HttpStatus status;
    private T data;
}

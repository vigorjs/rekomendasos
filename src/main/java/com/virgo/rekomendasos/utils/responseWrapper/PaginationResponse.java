package com.virgo.rekomendasos.utils.responseWrapper;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class PaginationResponse<T> {
    private List<T> content;
    private Integer size;
    private Integer totalPages;
    private Integer page;

    public PaginationResponse(Page<T> page) {
        this.content = page.getContent();
        this.size = page.getSize();
        this.totalPages = page.getTotalPages();
        this.page = page.getNumber();
    }
}

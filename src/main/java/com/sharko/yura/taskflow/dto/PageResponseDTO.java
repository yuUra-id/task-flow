package com.sharko.yura.taskflow.dto;

import lombok.Value;
import org.springframework.data.domain.Page;

import java.util.List;

@Value
public class PageResponseDTO<T> {

    List<T> content;
    Metadata metadata;

    public static <T> PageResponseDTO<T> mapToPageResponse(Page<T> page) {

        var metadata = new Metadata(page.getNumber(), page.getSize(), page.getTotalElements(),
                page.getTotalPages(), page.isFirst(), page.isLast());
        return new PageResponseDTO<>(page.getContent(), metadata);

    }

    @Value
    public static class Metadata{
        int page;
        int size;
        long totalElements;
        int totalPages;
        boolean first;
        boolean last;
    }

}

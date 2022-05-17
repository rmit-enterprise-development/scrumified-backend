package app.scrumifiedbackend.dto;

import com.fasterxml.jackson.annotation.JsonFilter;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.*;

@Data
@AllArgsConstructor
@JsonFilter(value = "paginationFilter")
public class PaginationDto <T> {
    private T entity;
    private Long totalElements;
    private Integer totalPages;
    private Integer currentPage;

    private static final List<String> params = Arrays.asList("entity", "totalElements", "totalPages", "currentPage");

    public static Set<String> getFilter(String... exceptElement) {
        Set<String> result = new HashSet<>(params);
        Arrays.asList(exceptElement).forEach(result::remove);
        return result;
    }
}

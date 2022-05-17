package app.scrumifiedbackend.dto;

import com.fasterxml.jackson.annotation.JsonFilter;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonFilter(value = "sprintFilter")
public class SprintDto {
    private Long id;
    private String goal;
    private String status;
    private String defOfDone;
    private Long startDate;
    private Long endDate;
    private Long completePercentage;
    private Long projectId;

    private static final List<String> params = Arrays.asList(
            "id", "goal", "status",
            "defOfDone", "startDate",
            "endDate", "projectId", "completePercentage"
    );

    public static Set<String> getFilter(String... exceptElement) {
        Set<String> result = new HashSet<>(params);
        Arrays.asList(exceptElement).forEach(result::remove);
        return result;
    }
}

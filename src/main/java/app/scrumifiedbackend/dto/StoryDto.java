package app.scrumifiedbackend.dto;

import com.fasterxml.jackson.annotation.JsonFilter;
import lombok.Data;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@JsonFilter(value = "storyFilter")
public class StoryDto {
    private Long id;
    private String userStory;
    private String category;
    private Long createdDate;
    private Integer point;
    private String defOfDone;
    private String status;
    private Long parentStoryId;
    private Long childStoryId;
    private Long replaceStoryId;
    private Long projectId;
    private Long sprintId;
    private Long assignId;

    private static final List<String> params = Arrays.asList(
            "id", "userStory", "category",
            "createdDate", "point", "defOfDone", "status",
            "parentStoryId", "childStoryId",
            "sprintId", "projectId", "assignId"
    );

    public static Set<String> getFilter(String... exceptElement) {
        Set<String> result = new HashSet<>(params);
        Arrays.asList(exceptElement).forEach(result::remove);
        return result;
    }
}
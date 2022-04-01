package app.scrumifiedbackend.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class StoryDto {
    private Long id;
    private String userStory;
    private String category;
    private Integer point;
    private String status;
    private Long projectId;
    private Long assignId;
}
package app.scrumifiedbackend.dto;

import app.scrumifiedbackend.repository.StoryRepo;
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
    private Integer position;
    private Long projectId;
    private Long assignId;
}
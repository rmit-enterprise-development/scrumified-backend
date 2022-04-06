package app.scrumifiedbackend.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class SprintDto {
    private Long id;
    private String goal;
    private String status;
    private String defOfDone;
    private String startDate;
    private String endDate;
    private Long projectId;
}

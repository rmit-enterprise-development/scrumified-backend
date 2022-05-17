package app.scrumifiedbackend.service.interface_service;

import app.scrumifiedbackend.dto.PaginationDto;
import app.scrumifiedbackend.dto.ProjectDto;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProjectService extends Service<ProjectDto> {

    ProjectDto findProjectWithPoints(Long projectId);

    PaginationDto<List<ProjectDto>> findProjectBelongToUser(Long id, String key, Pageable pageable);
}

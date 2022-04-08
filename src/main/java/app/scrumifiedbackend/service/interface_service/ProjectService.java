package app.scrumifiedbackend.service.interface_service;

import app.scrumifiedbackend.dto.ProjectDto;

public interface ProjectService extends Service<ProjectDto> {

    ProjectDto findProjectWithPoints(Long projectId);

    Long pointsOfProject(Long projectId);

    Long pointsOfProjectByStatus(Long projectId, String status);

    Long pointsOfUser(Long projectId, Long userId);

    Long pointsOfUserByStatus(Long projectId, Long userId, String status);
}

package app.scrumifiedbackend.assembler;

import app.scrumifiedbackend.dto.ProjectDto;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;

public class ProjectDtoEntityAssembler implements RepresentationModelAssembler<ProjectDto, EntityModel<ProjectDto>> {
    @Override
    public EntityModel<ProjectDto> toModel(ProjectDto entity) {
        return EntityModel.of(entity);
    }

    @Override
    public CollectionModel<EntityModel<ProjectDto>> toCollectionModel(Iterable<? extends ProjectDto> entities) {
        return RepresentationModelAssembler.super.toCollectionModel(entities);
    }
}

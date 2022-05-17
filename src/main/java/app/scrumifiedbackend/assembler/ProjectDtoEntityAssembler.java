package app.scrumifiedbackend.assembler;

import app.scrumifiedbackend.controller.ProjectController;
import app.scrumifiedbackend.dto.ProjectDto;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class ProjectDtoEntityAssembler implements RepresentationModelAssembler<ProjectDto, EntityModel<ProjectDto>> {
    @Override
    public EntityModel<ProjectDto> toModel(ProjectDto entity) {
        return EntityModel.of(entity,
                linkTo(methodOn(ProjectController.class).getProject(entity.getId(), true)).withSelfRel()
        );
    }

    @Override
    public CollectionModel<EntityModel<ProjectDto>> toCollectionModel(Iterable<? extends ProjectDto> entities) {
        return RepresentationModelAssembler.super.toCollectionModel(entities);
    }
}

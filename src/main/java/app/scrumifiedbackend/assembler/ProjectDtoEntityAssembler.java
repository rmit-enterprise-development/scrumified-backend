package app.scrumifiedbackend.assembler;

import app.scrumifiedbackend.controller.ProjectController;
import app.scrumifiedbackend.dto.ProjectDto;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import java.util.Arrays;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class ProjectDtoEntityAssembler implements RepresentationModelAssembler<ProjectDto, EntityModel<ProjectDto>> {
    @Override
    public EntityModel<ProjectDto> toModel(ProjectDto entity) {
        return EntityModel.of(entity,
                linkTo(methodOn(ProjectController.class).getProject(entity.getId())).withSelfRel().andAffordances(
                        Arrays.asList(
                                afford(methodOn(ProjectController.class).updateProject(entity.getId(), null))
                        )
                ),
                linkTo(methodOn(ProjectController.class).createStory(entity.getId(), null)).withRel("story"),
                linkTo(methodOn(ProjectController.class).getAllStories(entity.getId())).withRel("stories")
        );
    }

    @Override
    public CollectionModel<EntityModel<ProjectDto>> toCollectionModel(Iterable<? extends ProjectDto> entities) {
        return RepresentationModelAssembler.super.toCollectionModel(entities);
    }
}

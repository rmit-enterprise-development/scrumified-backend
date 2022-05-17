package app.scrumifiedbackend.assembler;

import app.scrumifiedbackend.controller.SprintController;
import app.scrumifiedbackend.dto.SprintDto;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class SprintDtoEntityAssembler implements RepresentationModelAssembler<SprintDto, EntityModel<SprintDto>> {

    @Override
    public EntityModel<SprintDto> toModel(SprintDto entity) {
        return EntityModel.of(entity,
                linkTo(methodOn(SprintController.class).getSprint(entity.getId())).withSelfRel()
        );
    }

    @Override
    public CollectionModel<EntityModel<SprintDto>> toCollectionModel(Iterable<? extends SprintDto> entities) {
        return RepresentationModelAssembler.super.toCollectionModel(entities);
    }
}

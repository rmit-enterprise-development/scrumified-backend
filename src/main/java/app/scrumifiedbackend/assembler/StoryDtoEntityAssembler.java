package app.scrumifiedbackend.assembler;

import app.scrumifiedbackend.controller.StoryController;
import app.scrumifiedbackend.dto.StoryDto;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class StoryDtoEntityAssembler implements RepresentationModelAssembler<StoryDto, EntityModel<StoryDto>> {

    @Override
    public EntityModel<StoryDto> toModel (StoryDto entity) {
        return EntityModel.of(entity,
                linkTo(methodOn(StoryController.class).getStory(entity.getId())).withSelfRel()
        );
    }

    @Override
    public CollectionModel<EntityModel<StoryDto>> toCollectionModel (Iterable<? extends StoryDto> entities) {
        return RepresentationModelAssembler.super.toCollectionModel(entities);
    }
}

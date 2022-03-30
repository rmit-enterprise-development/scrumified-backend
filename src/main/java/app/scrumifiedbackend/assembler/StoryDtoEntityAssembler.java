package app.scrumifiedbackend.assembler;

import app.scrumifiedbackend.controller.StoryController;
import app.scrumifiedbackend.dto.StoryDto;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import java.util.Arrays;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class StoryDtoEntityAssembler implements RepresentationModelAssembler<StoryDto, EntityModel<StoryDto>> {

    @Override
    public EntityModel<StoryDto> toModel (StoryDto entity) {
        Long id = entity.getId();
        return EntityModel.of(entity,
                linkTo(methodOn(StoryController.class).getStory(id)).withSelfRel(),
                linkTo(methodOn(StoryController.class).updateStory(id, null)).withRel("stories")
        );
    }

    @Override
    public CollectionModel<EntityModel<StoryDto>> toCollectionModel (Iterable<? extends StoryDto> entities) {
        return RepresentationModelAssembler.super.toCollectionModel(entities);
    }


}

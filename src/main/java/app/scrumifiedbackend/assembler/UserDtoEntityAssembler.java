package app.scrumifiedbackend.assembler;

import app.scrumifiedbackend.controller.UserController;
import app.scrumifiedbackend.dto.UserDto;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class UserDtoEntityAssembler implements RepresentationModelAssembler<UserDto, EntityModel<UserDto>> {

    @Override
    public EntityModel<UserDto> toModel(UserDto entity) {
        return EntityModel.of(entity,
                        linkTo(methodOn(UserController.class).getUser(entity.getId())).withSelfRel()
                );
    }
    @Override
    public CollectionModel<EntityModel<UserDto>> toCollectionModel(Iterable<? extends UserDto> entities) {
        return RepresentationModelAssembler.super.toCollectionModel(entities);
    }
}

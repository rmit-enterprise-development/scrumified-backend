package app.scrumifiedbackend.assembler;

import app.scrumifiedbackend.dto.UserProjectDto;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

@Component
public class UserProjectDtoEntityAssembler implements RepresentationModelAssembler<UserProjectDto, EntityModel<UserProjectDto>> {

    @Override
    public EntityModel<UserProjectDto> toModel(UserProjectDto entity) {
        return EntityModel.of(entity);
    }

    @Override
    public CollectionModel<EntityModel<UserProjectDto>> toCollectionModel(Iterable<? extends UserProjectDto> entities) {
        return RepresentationModelAssembler.super.toCollectionModel(entities);
    }
}

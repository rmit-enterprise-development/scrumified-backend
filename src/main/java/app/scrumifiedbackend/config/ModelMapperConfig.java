package app.scrumifiedbackend.config;

import app.scrumifiedbackend.dto.ProjectDto;
import app.scrumifiedbackend.dto.UserDto;
import app.scrumifiedbackend.entity.Project;
import app.scrumifiedbackend.entity.User;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.modelmapper.TypeMap;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelMapperConfig {
    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration()
                .setMatchingStrategy(MatchingStrategies.STRICT)
                .setSkipNullEnabled(true);

        modelMapper.addMappings(configMapProjectToProjectDto());
        return modelMapper;
    }

    PropertyMap<Project, ProjectDto> configMapProjectToProjectDto() {
        return new PropertyMap<>() {
            @Override
            protected void configure() {
                map().setOwnerId(source.getOwner().getId());
                map().setParticipantsId(source.getParticipatedId());
            }
        };
    }
}

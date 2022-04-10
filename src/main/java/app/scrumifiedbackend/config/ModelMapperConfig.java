package app.scrumifiedbackend.config;

import app.scrumifiedbackend.dto.*;
import app.scrumifiedbackend.entity.*;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

@Configuration
@AllArgsConstructor
public class ModelMapperConfig {
    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration()
                .setMatchingStrategy(MatchingStrategies.STRICT)
                .setSkipNullEnabled(true);

        modelMapper.addMappings(configMapProjectToProjectDto());
        modelMapper.addMappings(configMapStoryToStoryDto());
        modelMapper.addMappings(configMapSprintToSprintDto());
        return modelMapper;
    }

    PropertyMap<Project, ProjectDto> configMapProjectToProjectDto() {
        return new PropertyMap<Project, ProjectDto>() {
            @Override
            protected void configure() {
                map().setOwnerId(source.getOwner().getId());
                map().setParticipantsId(source.getParticipatedId());
            }
        };
    }

    PropertyMap<Story, StoryDto> configMapStoryToStoryDto() {
        return new PropertyMap<Story, StoryDto>() {
            @Override
            protected void configure() {
                map().setProjectId(source.getProject().getId());
                map().setAssignId(source.getAssignee().getId());
            }
        };
    }

    PropertyMap<Sprint, SprintDto> configMapSprintToSprintDto() {
        return new PropertyMap<Sprint, SprintDto>() {
            @Override
            protected void configure() {
                map().setProjectId(source.getId());
            }
        };
    }
}

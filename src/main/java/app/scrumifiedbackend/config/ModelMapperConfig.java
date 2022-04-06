package app.scrumifiedbackend.config;

import app.scrumifiedbackend.dto.ProjectDto;
import app.scrumifiedbackend.dto.StoryDto;
import app.scrumifiedbackend.dto.UserDto;
import app.scrumifiedbackend.entity.Project;
import app.scrumifiedbackend.entity.Story;
import app.scrumifiedbackend.entity.User;
import app.scrumifiedbackend.repository.ProjectRepo;
import app.scrumifiedbackend.repository.UserRepo;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.modelmapper.TypeMap;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Configuration
public class ModelMapperConfig {
    private UserRepo userRepo;
    private ProjectRepo projectRepo;

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration()
                .setMatchingStrategy(MatchingStrategies.STRICT)
                .setSkipNullEnabled(true);

        modelMapper.addMappings(configMapProjectToProjectDto());
        modelMapper.addMappings(configMapProjectDtoToProject());
        modelMapper.addMappings(configMapStoryToStoryDto());
        modelMapper.addMappings(configMapStoryDtoToStory());
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

    PropertyMap<ProjectDto, Project> configMapProjectDtoToProject() {
        return new PropertyMap<>() {
            @Override
            protected void configure() {
                map().setOwner(userRepo.getById(source.getOwnerId()));
                map().setParticipatedUsers(source.getParticipant());
            }
        };
    }

    PropertyMap<Story, StoryDto> configMapStoryToStoryDto() {
        return new PropertyMap<>() {
            @Override
            protected void configure() {
                map().setProjectId(source.getProject().getId());
                map().setAssignId(source.getAssignee().getId());
            }
        };
    }

    PropertyMap<StoryDto, Story> configMapStoryDtoToStory() {
        return new PropertyMap<>() {
            @Override
            protected void configure() {
                map().setProject(projectRepo.getById(source.getProjectId()));
                map().setAssignee(userRepo.getById(source.getAssignId()));
            }
        };
    }
}

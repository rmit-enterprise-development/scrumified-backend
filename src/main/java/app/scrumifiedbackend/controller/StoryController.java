package app.scrumifiedbackend.controller;

import app.scrumifiedbackend.assembler.StoryDtoEntityAssembler;
import app.scrumifiedbackend.dto.StoryDto;
import app.scrumifiedbackend.mapping.MappingJsonValue;
import app.scrumifiedbackend.service.interface_service.StoryService;
import lombok.AllArgsConstructor;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Objects;
import java.util.Set;

@RestController
@RequestMapping
@CrossOrigin
@AllArgsConstructor
public class StoryController {

    private StoryService storyService;
    private StoryDtoEntityAssembler storyDtoEntityAssembler;

    @DeleteMapping("/stories/{storyId}")
    public void deleteStory(@PathVariable("storyId") Long id) {
        Long projectId = storyService.findOne(id).getProjectId();
        storyService.delete(id);
        if (id != null) {
            SseSingletonController.notifySubscriber(projectId, "update");
        }
    }

    @GetMapping("/stories/{storyId}")
    public MappingJacksonValue getStory(@PathVariable("storyId") Long id) {
        StoryDto storyDto = storyService.findOne(id);
        EntityModel<StoryDto> model = storyDtoEntityAssembler.toModel(storyDto);
        HashMap<String, Set<String>> filter = new HashMap<>();
        filter.put("storyFilter", StoryDto.getFilter());
        return new MappingJsonValue<>().returnJson(filter, model);
    }

    @PutMapping("/stories/{storyId}")
    public MappingJacksonValue updateStory(
            @PathVariable("storyId") Long id,
            @RequestBody StoryDto storyDto,
            @RequestParam(name = "isDragged", defaultValue = "false", required = false) Boolean isDragged,
            @RequestParam(name = "isTopDown", required = false) Boolean isTopDown
    ) {
        StoryDto updateStoryDto = isDragged
                ? storyService.dragUpdate(id, storyDto, isTopDown)
                : storyService.update(id, storyDto);
        EntityModel<StoryDto> entityModel = storyDtoEntityAssembler.toModel(updateStoryDto);
        HashMap<String, Set<String>> filter = new HashMap<>();
        filter.put("storyFilter", StoryDto.getFilter());

        if (Objects.requireNonNull(entityModel.getContent()).getProjectId() != null) {
            SseSingletonController.notifySubscriber(entityModel.getContent().getProjectId(), "update");
        }

        return new MappingJsonValue<>().returnJson(filter, entityModel);
    }
}

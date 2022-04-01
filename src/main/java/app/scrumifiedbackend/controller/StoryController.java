package app.scrumifiedbackend.controller;

import app.scrumifiedbackend.assembler.StoryDtoEntityAssembler;
import app.scrumifiedbackend.dto.StoryDto;
import app.scrumifiedbackend.repository.StoryRepo;
import app.scrumifiedbackend.service.interface_service.StoryService;
import org.springframework.hateoas.EntityModel;
import org.springframework.web.bind.annotation.*;

public class StoryController {

    private StoryService storyService;
    private StoryRepo storyRepo;
    private StoryDtoEntityAssembler storyDtoEntityAssembler;

    @DeleteMapping("/stories/{storyId}")
    public void deleteStory (@PathVariable("storyId") Long id) {
        storyService.delete(id);
    }

    @GetMapping("/stories/{storyId}")
    public EntityModel<StoryDto> getStory (@PathVariable("storyId") Long id) {
        return storyDtoEntityAssembler.toModel(storyService.findOne(id));
    }

    @PutMapping("/stories/{storyId}")
    public EntityModel<StoryDto> updateStory (@PathVariable("storyId") Long id, @RequestBody StoryDto storyDto) {
        StoryDto updateStoryDto = storyService.update(id, storyDto);
        return storyDtoEntityAssembler.toModel(updateStoryDto);
    }


}

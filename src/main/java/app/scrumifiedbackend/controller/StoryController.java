package app.scrumifiedbackend.controller;

import app.scrumifiedbackend.dto.StoryDto;
import app.scrumifiedbackend.repository.StoryRepo;
import app.scrumifiedbackend.service.interface_service.StoryService;
import org.springframework.hateoas.EntityModel;
import org.springframework.web.bind.annotation.*;

public class StoryController {

    private StoryService storyService;
    private StoryRepo storyRepo;

    @DeleteMapping("/stories/{storyId}")
    public void deleteStory (@PathVariable("storyId") Long id) {
        storyService.delete(id);
    }

    @GetMapping("/stories/{storyId}")
    public EntityModel<StoryDto> getStory (@PathVariable("storyId") Long id) {
        StoryDto storyDto = storyService.findOne(id);
        // Assembler include!
        return null;
    }

    /*
        updateStory in case that the service is used to convert data from dto to entity form
     */

    @PutMapping("/stories/{storyId}")
    public EntityModel<StoryDto> updateStory (@PathVariable("storyId") Long id, @RequestBody StoryDto storyDto) {
        StoryDto updateStoryDto = storyService.findOne(id);
        if(updateStoryDto.getId().equals(storyDto.getId()) && !updateStoryDto.equals(storyDto)) {
            updateStoryDto = storyService.update(id, storyDto);
        }

        // Assembler include!
        return null;
    }


}

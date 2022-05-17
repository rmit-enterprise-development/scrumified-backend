package app.scrumifiedbackend.controller;

import app.scrumifiedbackend.assembler.SprintDtoEntityAssembler;
import app.scrumifiedbackend.assembler.StoryDtoEntityAssembler;
import app.scrumifiedbackend.dto.*;
import app.scrumifiedbackend.mapping.MappingJsonValue;
import app.scrumifiedbackend.service.interface_service.SprintService;
import app.scrumifiedbackend.service.interface_service.StoryService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping
@CrossOrigin
@AllArgsConstructor
public class SprintController {
    private SprintService sprintService;
    private StoryService storyService;
    private SprintDtoEntityAssembler sprintDtoEntityAssembler;
    private StoryDtoEntityAssembler storyDtoEntityAssembler;

    @GetMapping("/sprints")
    public MappingJacksonValue getAllSprints(
            @RequestParam(name = "key", defaultValue = "", required = false) String key,
            @RequestParam(name = "page", defaultValue = "0", required = false) int page,
            @RequestParam(name = "limit", defaultValue = "4", required = false) int limit,
            @RequestParam(name = "sortProp", defaultValue = "id", required = false) String sortProp,
            @RequestParam(name = "ascending", defaultValue = "false", required = false) boolean isAscending
    ) {
        Pageable pageable = isAscending ?
                PageRequest.of(page, limit, Sort.by(sortProp).ascending()) :
                PageRequest.of(page, limit, Sort.by(sortProp).descending());
        PaginationDto<List<SprintDto>> paginationDto = sprintService.findAll(key, pageable);
        CollectionModel<EntityModel<SprintDto>> collectionModel =
                sprintDtoEntityAssembler.toCollectionModel(paginationDto.getEntity());
        PaginationDto<CollectionModel<EntityModel<SprintDto>>> resultPaginationDto = new PaginationDto<>(
                collectionModel,
                paginationDto.getTotalElements(),
                paginationDto.getTotalPages(),
                paginationDto.getCurrentPage()
        );

        HashMap<String, Set<String>> filter = new HashMap<>();
        filter.put("paginationFilter", PaginationDto.getFilter());
        filter.put("userFilter", UserDto.getFilter("password", "isSuccess", "errorTarget"));

        return new MappingJsonValue<>().returnJson(filter, resultPaginationDto);
    }

    @GetMapping("/sprints/{sprintId}")
    public MappingJacksonValue getSprint(@PathVariable("sprintId") Long id) {
        EntityModel<SprintDto> model = sprintDtoEntityAssembler.toModel(sprintService.findOne(id));
        HashMap<String, Set<String>> filter = new HashMap<>();
        filter.put("sprintFilter", SprintDto.getFilter());
        return new MappingJsonValue<>().returnJson(filter, model);
    }

    @PutMapping("/sprints/{sprintId}")
    public MappingJacksonValue updateSprint(@PathVariable("sprintId") Long id, @RequestBody SprintDto sprintDto) {
        SprintDto updatedSprint = sprintService.update(id, sprintDto);
        EntityModel<SprintDto> model = sprintDtoEntityAssembler.toModel(updatedSprint);
        HashMap<String, Set<String>> filter = new HashMap<>();
        filter.put("sprintFilter", SprintDto.getFilter());
        if (Objects.requireNonNull(model.getContent()).getProjectId() != null) {
            SseSingletonController.notifySubscriber(model.getContent().getProjectId(), "update");
        }
        return new MappingJsonValue<>().returnJson(filter, model);
    }

    @DeleteMapping("/sprints/{sprintId}")
    public void deleteSprint(@PathVariable("sprintId") Long id) {
        storyService.moveStoriesBackToBacklog(id);
        SprintDto sprint = sprintService.findOne(id);
        if (sprint.getProjectId() != null) {
            SseSingletonController.notifySubscriber(sprint.getProjectId(), "update");
        }
        sprintService.delete(id);
    }

    @PostMapping("/sprints/{sprintId}/completed")
    public void completeSprint(@PathVariable("sprintId") Long sprintId) {
        storyService.updateStoriesWhenSprintComplete(sprintId);
        SprintDto sprintDto = new SprintDto();
        sprintDto.setStatus("done");
        sprintService.update(sprintId, sprintDto);
    }

    @GetMapping("/sprints/{sprintId}/stories")
    public MappingJacksonValue getAllStoriesInSprint(@PathVariable(name = "sprintId") Long sprintId) {
        List<StoryDto> storyDtoList = storyService.findAllStoriesBelongToSprint(sprintId);
        Map<Long, EntityModel<StoryDto>> model = new LinkedHashMap<>();
        for (StoryDto storyDto : storyDtoList) {
            model.put(storyDto.getId(), storyDtoEntityAssembler.toModel(storyDto));
        }
        HashMap<String, Set<String>> filter = new HashMap<>();
        filter.put("storyFilter", StoryDto.getFilter());
        return new MappingJsonValue<>().returnJson(filter, model);
    }
}

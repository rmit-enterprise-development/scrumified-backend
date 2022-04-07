package app.scrumifiedbackend.controller;

import app.scrumifiedbackend.assembler.SprintDtoEntityAssembler;
import app.scrumifiedbackend.dto.SprintDto;
import app.scrumifiedbackend.service.interface_service.SprintService;
import lombok.AllArgsConstructor;
import org.springframework.hateoas.EntityModel;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping
@CrossOrigin
@AllArgsConstructor
public class SprintController {
    private SprintService sprintService;
    private SprintDtoEntityAssembler sprintDtoEntityAssembler;

    @GetMapping("/sprints/{sprintId}")
    public EntityModel<SprintDto> getSprint(@PathVariable("sprintId") Long id) {
        return sprintDtoEntityAssembler.toModel(sprintService.findOne(id));
    }

    @PutMapping("/sprints/{sprintId}")
    public EntityModel<SprintDto> updateSprint(@PathVariable("sprintId") Long id, @RequestBody SprintDto sprintDto) {
        System.out.println("A");
        SprintDto updatedSprint = sprintService.update(id, sprintDto);
        System.out.println(updatedSprint);
        return sprintDtoEntityAssembler.toModel(updatedSprint);
    }

    @DeleteMapping("/sprints/{sprintId}")
    public void deleteSprint(@PathVariable("sprintId") Long id) {
        sprintService.delete(id);
    }
}

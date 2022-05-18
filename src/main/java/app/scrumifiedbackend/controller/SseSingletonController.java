package app.scrumifiedbackend.controller;

import lombok.AllArgsConstructor;
import org.modelmapper.internal.util.CopyOnWriteLinkedHashMap;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

@RestController
@RequestMapping
@CrossOrigin
@AllArgsConstructor
public class SseSingletonController {
    private static final Map<Long, CopyOnWriteArrayList<SseEmitter>> emitterMap = new CopyOnWriteLinkedHashMap<>();
    private static Long id = 0L;

    public static void notifySubscriber(Long projectId, String event) {
        ++id;
        List<SseEmitter> emitters = emitterMap.get(projectId);
        if (emitters == null) {
            return;
        }
        for (int i = 0; i < emitters.size(); i++) {
            try {
                emitters.get(i).send(SseEmitter.event().id(String.valueOf(id)).name(event).data(true));
                emitters.get(i).complete();
            } catch (IOException e) {
                System.out.println("Cannot be updated");
                emitters.remove(emitters.get(i));
            }
        }
    }

    @GetMapping(value = "/backlog", consumes = MediaType.ALL_VALUE)
    public SseEmitter subscribe(
            @RequestHeader(name = "Last-Event-ID", required = false) String lastId,
            @RequestParam(name = "projectId") Long projectId
    ) {
        SseEmitter sseEmitter = new SseEmitter(15_000L);
        try {
            sseEmitter.send(SseEmitter.event().name("received").data(true));
        } catch (IOException e) {
            System.out.println("Cannot be handle");
            throw new RuntimeException("Cannot be handle");
        }

        if (!emitterMap.containsKey(projectId)) {
            emitterMap.put(projectId, new CopyOnWriteArrayList<>());
        }
        emitterMap.get(projectId).add(sseEmitter);
        sseEmitter.onCompletion(() -> emitterMap.get(projectId).remove(sseEmitter));
        sseEmitter.onTimeout(() -> emitterMap.get(projectId).remove(sseEmitter));
        if (lastId != null) {
            if (Long.parseLong(lastId) != id) {
                try {
                    sseEmitter.send(SseEmitter.event().id(String.valueOf(++id)).name("update").data(lastId + " " + id.toString()));
                } catch (IOException e) {
                    System.out.println("Cannot be refresh");
                    emitterMap.get(projectId).remove(sseEmitter);
                }
            }
        }
        return sseEmitter;
    }
}


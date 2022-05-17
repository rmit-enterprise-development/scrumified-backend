package app.scrumifiedbackend.config;

import app.scrumifiedbackend.controller.SseSingletonController;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ScheduleTask {
    @Scheduled(fixedRate = 60_000)
    public void wakeUp() {
        System.out.println("Wake up");
    }
}

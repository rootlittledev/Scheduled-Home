package com.example.scheduledhome.controller;

import com.example.scheduledhome.model.ScheduleRequest;
import com.example.scheduledhome.service.SchedulerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@Slf4j
@RestController
public class SchedulerController {

    private final SchedulerService service;

    public SchedulerController(SchedulerService service) {
        this.service = service;
    }

    @PostMapping
    public LocalDateTime schedule(@RequestBody ScheduleRequest request) {
        log.info("Scheduling timer for: {} minutes, device: {}", request.getTimer(), request.getName());

        return service.schedule(request);
    }

    @DeleteMapping("/{name}")
    public void cancel(@PathVariable String name) {
        log.info("Canceling timer for device: {}", name);

        service.cancel(name);
    }
}

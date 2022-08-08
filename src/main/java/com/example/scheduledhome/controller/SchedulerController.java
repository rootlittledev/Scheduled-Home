package com.example.scheduledhome.controller;

import com.example.scheduledhome.model.ScheduleRequest;
import com.example.scheduledhome.service.SchedulerService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
public class SchedulerController {

    private final SchedulerService service;

    public SchedulerController(SchedulerService service) {
        this.service = service;
    }

    @PostMapping
    public LocalDateTime schedule(@RequestBody ScheduleRequest request) {
        return service.schedule(request);
    }

    @PutMapping
    public LocalDateTime updateSchedule(@RequestBody ScheduleRequest request) {
        return service.updateSchedule(request);
    }
}
package com.example.scheduledhome.service;

import com.example.scheduledhome.component.PublisherComponent;
import com.example.scheduledhome.model.ScheduleRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ScheduledFuture;

@Slf4j
@Service
public class SchedulerService {
    private final TaskScheduler taskScheduler;
    private final PublisherComponent publisher;

    private final ConcurrentMap<String, ScheduledFuture<?>> scheduledTimers = new ConcurrentHashMap<>();

    public SchedulerService(TaskScheduler taskScheduler, PublisherComponent publisher) {
        this.taskScheduler = taskScheduler;
        this.publisher = publisher;
    }

    public LocalDateTime updateSchedule(ScheduleRequest request) {
        String deviceName = request.getName();

        scheduledTimers.get(deviceName).cancel(false);

        return schedule(request);
    }

    public LocalDateTime schedule(ScheduleRequest request) {
        LocalDateTime timerOff = LocalDateTime.now().plusMinutes(request.getTimer());
        String deviceName = request.getName();

        Instant schedulerTimer = timerOff.toInstant(ZoneOffset.UTC);
        log.debug("Scheduled timer: {}", LocalDateTime.ofInstant(schedulerTimer, ZoneId.systemDefault()));

        ScheduledFuture<?> task = taskScheduler.schedule(() -> disableTimer(request.getType(), deviceName), timerOff.toInstant(ZoneOffset.ofHours(2)));
        scheduledTimers.put(deviceName, task);
        return timerOff;
    }

    private void disableTimer(String type, String name) {
        log.debug("Timer finished for device: {}", name);
        publisher.publishDisable(type, name);
    }
}

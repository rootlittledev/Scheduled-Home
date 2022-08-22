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

    public LocalDateTime schedule(ScheduleRequest request) {
        String deviceName = request.getName();
        scheduledTimers.get(deviceName);

        if (scheduledTimers.containsKey(deviceName)) {
            log.debug("Update timer for device {}", deviceName);
            cancel(deviceName);
        } else {
            log.debug("Start new timer");
        }

        LocalDateTime timerOff = LocalDateTime.now().plusMinutes(request.getTimer());

        Instant schedulerTimer = timerOff.toInstant(ZoneOffset.UTC);
        log.debug("Scheduled timer: {}", LocalDateTime.ofInstant(schedulerTimer, ZoneId.systemDefault()));

        ScheduledFuture<?> task = taskScheduler.schedule(() -> disableTimer(request.getType(), deviceName), timerOff.toInstant(ZoneOffset.ofHours(2)));
        scheduledTimers.put(deviceName, task);
        return timerOff;
    }

    public void cancel(String deviceName) {
        scheduledTimers.get(deviceName).cancel(false);
    }

    private void disableTimer(String type, String name) {
        log.debug("Timer finished for device: {}", name);
        publisher.publishDisable(type, name);
    }
}

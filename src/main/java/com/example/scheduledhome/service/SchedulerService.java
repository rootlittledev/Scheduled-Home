package com.example.scheduledhome.service;

import com.example.scheduledhome.component.PublisherComponent;
import com.example.scheduledhome.model.ScheduleRequest;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ScheduledFuture;

@Service
public class SchedulerService {
    private final TaskScheduler taskScheduler;
    private final PublisherComponent publisher;

    private ConcurrentMap<String, ScheduledFuture<?>> scheduledTimers = new ConcurrentHashMap<>();

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
        ScheduledFuture<?> task = taskScheduler.schedule(() -> disableTimer(deviceName), timerOff.toInstant(ZoneOffset.UTC));
        scheduledTimers.put(deviceName, task);
        return timerOff;
    }

    private void disableTimer(String name) {
        publisher.publishDisable(name);
    }
}

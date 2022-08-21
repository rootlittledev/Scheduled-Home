package com.example.scheduledhome.model;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class ScheduleRequest {
    private String name;
    private String type;
    private int timer;
}

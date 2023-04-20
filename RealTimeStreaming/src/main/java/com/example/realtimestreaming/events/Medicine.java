package com.example.realtimestreaming.events;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Medicine {
    private int id;
    private String title;
    private int releaseYear;
}

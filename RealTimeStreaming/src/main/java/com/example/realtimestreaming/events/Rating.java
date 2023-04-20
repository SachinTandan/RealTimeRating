package com.example.realtimestreaming.events;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class Rating {
    private int medicineId;
    private int userId;
    private double rating;
}

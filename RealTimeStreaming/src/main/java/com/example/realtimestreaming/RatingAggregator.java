package com.example.realtimestreaming;

class RatingAggregator {
    private double sum;
    private int count;

    public RatingAggregator() {
        this.sum = 0;
        this.count = 0;
    }

    public RatingAggregator addRating(double rating) {
        this.sum += rating;
        this.count++;
        return this;
    }


    public double getAverageRating() {
        if (count == 0) {
            return 0;
        }
        return sum / count;
    }
}


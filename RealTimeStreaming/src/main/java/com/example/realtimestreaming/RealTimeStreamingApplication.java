package com.example.realtimestreaming;

import com.example.realtimestreaming.events.Medicine;
import com.example.realtimestreaming.events.Rating;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.*;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Properties;

@SpringBootApplication
public class RealTimeStreamingApplication {

    public static void main(String[] args) {
        Properties props = new Properties();
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, "movie-rating-app");
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.Integer().getClass());
        props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.Double().getClass());
        StreamsBuilder builder = new StreamsBuilder();

        KStream<Integer, Medicine> medicineStream = builder.stream("medicine", Consumed.with(Serdes.Integer(), JsonSerdes.Json(Medicine.class)));
        KStream<Integer, Rating> ratingStream = builder.stream("ratings", Consumed.with(Serdes.Integer(), JsonSerdes.Json(Rating.class)));

        KTable<Integer, Double> averageRatings = ratingStream
                .groupBy((key, value) -> value.getMedicineId(), Grouped.with(Serdes.Integer(), JsonSerdes.Json(Rating.class)))
                .aggregate(
                        RatingAggregator::new,
                        (key, value, aggregate) -> aggregate.addRating(value.getRating()),
                        Materialized.with(Serdes.Integer(), JsonSerdes.Json(RatingAggregator.class))
                )
                .mapValues(RatingAggregator::getAverageRating);

        KTable<Integer, String> medicineTitles = medicineStream
                .mapValues(Medicine::getTitle)
                .toTable();
//To make use of the movieStream
// we can join it with the averageRatings table to include movie titles in the output.
        KStream<Integer, String> averageRatingsWithTitles = averageRatings
                .toStream()
                .leftJoin(medicineTitles, (avgRating, title) -> title + ": " + avgRating);

        averageRatingsWithTitles.to("average-ratings", Produced.with(Serdes.Integer(), Serdes.String()));

        KafkaStreams streams = new KafkaStreams(builder.build(), props);
        streams.start();

        Runtime.getRuntime().addShutdownHook(new Thread(streams::close));
    }
}

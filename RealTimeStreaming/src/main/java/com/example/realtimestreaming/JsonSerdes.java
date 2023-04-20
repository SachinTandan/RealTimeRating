package com.example.realtimestreaming;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.serialization.Serializer;

public class JsonSerdes {
    public static <T> Serde<T> Json(Class<T> type) {
        ObjectMapper objectMapper = new ObjectMapper();
        Serializer<T> serializer = new JsonSerializer<>(objectMapper);
        Deserializer<T> deserializer = new JsonDeserializer<>(objectMapper, type);
        return Serdes.serdeFrom(serializer, deserializer);
    }
}

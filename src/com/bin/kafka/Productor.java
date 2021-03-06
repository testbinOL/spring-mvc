package com.bin.kafka;

import com.bin.kafka.serializer.avro.User;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.Properties;

public class Productor {
    public static void main(String[] args) {

        //Assign topicName to string variable
        String topicName = "fortest";

        // create instance for properties to access producer configs
        Properties props = new Properties();

        //Assign localhost id
        props.put("bootstrap.servers", "localhost:9092");

        //Set acknowledgements for producer requests.
        props.put("acks", "all");

        //If the request fails, the producer can automatically retry,
        props.put("retries", 0);

        //Specify buffer size in config
        props.put("batch.size", 16384);

        //Reduce the no of requests less than 0
        props.put("linger.ms", 1);

        //The buffer.memory controls the total amount of memory available to the producer for buffering.
        props.put("buffer.memory", 33554432);

        props.put("key.serializer",
                "org.apache.kafka.common.serialization.StringSerializer");

        //props.put("value.serializer","org.apache.kafka.common.serialization.StringSerializer");
        //props.put("value.serializer", "com.bin.kafka.serializer.common.MessageSerializer");
        props.put("value.serializer", "com.bin.kafka.serializer.avro.AvroSerializer");


        Producer<String, Object> producer = new KafkaProducer<>(props);

        for (int i = 300; i < 320; i++) {
            User user = new User();
            user.setAge(12);
            user.setName("user:" + i);
            user.setPhone(Integer.toString(i));
            ProducerRecord<String, Object> record = new ProducerRecord(topicName,
                    Integer.toString(i), user);
            producer.send(record);
        }
        System.out.println("Message sent successfully");
        producer.close();
    }
}

package me.shubhamjain.codesamples.gcp.pubsub;

import com.google.cloud.pubsub.v1.Publisher;
import com.google.protobuf.ByteString;
import com.google.pubsub.v1.PubsubMessage;
import com.google.pubsub.v1.TopicName;

import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.stream.IntStream;

public class PublishMessages {
    private static final String message = """
                                {
                                  "college_id": "mock",
                                  "endpoint": "/open-banking/v3.1/aisp/accounts/1bd762a1-5483-45sdkjnclkjdsnclkm380e11eb/balances",
                                  "status": 200,
                                  "success": true,
                                  "version": {"string": "V1.1"},
                                  "response": {"string": "null"},
                                  "error_message":{"string": "null"},
                                  "tracing_id": {"string": "cnkjdsancklds"},
                                  "application_id": {"string": "2kcnsdkjcndskl0e"},
                                  "created_at": { "string": "2024-01-22T18:46:00.094" }
                                }
                                """;
    public static void publishMessages(String project, String topic, String message) throws IOException {
        Publisher publisher = Publisher.newBuilder(TopicName.of(project, topic))
                .build();
        PubsubMessage pubsubMessage = PubsubMessage
                .newBuilder()
                .setData(ByteString.copyFromUtf8(message))
                .build();
        try {
            String messagePublished = publisher.publish(pubsubMessage).get();
            System.out.println(messagePublished);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) throws IOException {

        IntStream.range(1, 10).forEach(i -> {
            try {
                publishMessages("test-project-development", "extract", message);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

    }
}

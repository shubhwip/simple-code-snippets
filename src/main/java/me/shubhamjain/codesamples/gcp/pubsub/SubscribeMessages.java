package me.shubhamjain.codesamples.gcp.pubsub;

import com.google.cloud.pubsub.v1.AckReplyConsumer;
import com.google.cloud.pubsub.v1.MessageReceiver;
import com.google.cloud.pubsub.v1.Subscriber;
import com.google.pubsub.v1.PubsubMessage;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.Option;
import me.shubhamjain.codesamples.httpclient.JavaHttpClient;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class SubscribeMessages {

    public static void subscribeMessages(String subscriptionName) {
        MessageReceiver receiver =
                (PubsubMessage message, AckReplyConsumer consumer) -> {
//                    String traceId = JsonPath
//                            .using(Configuration.builder()
//                                    .options(Option.SUPPRESS_EXCEPTIONS).options(Option.DEFAULT_PATH_LEAF_TO_NULL).build())
//                            .parse(message.getData().toStringUtf8()).read("$.traceId");
                    System.out.println(message);
                    JavaHttpClient.post(message.getData().toStringUtf8(), "http://localhost:8000", "/api/v1/logstream/aspsp", "", "X-P-META-originator", message.getAttributesOrDefault("originator", "not-found"), "X-P-META-collegeId", message.getAttributesOrDefault("collegeId", "not-found"), "X-P-META-collegeId", message.getAttributesOrDefault("applicationId", "not-found"), "Authorization", "Basic YWRtaW46YWRtaW4=", "Content-Type", "/application/json");
                    // System.out.println(traceId);
                };

        Subscriber subscriber = null;
        try {
            subscriber = Subscriber.newBuilder(subscriptionName, receiver).build();
            // Start the subscriber.
            subscriber.startAsync().awaitRunning();
            System.out.printf("Listening for messages on %s:\n", subscriptionName.toString());
            // Allow the subscriber to run for 30s unless an unrecoverable error occurs.
            subscriber.awaitTerminated(40000000, TimeUnit.SECONDS);
        } catch (TimeoutException timeoutException) {
            // Shut down the subscriber after 30s. Stop receiving messages.
            subscriber.stopAsync();
        }
    }
    public static void main(String[] args) {
        subscribeMessages("projects/test-project-production/subscriptions/delete-sink");
    }
}

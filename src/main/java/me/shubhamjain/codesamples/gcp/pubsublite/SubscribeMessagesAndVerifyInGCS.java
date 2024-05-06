package me.shubhamjain.codesamples.gcp.pubsublite;

import com.google.cloud.pubsub.v1.AckReplyConsumer;
import com.google.cloud.pubsub.v1.MessageReceiver;
import com.google.cloud.pubsublite.*;
import com.google.cloud.pubsublite.cloudpubsub.FlowControlSettings;
import com.google.cloud.pubsublite.cloudpubsub.Subscriber;
import com.google.cloud.pubsublite.cloudpubsub.SubscriberSettings;
import com.google.cloud.pubsublite.proto.Topic;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import com.google.protobuf.util.Durations;
import com.google.pubsub.v1.PubsubMessage;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.Option;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static java.nio.charset.StandardCharsets.UTF_8;

public class SubscribeMessagesAndVerifyInGCS {

    // TODO(developer): Replace these variables with your own.
    private static final String projectId = "test-project-development";
    private static final String cloudRegion = "europe-west2";
    public static final String subscriptionId = "test-subscription-pubsub-lite-to-be-deleted-de-1574";
    private static final String topicId = "test-pubsub-lite-to-be-deleted-de-1574";
    private static final Integer partitions = 1;

    private static final String bucketName = "dev-euwe2-raw-requests-data-stream-tracinglogs-lxi";

    //private static final String bucketName = "prod-euwe2-raw-requests-data-stream-tracinglogs-gsn";

    private static final TopicPath topicPath =
            TopicPath.newBuilder()
                    .setProject(ProjectId.of(projectId))
                    .setLocation(CloudRegion.of(cloudRegion))
                    .setName(TopicName.of(topicId))
                    .build();

    private static final Topic topic =
            Topic.newBuilder()
                    .setPartitionConfig(
                            Topic.PartitionConfig.newBuilder()
                                    // Set publishing throughput to 1 times the standard partition
                                    // throughput of 4 MiB per sec. This must be in the range [1,4]. A
                                    // topic with `scale` of 2 and count of 10 is charged for 20 partitions.
                                    .setScale(1)
                                    .setCount(partitions))
                    .setRetentionConfig(
                            Topic.RetentionConfig.newBuilder()
                                    // How long messages are retained.
                                    .setPeriod(Durations.fromDays(1))
                                    // Set storage per partition to 30 GiB. This must be 30 GiB-10 TiB.
                                    // If the number of bytes stored in any of the topic's partitions grows
                                    // beyond this value, older messages will be dropped to make room for
                                    // newer ones, regardless of the value of `period`.
                                    .setPerPartitionBytes(30 * 1024 * 1024 * 1024L))
                    .setName(topicPath.toString())
                    .build();

    private static final AdminClientSettings adminClientSettings =
            AdminClientSettings.newBuilder().setRegion(CloudRegion.of(cloudRegion)).build();



    public static void subscribeMessages() {
        SubscriptionPath subscriptionPath =
                SubscriptionPath.newBuilder()
                        .setLocation(CloudRegion.of(cloudRegion))
                        .setProject(ProjectId.of(projectId))
                        .setName(SubscriptionName.of(subscriptionId))
                        .build();

// The message stream is paused based on the maximum size or number of messages that the
// subscriber has already received, whichever condition is met first.
        FlowControlSettings flowControlSettings =
                FlowControlSettings.builder()
                        // 10 MiB. Must be greater than the allowed size of the largest message (1 MiB).
                        .setBytesOutstanding(10 * 1024 * 1024L)
                        // 1,000 outstanding messages. Must be >0.
                        .setMessagesOutstanding(1000L)
                        .build();
        Storage storage = StorageOptions.newBuilder().setProjectId(projectId).build().getService();
        MessageReceiver receiver =
                (PubsubMessage message, AckReplyConsumer consumer) -> {
                    System.out.println("Id : " + message.getMessageId());
                    System.out.println("Data : " + message.getData().toStringUtf8());
                    String json = message.getData().toStringUtf8();
                    DocumentContext documentContext = JsonPath
                            .using(Configuration.builder()
                                    .options(Option.SUPPRESS_EXCEPTIONS).options(Option.DEFAULT_PATH_LEAF_TO_NULL).build())
                            .parse(message.getData().toStringUtf8());
                    var logType = documentContext.read("$.log_type");
                    var applicationId = documentContext.read("$.application_id");
                    var tracingId = documentContext.read("$.tracing_id");
                    if(logType.equals("test-project_request")) {
                        var blob = storage.get(BlobId.of(bucketName, String.format("%s/%s/%s", tracingId, applicationId, "test-project-request")));
                        if(!blob.exists()) {
                            System.out.println("test-project_request blob doesn't exist " + blob.getName());
                        }
                    } else if(logType.equals("college_request")) {
                        var blob = storage.get(BlobId.of(bucketName, String.format("%s/%s/%s", tracingId, applicationId, "college-request")));
                        if(!blob.exists()) {
                            System.out.println("college_request blob doesn't exist " + blob.getName());
                        }
                    }
                    //consumer.ack();
                };

        SubscriberSettings subscriberSettings =
                SubscriberSettings.newBuilder()
                        .setSubscriptionPath(subscriptionPath)
                        .setReceiver(receiver)
                        // Flow control settings are set at the partition level.
                        .setPerPartitionFlowControlSettings(flowControlSettings)
                        .build();

        Subscriber subscriber = Subscriber.create(subscriberSettings);

// Start the subscriber. Upon successful starting, its state will become RUNNING.
        subscriber.startAsync().awaitRunning();

        System.out.println("Listening to messages on " + subscriptionPath.toString() + "...");

        try {
            System.out.println(subscriber.state());
            // Wait 90 seconds for the subscriber to reach TERMINATED state. If it encounters
            // unrecoverable errors before then, its state will change to FAILED and an
            // IllegalStateException will be thrown.
            subscriber.awaitTerminated(90, TimeUnit.SECONDS);
        } catch (TimeoutException t) {
            // Shut down the subscriber. This will change the state of the subscriber to TERMINATED.
            subscriber.stopAsync().awaitTerminated();
            System.out.println("Subscriber is shut down: " + subscriber.state());
        }
    }

    public static void main(String[] args) {
        subscribeMessages();
    }
}

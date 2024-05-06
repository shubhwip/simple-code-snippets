package me.shubhamjain.codesamples.gcp.pubsublite;

import com.google.api.core.ApiFuture;
import com.google.api.core.ApiFutures;
import com.google.cloud.pubsub.v1.AckReplyConsumer;
import com.google.cloud.pubsub.v1.MessageReceiver;
import com.google.cloud.pubsublite.*;
import com.google.cloud.pubsublite.cloudpubsub.*;
import com.google.cloud.pubsublite.proto.Topic;
import com.google.protobuf.ByteString;
import com.google.protobuf.util.Durations;
import com.google.pubsub.v1.PubsubMessage;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class SubscribeMessages {

    // TODO(developer): Replace these variables with your own.
    private static final String projectId = "test-project-development";
    private static final String cloudRegion = "europe-west2";
    private static final char zoneId = 'b';
    public static final String subscriptionId = "test-subscription-pubsub-lite-to-be-deleted-de-1574";
    private static final String topicId = "test-pubsub-lite-to-be-deleted-de-1574";
    private static final Integer partitions = 1;

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

        MessageReceiver receiver =
                (PubsubMessage message, AckReplyConsumer consumer) -> {
                    System.out.println("Id : " + message.getMessageId());
                    System.out.println("Data : " + message.getData().toStringUtf8());
                    consumer.ack();
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

package me.shubhamjain.codesamples.akka;

import akka.NotUsed;
import akka.actor.ActorSystem;
import akka.stream.alpakka.googlecloud.pubsub.grpc.javadsl.GooglePubSub;
import akka.stream.javadsl.Flow;
import akka.stream.javadsl.Sink;
import akka.stream.javadsl.Source;
import com.google.protobuf.ByteString;
import com.google.pubsub.v1.PublishRequest;
import com.google.pubsub.v1.PublishResponse;
import com.google.pubsub.v1.PubsubMessage;

import java.util.List;
import java.util.concurrent.CompletionStage;

// Example taken from
// https://doc.akka.io/docs/alpakka/current/google-cloud-pub-sub-grpc.html
public class PubSubPublishGrpc {

    public static void main(String[] args) {
        String message = """
                {
                  "message": "jsonMessage"
                }
                """;
        String topic = "projects/test-project-development/topics/to-be-deleted-test-project-request-data-test-project";
        ActorSystem system = ActorSystem.create();

        final PubsubMessage publishMessage =
                PubsubMessage.newBuilder().setData(ByteString.copyFromUtf8(message)).build();

        final PublishRequest publishRequest =
                PublishRequest.newBuilder()
                        .setTopic(topic)
                        .addMessages(publishMessage)
                        .build();

        final Source<PublishRequest, NotUsed> source = Source.single(publishRequest);

        final Flow<PublishRequest, PublishResponse, NotUsed> publishFlow = GooglePubSub.publish(1);

        final CompletionStage<List<PublishResponse>> publishedMessageIds =
                source.via(publishFlow).runWith(Sink.seq(), system);


    }
}

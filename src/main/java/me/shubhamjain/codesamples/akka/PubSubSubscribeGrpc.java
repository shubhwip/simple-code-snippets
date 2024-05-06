package me.shubhamjain.codesamples.akka;

import akka.Done;
import akka.actor.Cancellable;
import akka.actor.typed.ActorSystem;
import akka.actor.typed.javadsl.Behaviors;
import akka.stream.alpakka.googlecloud.pubsub.grpc.javadsl.GooglePubSub;
import akka.stream.javadsl.Keep;
import akka.stream.javadsl.Sink;
import akka.stream.javadsl.Source;
import com.google.pubsub.v1.PullRequest;
import com.google.pubsub.v1.ReceivedMessage;
import lombok.extern.slf4j.Slf4j;

import java.time.Duration;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

// Example taken from
// https://doc.akka.io/docs/alpakka/current/google-cloud-pub-sub-grpc.html
@Slf4j
public class PubSubSubscribeGrpc {

    public static void main(String[] args) {
        String topic = "projects/test-project-development/topics/to-be-deleted-test-project-request-data-test-project";
        String subscription = "projects/test-project-development/subscriptions/to-be-deleted-test-project-request-data-test-project-sub";
        ActorSystem system = akka.actor.typed.ActorSystem.apply(Behaviors.receive(
                (context, message) -> {
                    context.getLog().info("Guardian actor received message");
                    return Behaviors.same();
                }), "sample-class");

        var request = PullRequest.newBuilder()
                .setSubscription(subscription)
                .setMaxMessages(10)
                .build();

        var pollInterval = Duration.ofMillis(100);
        Source<ReceivedMessage, CompletableFuture<Cancellable>> subscriptionSource =
                GooglePubSub.subscribePolling(request, pollInterval);
        subscriptionSource.toMat(Sink.foreach(a -> log.info("Received message: " + a)), Keep.right()).run(system);


// Keep the ActorSystem running to prevent it from shutting down immediately
//        ActorSystem system = ActorSystem.create();
//        var request =
//                PullRequest.newBuilder()
//                        .setSubscription(subscription)
//                        .setMaxMessages(10)
//                        .build();
//
//        var pollInterval = Duration.ofMillis(1);
//        Source<ReceivedMessage, CompletableFuture<Cancellable>> subscriptionSource =
//                GooglePubSub.subscribePolling(request, pollInterval);
//
//        subscriptionSource.to(Sink.foreach(a-> System.out.println("Afaib " + a))).run(system);
//        subscriptionSource.runWith(Sink.foreach(a -> log.info("Printing" + a)), system);
//        subscriptionSource.runWith(Sink.foreach(a -> log.info("Printing Again" + a)), system);

        Sink<Object, CompletionStage<Done>> sink = Sink.foreach(a -> System.out.println(a));
        subscriptionSource
                .map(message -> {
                            // do something fun
                            log.info("Message Received " + message);
                            return message.getAckId();
                        })
                .to(Sink.foreach(a-> log.info("Ack Id " + a)))
                .run(system);




    }
}

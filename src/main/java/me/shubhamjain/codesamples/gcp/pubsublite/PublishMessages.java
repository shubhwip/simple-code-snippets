package me.shubhamjain.codesamples.gcp.pubsublite;

import com.google.api.core.ApiFuture;
import com.google.api.core.ApiFutures;
import com.google.cloud.pubsublite.*;
import com.google.cloud.pubsublite.cloudpubsub.Publisher;
import com.google.cloud.pubsublite.cloudpubsub.PublisherSettings;
import com.google.cloud.pubsublite.proto.Topic;
import com.google.protobuf.ByteString;
import com.google.protobuf.util.Durations;
import com.google.pubsub.v1.PubsubMessage;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class PublishMessages {

    // TODO(developer): Replace these variables with your own.
    private static final String projectId = "test-project-development";
    private static final String cloudRegion = "europe-west2";
    private static final char zoneId = 'b';
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



    public static void publishMessage() {
        int messageCount = 2;
        Publisher publisher = null;
        List<ApiFuture<String>> futures = new ArrayList<>();

        try {
            PublisherSettings publisherSettings =
                    PublisherSettings.newBuilder().setTopicPath(topicPath).build();

            publisher = Publisher.create(publisherSettings);


            // Start the publisher. Upon successful starting, its state will become RUNNING.
            publisher.startAsync().awaitRunning();

            for (int i = 0; i < messageCount; i++) {
                String message = """
                        {"status":201,"end":"2023-11-22T08:04:25.625Z","result":{"data":{"createdAt":"2023-11-22T08:04:25.033Z","charges":[{"chargeTo":"BorneByDebtor","chargeType":"UK.OBIE.MoneyTransferOut","chargeAmount":{"amount":1.88,"currency":"GBP"}}],"collegeId":"mock-sandbox-v1","qrCodeUrl":"https://images.test-project.com/image/9d274835-f983-4f6e-8bcb-1d02b11b2b85/1700640265?size=0","userUuid":"b4ceb20f-6fb2-47aa-a6ca-27fca3f9cb66","applicationUserId":"68d87ae2-061d-467f-b0fc-e99d5edde953f92cfb5a-7013-4d6e-bfce-2883f28cf3ce","id":"37101b4e-71e3-42b6-be6c-49239b11336b","state":"09ca3b9aada84ca4a3fc2acec1fb89b4","featureScope":["EXISTING_PAYMENTS_DETAILS","EXISTING_PAYMENT_INITIATION_DETAILS","CREATE_BULK_PAYMENT"],"authorisationUrl":"https://uk.mock.master.p2.test-project.com/oauth2/authorize?client_id=asxasxasxascsdgv&response_type=code+id_token&state=09ca3b9aada84ca4a3fc2acec1fb89b4&nonce=09ca3b9aada84ca4a3fc2acec1fb89b4&scope=openid+clicks&redirect_uri=https%3A%2F%2Fdevelopment-auth.test-project.com%2F&request=eyJraWQiOiIxNTA2MTIyMzExIiwidHlwIjoiSldUIiwiYWxnIjoiUlMyNTYifQ.eyJhdWQiOiJodHRwczovL3VrLm1vY2subWFzdGVyLnAyLnlhcGlseS5jb20vb2F1dGgyIiwic2NvcGUiOiJvcGVuaWQgcGF5bWVudHMiLCJpc3MiOiJhc3hhc3hhc3hhc2NzZGd2IiwiY2xpZW50X2lkIjoiYXN4YXN4YXN4YXNjc2RndiIsInJlc3BvbnNlX3R5cGUiOiJjb2RlIGlkX3Rva2VuIiwicmVkaXJlY3RfdXJpIjoiaHR0cHM6Ly9kZXZlbG9wbWVudC1hdXRoLnlhcGlseS5jb20vIiwic3RhdGUiOiIwOWNhM2I5YWFkYTg0Y2E0YTNmYzJhY2VjMWZiODliNCIsImNsYWltcyI6eyJpZF90b2tlbiI6eyJhY3IiOnsidmFsdWUiOiJ1cm46b3BlbmJhbmtpbmc6cHNkMjpzY2EiLCJlc3NlbnRpYWwiOnRydWV9LCJvcGVuYmFua2luZ19pbnRlbnRfaWQiOnsidmFsdWUiOiJzZHAtNi1mNGVmZTY3NS1kODEyLTRiYWYtYTJmNS0xYmJkZjcxNGQzNTEiLCJlc3NlbnRpYWwiOnRydWV9fSwidXNlcmluZm8iOnsib3BlbmJhbmtpbmdfaW50ZW50X2lkIjp7InZhbHVlIjoic2RwLTYtZjRlZmU2NzUtZDgxMi00YmFmLWEyZjUtMWJiZGY3MTRkMzUxIiwiZXNzZW50aWFsIjp0cnVlfX19LCJub25jZSI6IjA5Y2EzYjlhYWRhODRjYTRhM2ZjMmFjZWMxZmI4OWI0IiwianRpIjoiOTJhNTViMTgtY2IwNS00OWFmLWJkMTktZjQ2YjQ2Mjc0MDE2IiwiaWF0IjoxNzAwNjQwMjY1LCJleHAiOjE3MDA2NDIwNjV9.ktNyTm3_LVLdJ5dF6j5qL2QEKaaquwgYAirufxOJ00TWg0tu-yP7WiltSVEr2YXAoEvsNYUmQVAu4DxqdzjJj_xeAJ0a7fKEsT1zgOFLCAs-na2T64IKGD1ivskVq2w3QhY_sKHL8wPrGG2dJmOWSW_JSaqgCCa__1Wt0PO9cLcA9lSeRiEf4bsfoYxYmAiY9f6U4lnHN07iBkJno89_9yScjS9BVHMQKmHiPzaRtTsTb0bJJvXlb-mk_y93yo_RlE1PJnT8DeNYPj1lMxrnAimE6woQkNSkOvTYQzdCc6KXeKfhrLLQOjZv6JEGj17Wyyz8hWfwQ89l1RbbH9iZJA","status":"AWAITING_AUTHORIZATION","collegeConsentId":"sdp-6-f4efe675-d812-4baf-a2f5-1bbdf714d351"},"meta":{"tracingId":"001344ed4541456aa6d87d0749bf17d0"}},"headers":{"test-project-Tracing-Id":"001344ed4541456aa6d87d0749bf17d0","Date":"Wed, 22 Nov 2023 08:04:24 GMT","Content-Type":"application/json;charset=utf-8"},"request":{"method":"POST","url":"http://test-project-api-service.development.svc.cluster.local/bulk-click-auth-requests","requestInstant":"2023-11-22T08:04:24.821Z","headers":{"X-Request-ID":"7839a4105ee8576ebcfd598408dbd1b2","User-Agent":"Apache-HttpClient/4.5.5 (Java/1.8.0_332)","Accept-Encoding":"gzip,deflate","x-test-project-redirect":"http://test-project-api-service.development.svc.cluster.local:80/bulk-click-auth-requests?raw=true","test-project.request.method":"POST","x-test-project-platform":"P1","test-project.application.id":"c5a25c06-ec79-4df4-8da3-0219e6012c34","X-B3-Sampled":"1",a"test-project-tracing-id":"001344ed4541456aa6d87d0749bf17d0","test-project.organisation.id":"585c2277-f08a-486d-ad36-d710ef380e61","X-B3-TraceId":"7f517d45fb35e777","test-project.request.path":"/bulk-click-auth-requests","x-test-project-identity":"eyJraWQiOiI3MmFmYjM5MS03MzRiLTQ4OTEtOWM4NC01OTgzODdiNmVkMWIiLCJhbGciOiJFUzI1NiJ9.eyJhdWQiOiJkZXZlbG9wbWVudCIsInN1YiI6ImM1YTI1YzA2LWVjNzktNGRmNC04ZGEzLTAyMTllNjAxMmMzNCIsIm9yZyI6IjU4NWMyMjc3LWYwOGEtNDg2ZC1hZDM2LWQ3MTBlZjM4MGU2MSIsImlzcyI6ImdhdGV3YXktYXBpIiwidHlwIjoiQVBQTElDQVRJT04iLCJleHAiOjE3MDA2NDA4NjQsImlhdCI6MTcwMDY0MDI2NCwianRpIjoiMjhmMzdkNDQtY2E2Zi00MjQxLWI3MjYtMmVmYWFlNTI1NDhlIn0.f9VK3T0qPX39DSobY_5x9TAZeBdiWenjbfwGLFdbBIfjnHjtXAm3Bo0Nv87DNvoB-zgvCEf4vfU_gE0zW1Hwww","Content-Length":"1676","test-project.trace":"001344ed4541456aa6d87d0749bf17d0","X-Real-IP":"34.236.25.177","test-project.request.host":"development-api.test-project.com","Content-Type":"application/json","x-test-project-application-configuration":"{\\"collegesConfiguration\\":[],\\"configurationProperties\\":[{\\"key\\":\\"can.have.merchant.apps\\",\\"value\\":\\"false\\"},{\\"key\\":\\"click.monitoring.enabled\\",\\"value\\":\\"false\\"},{\\"key\\":\\"debug.mode\\",\\"value\\":\\"MAXIMUM\\"},{\\"key\\":\\"is.monitored\\",\\"value\\":\\"false\\"}]}","test-project.college.id":"mock-sandbox-v1","Accept":"application/json, application/javascript, text/javascript, text/json","X-Forwarded-Host":"development-api.test-project.com,development-api.test-project.com,colleges-lb.integration.svc.cluster.local","X-Forwarded-Proto":"https,https,https","X-B3-ParentSpanId":"298063f9aaf305f2","Host":"test-project-api-service.development.svc.cluster.local","X-Forwarded-Port":"443,443,443","X-B3-SpanId":"8d1a843d07332fac","Forwarded":"proto=https;host=development-api.test-project.com;for=\\"34.236.25.177:35096\\"","X-Forwarded-For":"34.236.25.177,34.236.25.177,34.236.25.177","test-project.platform.id":"P1","X-Forwarded-Scheme":"https","test-project.user.id":"b4ceb20f-6fb2-47aa-a6ca-27fca3f9cb66","X-Scheme":"https"},"body":{"forwardParameters":[],"collegeId":"mock-sandbox-v1","userUuid":"b4ceb20f-6fb2-47aa-a6ca-27fca3f9cb66","applicationUserId":"68d87ae2-061d-467f-b0fc-e99d5edde953f92cfb5a-7013-4d6e-bfce-2883f28cf3ce","clickRequest":{"clicks":[{"reference":"Test Bulk International Payment","payee":{"accountIdentifications":[{"identification":"AIBKIEDXXX","type":"BIC"},{"identification":"GB11OOYE04114900188791","type":"IBAN"}],"address":{"country":"GB","county":[],"addressLines":["Address Line 1","Address Line 2"]},"name":"TestPayee"},"amount":{"amount":15000,"currency":"GBP"},"clickIdempotencyId":"7e897b8a199e4e9b81b796d4ac694a3a","internationalPayment":{"currencyOfTransfer":"GBP"},"type":"INTERNATIONAL_PAYMENT"},{"reference":"Test Bulk International Payment","payee":{"accountIdentifications":[{"identification":"AIBKIEDXXX","type":"BIC"},{"identification":"GB11OOYE04114900188791","type":"IBAN"}],"address":{"country":"GB","county":[],"addressLines":["Address Line 1","Address Line 2"]},"name":"TestPayee"},"amount":{"amount":7000,"currency":"GBP"},"clickIdempotencyId":"a1a750e48d494a7e80b1683e08d83293","internationalPayment":{"currencyOfTransfer":"GBP"},"type":"INTERNATIONAL_PAYMENT"},{"reference":"Test Bulk International Payment","payee":{"accountIdentifications":[{"identification":"GB11OOYE04114900188791","type":"IBAN"},{"identification":"AIBKIEDXXX","type":"BIC"}],"address":{"country":"GB","county":[],"addressLines":["Address Line 1","Address Line 2"]},"name":"TestPayee"},"amount":{"amount":3000,"currency":"GBP"},"clickIdempotencyId":"e016c3e76f2c43e7bcb8fe55967d554e","internationalPayment":{"currencyOfTransfer":"GBP"},"type":"INTERNATIONAL_PAYMENT"}]}},"bodyParameters":{"raw":"[true]"},"startTime":"2023-11-22T08:04:24.821Z"}}
                        """;

                // Convert the message to a byte string.
                ByteString data = ByteString.copyFromUtf8(message);
                PubsubMessage pubsubMessage = PubsubMessage.newBuilder().setData(data).build();

                // Publish a message. Messages are automatically batched.
                ApiFuture<String> future = publisher.publish(pubsubMessage);
                futures.add(future);
            }
        } finally {
            ArrayList<MessageMetadata> metadata = new ArrayList<>();
            List<String> ackIds = null;
            try {
                ackIds = ApiFutures.allAsList(futures).get();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } catch (ExecutionException e) {
                throw new RuntimeException(e);
            }
            for (String id : ackIds) {
                // Decoded metadata contains partition and offset.
                metadata.add(MessageMetadata.decode(id));
            }
            System.out.println(metadata + "\nPublished " + ackIds.size() + " messages.");

            if (publisher != null) {
                // Shut down the publisher.
                publisher.stopAsync().awaitTerminated();
                System.out.println("Publisher is shut down.");
            }
        }
    }

    public static void main(String[] args) {
        publishMessage();
    }
}

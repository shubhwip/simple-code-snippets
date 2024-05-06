//package me.shubhamjain.codesamples.protobuf;
//
//import com.google.protobuf.Message;
//import com.google.protobuf.MessageOrBuilder;
//import com.google.protobuf.Struct;
//import com.google.protobuf.util.JsonFormat;
//
//import java.io.IOException;
//
//public class ProtobufToJsonConversion {
//
//    public static String toJson(MessageOrBuilder messageOrBuilder) throws IOException {
//        return JsonFormat.printer().print(messageOrBuilder);
//    }
//
//    public static Message fromJson(String json) throws IOException {
//        Message.Builder structBuilder = Struct.newBuilder();
//        JsonFormat.parser().ignoringUnknownFields().merge(json, structBuilder);
//        return structBuilder.build();
//    }
//
//    public static void main(String[] args) {
//        byte[] data = new String("\n\x8e\x06\n\x97\x01\n)\n\x0cservice.name\x12\x19\n\x17OtlpExporterExampleHttp\n \n\x16telemetry.sdk.language\x12\x06\n\x04java\n%\n\x12telemetry.sdk.name\x12\x0f\n\ropentelemetry\n!\n\x15telemetry.sdk.version\x12\x08\n\x061.26.0\x12\xa1\x03\n\x1a\n\x18io.opentelemetry.example\x124\n\x0fexample_counter:!\n\x1b\x11\x18\xa4\xb3\x1d)\x8be\x17\x19 x\x04\x02,\x8be\x171d\0\0\0\0\0\0\0\x10\x02\x18\x01\x12\xcc\x02\n\x0bsuper_timer\x1a\x02msJ\xb8\x02\n\xb3\x02\x11\x18\xa4\xb3\x1d)\x8be\x17\x19 x\x04\x02,\x8be\x17!d\0\0\0\0\0\0\0)\0\0\0\0\x80A\xc4@2\x80\x01\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\x03\0\0\0\0\0\0\0a\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0\0:x\0\0\0\0\0\0\0\0\0\0\0\0\0\0\x14@\0\0\0\0\0\0$@\0\0\0\0\0\09@\0\0\0\0\0\0I@\0\0\0\0\0\xc0R@\0\0\0\0\0\0Y@\0\0\0\0\0@o@\0\0\0\0\0@\x7f@\0\0\0\0\0p\x87@\0\0\0\0\0@\x8f@\0\0\0\0\0\x88\xa3@\0\0\0\0\0\x88\xb3@\0\0\0\0\0L\xbd@\0\0\0\0\0\x88\xc3@Y\0\0\0\0\0\0Y@a\0\0\0\0\0\0]@\x10\x02\x12\xcd\x01\n&\n$io.opentelemetry.exporters.otlp-http\x12Z\n\x16otlp.exporter.exported:@\n:\x11\x18\xa4\xb3\x1d)\x8be\x17\x19 x\x04\x02,\x8be\x171d\0\0\0\0\0\0\0:\r\n\x07success\x12\x02\x10\x01:\x0e\n\x04type\x12\x06\n\x04span\x10\x02\x18\x01\x12G\n\x12otlp.exporter.seen:1\n+\x11\x18\xa4\xb3\x1d)\x8be\x17\x19 x\x04\x02,\x8be\x171d\0\0\0\0\0\0\0:\x0e\n\x04type\x12\x06\n\x04span\x10\x02\x18\x01").getBytes();
//
//        try {
//            ExampleMessage message = ExampleMessage.parseFrom(data);
//
//            // Access the fields of the message
//            String serviceName = message.getService().getName();
//            String sdkLanguage = message.getTelemetry().getSdk().getLanguage();
//            // ... access other fields as needed
//
//            // Print the values
//            System.out.println("Service Name: " + serviceName);
//            System.out.println("SDK Language: " + sdkLanguage);
//            // ... print other fields as needed
//        } catch (InvalidProtocolBufferException e) {
//            e.printStackTrace();
//        }
//    }
//}

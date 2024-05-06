//package me.shubhamjain.codesamples.protobuf;
//
//import com.google.protobuf.Message;
//import com.googlecode.protobuf.format.JsonFormat;
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.Test;
//
//import java.io.IOException;
//import java.nio.charset.StandardCharsets;
//
//class ProtobufToJsonConversionTest {
//
//    String jsonStr = """
//            {
//                "boolean": true,
//                "color": "gold",
//                "object": {
//                  "a": "b",
//                  "c": "d"
//                },
//                "string": "Hello World"
//            }
//            """;
//
//    String protoBufJson = """
//            \\n\\x99\\x02\\n\\x93\\x01\\n%\\n\\x0cservice.name\\x12\\x15\\n\\x13OtlpExporterExample\\n \\n\\x16telemetry.sdk.language\\x12\\x06\\n\\x04java\\n%\\n\\x12telemetry.sdk.name\\x12\\x0f\\n\\ropentelemetry\\n!\\n\\x15telemetry.sdk.version\\x12\\x08\\n\\x061.26.0\\x12\\x80\\x01\\n\\x1a\\n\\x18io.opentelemetry.example\\x12b\\n\\x10\\xb1\\xf2Y\\x94B\\xd1\\xce\\xd4\\x1ce\\x1a\\xeb49V9\\x12\\x08k\\t\\x17\\x92\\x08\\xbbZU\\"\\0*\\x0bexampleSpan0\\x019\\xc0#\\xa5\\xf5i\\x18e\\x17A)\\x13\\xcb\\xfbi\\x18e\\x17J\\n\\n\\x04good\\x12\\x02\\x10\\x01J\\x13\\n\\rexampleNumber\\x12\\x02\\x18bz\\0\\n\\x99\\x02\\n\\x93\\x01\\n%\\n\\x0cservice.name\\x12\\x15\\n\\x13OtlpExporterExample\\n \\n\\x16telemetry.sdk.language\\x12\\x06\\n\\x04java\\n%\\n\\x12telemetry.sdk.name\\x12\\x0f\\n\\ropentelemetry\\n!\\n\\x15telemetry.sdk.version\\x12\\x08\\n\\x061.26.0\\x12\\x80\\x01\\n\\x1a\\n\\x18io.opentelemetry.example\\x12b\\n\\x10\\xa4\\xddQ\\xae\\xea>\\xa2\\x18\\x0f\\x0f8\\xacm\\xa7/\\xf5\\x12\\x08EJ\\x1d\\x9a`\\x0e\\x0c~\\"\\0*\\x0bexampleSpan0\\x019(\\x1b\\xcc\\xfbi\\x18e\\x17A]]\\x1c\\x02j\\x18e\\x17J\\n\\n\\x04good\\x12\\x02\\x10\\x01J\\x13\\n\\rexampleNumber\\x12\\x02\\x18cz\\0
//            """;
//    @Test
//    public void givenJson_convertToProtobuf() throws IOException {
//        Message protobuf = ProtobufToJsonConversion.fromJson(jsonStr);
//        Assertions.assertTrue(protobuf.toString().contains("key: \"boolean\""));
//        Assertions.assertTrue(protobuf.toString().contains("string_value: \"Hello World\""));
//    }
//
//    @Test
//    public void givenProtobuf_convertToJson() throws IOException {
//        Message protobuf = ProtobufToJsonConversion.fromJson(jsonStr);
//        String json = ProtobufToJsonConversion.toJson(protobuf);
//        Assertions.assertTrue(json.contains("\"boolean\": true"));
//        Assertions.assertTrue(json.contains("\"string\": \"Hello World\""));
//        Assertions.assertTrue(json.contains("\"color\": \"gold\""));
//    }
//}
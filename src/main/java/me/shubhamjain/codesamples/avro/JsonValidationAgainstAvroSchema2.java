package me.shubhamjain.codesamples.avro;

import org.apache.avro.AvroTypeException;
import org.apache.avro.Schema;
import org.apache.avro.generic.GenericDatumReader;
import org.apache.avro.io.DatumReader;
import org.apache.avro.io.Decoder;
import org.apache.avro.io.DecoderFactory;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.InputStream;

public class JsonValidationAgainstAvroSchema2 {

    public static void main(String[] args) throws Exception {
        Schema schema = new Schema.Parser().parse("""
                {
                  "type": "record",
                  "name": "CollegeRequestMetric",
                  "namespace": "com.test-project.platform.point.model",
                  "fields": [
                    {
                      "name": "college_id",
                      "type": "string"
                    },
                    {
                      "name": "endpoint",
                      "type": "string"
                    },
                    {
                      "name": "status",
                      "type": "int"
                    },
                    {
                      "name": "success",
                      "type": "boolean"
                    },
                    {
                      "name": "version",
                      "type": [
                        "null",
                        "string"
                      ],
                      "default": null
                    },
                    {
                      "name": "response",
                      "type": [
                        "null",
                        "string"
                      ],
                      "default": null
                    },
                    {
                      "name": "error_message",
                      "type": [
                        "null",
                        "string"
                      ],
                      "default": null
                    },
                    {
                      "name": "tracing_id",
                      "type": [
                        "null",
                        "string"
                      ],
                      "default": null
                    },
                    {
                      "name": "application_id",
                      "type": [
                        "null",
                        "string"
                      ],
                      "default": null
                    },
                    {
                      "name": "created_at",
                      "type": [
                        "null",
                        "string"
                      ],
                      "default": null
                    }
                  ]
                }
                """);
        String invalidJson = """
                              {
                                  "college_id": "mock-sandbox",
                                  "endpoint": "/open-banking/v3.1/aisp/accounts/1bd762a1-5483-45d6-9781-e657380e11eb/balances",
                                  "status": 200,
                                  "success": true,
                                  "version": {"string": "V1.1"},
                                  "response": {"string": "null"},
                                  "error_message":{"string": "null"},
                                  "tracing_id": {"string": "8c86416b2da5481293fd4da5d3e9f0eb"},
                                  "application_id": {"string": "2698db90-6635-4f76-b673-5ce8e2aeda0e"},
                                  "created_at": { "string": "2024-01-22T18:46:00.094" }
                                }
                """;
        String json = """
                {"click_request_id":{"string":"68418bea-50d7-4813-9486-4be6f9de3131"},"click_id":{"string":"c7c36939-9f68-47e2-ac2c-06c5e6719173"},"application_id":{"string":"b445baad-dd5e-458c-8f2b-34421720c9b5"},"user_uuid":{"string":"2dba6e27-8ff4-4dcf-ad4d-441e0a80e852"},"click_idempotency_id":{"string":"9054df1b-939d-4597-86d3-ff4026a5f6bc"},"reference":{"string":""},"college_id":null,"college_country_code":null,"status":[{"status":{"string":"COMPLETED"},"iso_status":{"string":"{'code': 'ACSC', 'name': 'AcceptedSettlementCompleted'}"},"status_update_date":{"string":"2023-10-06T14:06:32.740841"}}],"phase":[{"phase_name":{"string":"INITIATED"},"created_at":{"string":"2023-10-06T14:06:32.740841"}}],"amount":{"amount":{"string":"70.26"},"currency":{"string":"EUR"}},"payee":{"name":{"string":"John Doe"}},"created_at":null}
                """;
        System.out.println(validateJson(json, schema));
        System.out.println(validateJson(invalidJson, schema));
    }

    public static boolean validateJson(String json, Schema schema) throws Exception {
        InputStream input = new ByteArrayInputStream(json.getBytes());
        DataInputStream din = new DataInputStream(input);

        try {
            DatumReader reader = new GenericDatumReader(schema);
            Decoder decoder = DecoderFactory.get().jsonDecoder(schema, din);
            reader.read(null, decoder);
            return true;
        } catch (AvroTypeException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }
}

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

public class JsonValidationAgainstAvroSchema {

    public static void main(String[] args) throws Exception {
        Schema schema = new Schema.Parser().parse("""
                {
                  "namespace": "model",
                  "name": "Point",
                  "type": "record",
                  "fields": [
                    {
                      "name": "click_request_id",
                      "type": [
                        "null",
                        "string"
                      ],
                      "default": null
                    },
                    {
                      "name": "click_id",
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
                      "name": "user_uuid",
                      "type": [
                        "null",
                        "string"
                      ],
                      "default": null
                    },
                    {
                      "name": "click_idempotency_id",
                      "type": [
                        "null",
                        "string"
                      ],
                      "default": null
                    },
                    {
                      "name": "reference",
                      "type": [
                        "null",
                        "string"
                      ],
                      "default": null
                    },
                    {
                      "name": "college_id",
                      "type": [
                        "null",
                        "string"
                      ],
                      "default": null
                    },
                    {
                      "name": "college_country_code",
                      "type": [
                        "null",
                        "string"
                      ],
                      "default": null
                    },
                    {
                      "name": "status",
                      "type": {
                        "type": "array",
                        "default": null,
                        "items": {
                          "name": "status",
                          "type": "record",
                          "default": null,
                          "fields": [
                            {
                              "name": "status",
                              "type": [
                                "null",
                                "string"
                              ],
                              "default": null
                            },
                            {
                              "name": "iso_status",
                              "type": [
                                "null",
                                "string"
                              ],
                              "default": null
                            },
                            {
                              "name": "status_update_date",
                              "type": [
                                "null",
                                "string"
                              ],
                              "default": null
                            }
                          ]
                        }
                      }
                    },
                    {
                      "name": "phase",
                      "type": {
                        "type": "array",
                        "default": null,
                        "items": {
                          "name": "phase",
                          "type": "record",
                          "default": null,
                          "fields": [
                            {
                              "name": "phase_name",
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
                      }
                    },
                    {
                      "name": "amount",
                      "type": {
                        "name" : "Amount",
                        "type" : "record",
                        "default": null,
                        "fields" : [
                          {
                            "name": "amount",
                            "type" : ["null", "string"],
                            "default": null
                          },
                          {
                            "name": "currency",
                            "type" : ["null", "string"],
                            "default": null
                          }
                        ]
                      }
                    },
                    {
                      "name": "payee",
                      "type": {
                        "name" : "Payee",
                        "type" : "record",
                        "default": null,
                        "fields" : [
                          {
                            "name": "name",
                            "type" : ["null", "string"],
                            "default": null
                          }
                        ]
                      }
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
                    "amount": {
                        "amount": "70.26",
                        "currency": "EUR"
                    },
                    "application_id": "2003b474-75f6-47bb-bb4b-9ac590f13c02",
                    "created_at": "2023-10-05T15:52:16.722996",
                    "college_country_code": null,
                    "college_id": null,
                    "payee": {
                        "name": "John Doe"
                    },
                    "click_id": "5b9c0e00-8258-40c8-84a5-3cbe81cafefe",
                    "click_idempotency_id": "bdaa6f00-d965-4bd8-8518-6af105cb3d1d",
                    "click_request_id": "26fcd2cf-5034-4b6a-a103-e4c1b5205c92",
                    "phase": [
                        {
                            "created_at": "2023-10-05T15:52:16.722996",
                            "phase_name": "INITIATED"
                        }
                    ],
                    "reference": "INITIATED",
                    "status": [
                        {
                            "iso_status": "{'code': 'ACSC', 'name': 'AcceptedSettlementCompleted'}",
                            "status": "COMPLETED",
                            "status_update_date": "2023-10-05T15:52:16.722996"
                        }
                    ],
                    "user_uuid": "bda26b6e-d950-4079-a718-2daa6a3ffb55"
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

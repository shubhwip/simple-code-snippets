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

public class JsonValidationAgainstAvroSchema3 {

    public static void main(String[] args) throws Exception {
        Schema schema = new Schema.Parser().parse("""
                {
                   "namespace": "com.test-project.platform.point.model",
                   "name": "TRIPaymentDataPoint",
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
                       "name": "college_id",
                       "type": [
                         "null",
                         "string"
                       ],
                       "default": null
                     },
                     {
                       "name": "merchant_id",
                       "type": [
                         "null",
                         "string"
                       ],
                       "default": null
                     },
                     {
                       "name": "merchant_code",
                       "type": [
                         "null",
                         "string"
                       ],
                       "default": null
                     },
                     {
                       "name": "payee_account_type",
                       "type": [
                         "null",
                         "string"
                       ],
                       "default": null
                     },
                     {
                       "name": "click_purpose_code",
                       "type": [
                         "null",
                         "string"
                       ],
                       "default": null
                     },
                     {
                       "name": "click_context_code",
                       "type": [
                         "null",
                         "string"
                       ],
                       "default": null
                     },
                     {
                       "name": "click_amount",
                       "type": [
                         "null",
                         "string"
                       ],
                       "default": null
                     },
                     {
                       "name": "click_currency",
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
  "click_request_id":{"string":"93544a99-3b0c-4bb2-ae08-044ae175ddf4"},
  "click_id":"{"string":"93544a99-3b0c-4bb2-ae08-044ae175ddf4"},
  "application_id":{"string":"93544a99-3b0c-4bb2-ae08-044ae175ddf4"},
  "college_id":{"string":"93544a99-3b0c-4bb2-ae08-044ae175ddf4"},
  "merchant_id":{"string":"93544a99-3b0c-4bb2-ae08-044ae175ddf4"},
  "merchant_code":{"string":"jsnv"},
  "payee_account_type":{"string":"vsdvsd"},
  "click_purpose_code":{"string":"snjdnv"},
  "click_context_code":{"string":"vksnv"},
  "click_amount":{"string":"9790809"},
  "click_currency":{"string":"EUR"}
}
                """;
        String json = """
                {"click_request_id":{"string":"68418bea-50d7-4813-9486-4be6f9de3131"},"click_id":{"string":"c7c36939-9f68-47e2-ac2c-06c5e6719173"},"application_id":{"string":"b445baad-dd5e-458c-8f2b-34421720c9b5"},"user_uuid":{"string":"2dba6e27-8ff4-4dcf-ad4d-441e0a80e852"},"click_idempotency_id":{"string":"9054df1b-939d-4597-86d3-ff4026a5f6bc"},"reference":{"string":""},"college_id":null,"college_country_code":null,"status":[{"status":{"string":"COMPLETED"},"iso_status":{"string":"{'code': 'ACSC', 'name': 'AcceptedSettlementCompleted'}"},"status_update_date":{"string":"2023-10-06T14:06:32.740841"}}],"phase":[{"phase_name":{"string":"INITIATED"},"created_at":{"string":"2023-10-06T14:06:32.740841"}}],"amount":{"amount":{"string":"70.26"},"currency":{"string":"EUR"}},"payee":{"name":{"string":"John Doe"}},"created_at":null}
                """;
        //System.out.println(validateJson(json, schema));
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

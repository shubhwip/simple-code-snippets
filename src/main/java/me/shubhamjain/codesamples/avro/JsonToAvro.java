package me.shubhamjain.codesamples.avro;

import org.apache.avro.Schema;
import org.apache.avro.generic.GenericDatumReader;
import org.apache.avro.generic.GenericDatumWriter;
import org.apache.avro.generic.GenericRecord;
import org.apache.avro.io.*;
import org.kitesdk.data.spi.JsonUtil;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

// https://stackoverflow.com/questions/21977704/how-to-avro-binary-encode-the-json-string-using-apache-avro
public class JsonToAvro {

    public static byte[] jsonToAvro(String json, String schemaStr) throws IOException {
        InputStream input = null;
        GenericDatumWriter<GenericRecord> writer = null;
        Encoder encoder = null;
        ByteArrayOutputStream output = null;
        try {
            Schema schema = new Schema.Parser().parse(schemaStr);
            DatumReader<GenericRecord> reader = new GenericDatumReader<GenericRecord>(schema);
            input = new ByteArrayInputStream(json.getBytes());
            output = new ByteArrayOutputStream();
            DataInputStream din = new DataInputStream(input);
            writer = new GenericDatumWriter<GenericRecord>(schema);
            Decoder decoder = DecoderFactory.get().jsonDecoder(schema, din);
            encoder = EncoderFactory.get().binaryEncoder(output, null);
            GenericRecord datum;
            while (true) {
                try {
                    datum = reader.read(null, decoder);
                } catch (EOFException eofe) {
                    break;
                }
                writer.write(datum, encoder);
            }
            encoder.flush();
            return output.toByteArray();
        } finally {
            try { input.close(); } catch (Exception e) { }
        }
    }
    public static String avroToJson(byte[] avro, String schemaStr) throws IOException {
        boolean pretty = false;
        GenericDatumReader<GenericRecord> reader = null;
        JsonEncoder encoder = null;
        ByteArrayOutputStream output = null;
        try {
            Schema schema = new Schema.Parser().parse(schemaStr);
            reader = new GenericDatumReader<GenericRecord>(schema);
            InputStream input = new ByteArrayInputStream(avro);
            output = new ByteArrayOutputStream();
            DatumWriter<GenericRecord> writer = new GenericDatumWriter<GenericRecord>(schema);
            encoder = EncoderFactory.get().jsonEncoder(schema, output);
            Decoder decoder = DecoderFactory.get().binaryDecoder(input, null);
            GenericRecord datum;
            while (true) {
                try {
                    datum = reader.read(null, decoder);
                } catch (EOFException eofe) {
                    break;
                }
                writer.write(datum, encoder);
            }
            encoder.flush();
            output.flush();
            return new String(output.toByteArray());
        } finally {
            try { if (output != null) output.close(); } catch (Exception e) { }
        }
    }

    public static void main(String[] args) throws IOException {
        String sampleJson = Files.readString(Path.of(InferAvroSchemaFromJson.class.getClassLoader().getResource("sample1.json").getPath()));
        System.out.println("Original Size: " + sampleJson.getBytes().length);
        String avroSchema = JsonUtil.inferSchema(JsonUtil.parse(sampleJson), "myschema").toString();
        System.out.println("Schema Size: " + avroSchema.getBytes().length);
        byte[] result = jsonToAvro(sampleJson, avroSchema);
        System.out.println("Avro Size: " + result.length);
    }
}

package me.shubhamjain.codesamples.gcp.bq;

import com.google.cloud.bigquery.*;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Slf4j
public class BigQueryExecuteQuery {
    private static final BigQuery bigQuery = BigQueryOptions.getDefaultInstance().getService();

    public static Set<String> getTracingIds(String query) {
        Set<String> matchedRows = new HashSet<>();
        try {
            QueryJobConfiguration queryConfig =
                    QueryJobConfiguration.newBuilder(query)
                            .setUseLegacySql(false)
                            .build();

            // Create a job ID so that we can safely retry.
            JobId jobId = JobId.of("test-project-production", UUID.randomUUID().toString());
            Job queryJob = bigQuery.create(JobInfo.newBuilder(queryConfig).setJobId(jobId).build());

            // Wait for the query to complete.
            queryJob = queryJob.waitFor();

            // Check for errors
            if (queryJob == null) {
                throw new RuntimeException("BigQueryExecutionException : Job no longer exists");
            } else if (queryJob.getStatus().getError() != null) {
                // You can also look at queryJob.getStatus().getExecutionErrors() for all
                // errors, not just the latest one.
                throw new RuntimeException("BigQueryExecutionException : " + queryJob.getStatus().getError().toString());
            }

            // Get the results.
            TableResult result = queryJob.getQueryResults();
            // Print all pages of the results.
            for (FieldValueList row : result.iterateAll()) {
                // String type
                matchedRows.add(row.get("ID").getStringValue());
            }
        } catch (InterruptedException e) {
            log.error("BigQueryExecutionException : Interrupted, Exception occurred in executing bigquery!", e);
            Thread.currentThread().interrupt();
        }
        return matchedRows;

    }

    public static void main(String[] args) throws IOException {
        String query = """
                SELECT * FROM test-project-production.ABC.ABC;
                """;
        Files.write(Path.of("/tmp/abc.csv"), getTracingIds(query));
    }
}

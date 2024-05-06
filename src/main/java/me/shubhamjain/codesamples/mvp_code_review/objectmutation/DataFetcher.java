package me.shubhamjain.codesamples.mvp_code_review.objectmutation;

public interface DataFetcher {
    ResultV1 fetch(RequestV1 requestV1, Metadata metadata);
}

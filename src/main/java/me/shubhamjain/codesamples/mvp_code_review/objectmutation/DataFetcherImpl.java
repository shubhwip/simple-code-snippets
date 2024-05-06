//package me.shubhamjain.codesamples.mvp_code_review.objectmutation;
//
//import com.github.benmanes.caffeine.cache.Cache;
//
//public class DataFetcherImpl implements DataFetcher {
//    RemoteDataFetcher remoteDataFetcher;
//    @Override
//    public ResultV1 fetch(RequestV1 requestV1, Metadata metadata) {
//        if(requestV1.page() == "1") {
//            metadata.setOptions(new String[]{"start_offset=2", "prefix=7"});
//            return remoteDataFetcher.find(requestV1.data(), requestV1.page(), metadata.options);
//        }
//        metadata.setOptions(new String[]{"prefix=7"});
//        return remoteDataFetcher.find(requestV1.data(), requestV1.page(), metadata.options);
//    }
//}

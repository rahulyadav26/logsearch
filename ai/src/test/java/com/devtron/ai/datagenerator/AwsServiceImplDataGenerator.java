package com.devtron.ai.datagenerator;

import com.amazonaws.services.s3.model.ListObjectsV2Result;
import com.amazonaws.services.s3.model.S3Object;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class AwsServiceImplDataGenerator {

    public static final String AWS_BUCKET_NAME = "aws-bucket";

    public static final String TEST_PREFIX_VALUE = "test-prefix";

    public static final String TEST_KEY_VALUE = "test-key";

    public static ListObjectsV2Result fetchListObjectsV2Result() {
        final ListObjectsV2Result result = new ListObjectsV2Result();
        result.setBucketName(AWS_BUCKET_NAME);
        result.setKeyCount(10);
        return result;
    }

    public static S3Object fetchS3Object(){
        final S3Object s3Object = new S3Object();
        s3Object.setBucketName(AWS_BUCKET_NAME);
        s3Object.setKey(TEST_KEY_VALUE);
        return s3Object;
    }

}

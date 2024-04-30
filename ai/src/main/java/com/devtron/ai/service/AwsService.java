package com.devtron.ai.service;

import com.amazonaws.services.s3.model.ListObjectsV2Request;
import com.amazonaws.services.s3.model.ListObjectsV2Result;
import com.amazonaws.services.s3.model.S3Object;

public interface AwsService {

    ListObjectsV2Result fetchObjectsForTheGivenPrefix(String bucketName, ListObjectsV2Request listObjectsV2Request);

    ListObjectsV2Request getListObjectsV2RequestForS3(String bucketName, String prefix);

    S3Object fetchObjectForTheGivenKey(String bucketName, String key);

}

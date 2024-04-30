package com.devtron.ai.service.impl;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ListObjectsV2Request;
import com.amazonaws.services.s3.model.ListObjectsV2Result;
import com.amazonaws.services.s3.model.S3Object;
import com.devtron.ai.exception.BadRequestException;
import com.devtron.ai.service.AwsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Objects;

@Slf4j
@Service
public class AwsServiceImpl implements AwsService {

    private final AmazonS3 s3Client;

    @Autowired
    public AwsServiceImpl(final AmazonS3 s3Client) {
        this.s3Client = s3Client;
    }

    @Override
    public ListObjectsV2Request getListObjectsV2RequestForS3(final String bucketName, final String prefix) {
        if (!checkBucketNameValue(bucketName) || StringUtils.isEmpty(prefix)) {
            log.error("Bucket name or prefix is empty");
            throw new BadRequestException("Invalid data to fetch data from AWS S3 bucket");
        }
        return new ListObjectsV2Request().withBucketName(bucketName).withPrefix(prefix);
    }

    @Override
    public ListObjectsV2Result fetchObjectsForTheGivenPrefix(final String bucketName,
                                                             final ListObjectsV2Request listObjectsV2Request) {
        if (!checkBucketNameValue(bucketName) || Objects.isNull(listObjectsV2Request)) {
            log.error("Bucket name or listObjectsV2Request is empty");
            throw new BadRequestException("Invalid data to fetch data from AWS S3 bucket");
        }
        return s3Client.listObjectsV2(listObjectsV2Request);
    }

    @Override
    public S3Object fetchObjectForTheGivenKey(final String bucketName, final String key) {
        if (!checkBucketNameValue(bucketName) || Objects.isNull(key)) {
            log.error("Bucket name or key is empty");
            throw new BadRequestException("Invalid data to fetch data from AWS S3 bucket");
        }
        return s3Client.getObject(bucketName, key);
    }

    private Boolean checkBucketNameValue(final String bucketName) {
        if (StringUtils.isEmpty(bucketName)) {
            log.error("Bucket name is empty");
            return false;
        }
        return true;
    }
}

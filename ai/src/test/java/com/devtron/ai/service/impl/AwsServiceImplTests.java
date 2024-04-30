package com.devtron.ai.service.impl;

import com.amazonaws.services.s3.model.ListObjectsV2Result;
import com.amazonaws.services.s3.model.S3Object;
import com.devtron.ai.exception.BadRequestException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ListObjectsV2Request;

import static com.devtron.ai.datagenerator.AwsServiceImplDataGenerator.AWS_BUCKET_NAME;
import static com.devtron.ai.datagenerator.AwsServiceImplDataGenerator.TEST_KEY_VALUE;
import static com.devtron.ai.datagenerator.AwsServiceImplDataGenerator.TEST_PREFIX_VALUE;
import static com.devtron.ai.datagenerator.AwsServiceImplDataGenerator.fetchListObjectsV2Result;
import static com.devtron.ai.datagenerator.AwsServiceImplDataGenerator.fetchS3Object;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class AwsServiceImplTests {

    @InjectMocks
    private AwsServiceImpl awsServiceImpl;

    @Mock
    private AmazonS3 s3Client;

    @Test(expected = BadRequestException.class)
    public void test_getListObjectsV2RequestForS3_when_bucketNameIsNull_then_throwBadRequestException() {
        awsServiceImpl.getListObjectsV2RequestForS3(null, TEST_PREFIX_VALUE);
    }

    @Test(expected = BadRequestException.class)
    public void test_getListObjectsV2RequestForS3_when_bucketNameIsEmpty_then_throwBadRequestException() {
        awsServiceImpl.getListObjectsV2RequestForS3("", TEST_PREFIX_VALUE);
    }

    @Test(expected = BadRequestException.class)
    public void test_getListObjectsV2RequestForS3_when_prefixIsEmpty_then_throwBadRequestException() {
        awsServiceImpl.getListObjectsV2RequestForS3(AWS_BUCKET_NAME, "");
    }

    @Test(expected = BadRequestException.class)
    public void test_getListObjectsV2RequestForS3_when_prefixIsNull_then_throwBadRequestException() {
        awsServiceImpl.getListObjectsV2RequestForS3(AWS_BUCKET_NAME, null);
    }

    @Test
    public void test_getListObjectsV2RequestForS3_when_bucketNameAndPrefixIsValid_then_returnListObjectsV2Request() {
        final ListObjectsV2Request request =
                awsServiceImpl.getListObjectsV2RequestForS3(AWS_BUCKET_NAME, TEST_PREFIX_VALUE);
        assertNotNull(request);
        assertEquals(AWS_BUCKET_NAME, request.getBucketName());
        assertEquals(TEST_PREFIX_VALUE, request.getPrefix());
    }

    @Test(expected = BadRequestException.class)
    public void test_fetchObjectsForTheGivenPrefix_when_bucketNameIsNull_then_throwBadRequestException() {
        awsServiceImpl.fetchObjectsForTheGivenPrefix(null, new ListObjectsV2Request());
    }

    @Test(expected = BadRequestException.class)
    public void test_fetchObjectsForTheGivenPrefix_when_bucketNameIsEmpty_then_throwBadRequestException() {
        awsServiceImpl.fetchObjectsForTheGivenPrefix("", new ListObjectsV2Request());
    }

    @Test(expected = BadRequestException.class)
    public void test_fetchObjectsForTheGivenPrefix_when_listObjectsV2RequestIsNull_then_throwBadRequestException() {
        awsServiceImpl.fetchObjectsForTheGivenPrefix(AWS_BUCKET_NAME, null);
    }

    @Test
    public void test_fetchObjectsForTheGivenPrefix_when_bucketNameAndListObjectIsValid_then_returnFetchObjectsFromS3AndReturn() {
        when(s3Client.listObjectsV2(any(ListObjectsV2Request.class))).thenReturn(fetchListObjectsV2Result());
        final ListObjectsV2Result result =
                awsServiceImpl.fetchObjectsForTheGivenPrefix(AWS_BUCKET_NAME, new ListObjectsV2Request());
        assertNotNull(result);
        assertEquals(AWS_BUCKET_NAME, result.getBucketName());
        assertEquals(10, result.getKeyCount());
    }

    @Test(expected = BadRequestException.class)
    public void test_fetchObjectForTheGivenKey_when_bucketNameIsNull_then_throwBadRequestException() {
        awsServiceImpl.fetchObjectForTheGivenKey(null, TEST_KEY_VALUE);
    }

    @Test(expected = BadRequestException.class)
    public void test_fetchObjectForTheGivenKey_when_bucketNameIsEmpty_then_throwBadRequestException() {
        awsServiceImpl.fetchObjectForTheGivenKey("", TEST_KEY_VALUE);
    }

    @Test(expected = BadRequestException.class)
    public void test_fetchObjectForTheGivenKey_when_keyIsNull_then_throwBadRequestException() {
        awsServiceImpl.fetchObjectForTheGivenKey(AWS_BUCKET_NAME, null);
    }

    @Test
    public void test_fetchObjectForTheGivenKey_when_bucketNameAndKeyNameIsValid_then_fetchAndReturnS3Object() {
        when(s3Client.getObject(AWS_BUCKET_NAME, TEST_KEY_VALUE)).thenReturn(fetchS3Object());
        final S3Object s3Object = awsServiceImpl.fetchObjectForTheGivenKey(AWS_BUCKET_NAME, TEST_KEY_VALUE);
        assertNotNull(s3Object);
        assertEquals(TEST_KEY_VALUE, s3Object.getKey());
        assertEquals(AWS_BUCKET_NAME, s3Object.getBucketName());
    }

}

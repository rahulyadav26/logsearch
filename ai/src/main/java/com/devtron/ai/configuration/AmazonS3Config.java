package com.devtron.ai.configuration;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AmazonS3Config {

    @Value("${amazonproperties.s3.access.key.id}")
    private String awsAccessKeyId;

    @Value("${amazonproperties.s3.secret.key}")
    private String awsSecretKey;

    @Value("${amazonproperties.region}")
    private String region;

    @Bean
    public AmazonS3 s3client() {
        return AmazonS3ClientBuilder.standard().withRegion(Regions.fromName(region))
                                    .withCredentials(new AWSCredentialsProvider() {
                                        @Override
                                        public AWSCredentials getCredentials() {
                                            return new AWSCredentials() {
                                                @Override
                                                public String getAWSAccessKeyId() {
                                                    return awsAccessKeyId;
                                                }

                                                @Override
                                                public String getAWSSecretKey() {
                                                    return awsSecretKey;
                                                }
                                            };
                                        }

                                        @Override
                                        public void refresh() {

                                        }
                                    }).build();
    }

}

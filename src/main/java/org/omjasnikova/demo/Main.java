package org.omjasnikova.demo;

import io.minio.*;
import io.minio.errors.MinioException;
import io.minio.messages.Bucket;

import java.io.IOException;
import java.util.List;

public class Main {
    public static void main(String[] args) throws Exception {
        MinioClient minioClient = connect();
        try {
            List<Bucket> blist = minioClient.listBuckets();
            System.out.println("Connection success, total buckets: " + blist.size());
        } catch (MinioException e) {
            System.out.println("Connection failed: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
        String bucketName = "test-bucket";
        createBucketIfNecessary(minioClient, bucketName);
        ObjectWriteResponse response = minioClient.uploadObject(getUploadArgs(bucketName));
        System.out.println(response.object() + "," + response.etag() + "," + response.bucket());
    }

    private static MinioClient connect() {
        MinioClient minioClient = MinioClient.builder()
                .endpoint("http://localhost:9000")
                .credentials("hhtech", "minio123")
                .build();

        return minioClient;
    }

    private static UploadObjectArgs getUploadArgs(String bucketName) throws IOException {
        String objectName = "test.json";
        String contentType = "application/json";
        String fileName ="test.json";
        UploadObjectArgs uArgs = UploadObjectArgs.builder()
                .bucket(bucketName)
                .object(objectName)
                .filename(fileName)
                .contentType(contentType)
                .build();
        return uArgs;
    }

    private static void createBucketIfNecessary(MinioClient minioClient, String bucketName) throws Exception {
         BucketExistsArgs bucketExistsArgs = BucketExistsArgs.builder()
                .bucket(bucketName)
                .build();

        if (minioClient.bucketExists(bucketExistsArgs)) {
            System.out.println(bucketName + " exists");
        } else {
            System.out.println(bucketName + " does not exists. Creating the bucket");
            MakeBucketArgs makeBucketArgs = MakeBucketArgs.builder()
                    .bucket(bucketName)
                    .build();
            minioClient.makeBucket(makeBucketArgs);
            if (minioClient.bucketExists(bucketExistsArgs)) {
                System.out.println(bucketName + " exists");
            } else {
                System.out.println(bucketName + " does not exists]");
            }
        }

    }
}
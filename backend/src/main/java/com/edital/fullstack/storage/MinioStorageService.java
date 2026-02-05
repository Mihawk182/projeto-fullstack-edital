package com.edital.fullstack.storage;

import io.minio.BucketExistsArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.GetPresignedObjectUrlArgs;
import io.minio.http.Method;
import java.io.InputStream;
import java.time.Duration;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class MinioStorageService {
  private final MinioClient client;
  private final String bucket;

  public MinioStorageService(MinioClient client, @Value("${minio.bucket}") String bucket) {
    this.client = client;
    this.bucket = bucket;
  }

  public String upload(String filename, InputStream inputStream, long size, String contentType) throws Exception {
    ensureBucket();
    String objectKey = "albums/" + UUID.randomUUID() + "-" + filename;
    client.putObject(
        PutObjectArgs.builder()
            .bucket(bucket)
            .object(objectKey)
            .stream(inputStream, size, -1)
            .contentType(contentType)
            .build()
    );
    return objectKey;
  }

  public String presignedUrl(String objectKey, Duration expiry) throws Exception {
    ensureBucket();
    return client.getPresignedObjectUrl(
        GetPresignedObjectUrlArgs.builder()
            .bucket(bucket)
            .object(objectKey)
            .method(Method.GET)
            .expiry((int) expiry.toSeconds())
            .build()
    );
  }

  private void ensureBucket() throws Exception {
    boolean exists = client.bucketExists(BucketExistsArgs.builder().bucket(bucket).build());
    if (!exists) {
      client.makeBucket(MakeBucketArgs.builder().bucket(bucket).build());
    }
  }
}

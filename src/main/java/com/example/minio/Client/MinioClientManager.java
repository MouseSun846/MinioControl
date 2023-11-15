package com.example.minio.Client;

import io.minio.*;
import io.minio.errors.*;
import io.minio.messages.Bucket;
import io.minio.messages.Item;
import org.springframework.stereotype.Component;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
public class MinioClientManager {
    public MinioClient getMinioClient() {
//        try {
            // Create a minioClient with the MinIO server playground, its access key and secret key.
        MinioClient minioClient = MinioClient.builder()
                .endpoint("192.168.6.254", 9000, false)
                .credentials("admin", "12345678")
                .build();
        return minioClient;
//
//            // Make 'asiatrip' bucket if not exist.
//            boolean found =
//                    minioClient.bucketExists(BucketExistsArgs.builder().bucket("asiatrip").build());
//            if (!found) {
//                // Make a new bucket called 'asiatrip'.
//                minioClient.makeBucket(MakeBucketArgs.builder().bucket("asiatrip").build());
//            } else {
//                System.out.println("Bucket 'asiatrip' already exists.");
//            }
//
//            // Upload '/home/user/Photos/asiaphotos.zip' as object name 'asiaphotos-2015.zip' to bucket
//            // 'asiatrip'.
//            minioClient.uploadObject(
//                    UploadObjectArgs.builder()
//                            .bucket("asiatrip")
//                            .object("asiaphotos-2015.zip")
//                            .filename("/home/user/Photos/asiaphotos.zip")
//                            .build());
//            System.out.println(
//                    "'/home/user/Photos/asiaphotos.zip' is successfully uploaded as "
//                            + "object 'asiaphotos-2015.zip' to bucket 'asiatrip'.");
//        } catch (MinioException e) {
//            System.out.println("Error occurred: " + e);
//            System.out.println("HTTP trace: " + e.httpTrace());
//        }
    }

    public List<String> getDirectoryFiles(String bucketName, String path) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        MinioClient minioClient = getMinioClient();
        Iterable<Result<Item>> results = minioClient.listObjects(ListObjectsArgs.builder()
                        .bucket(bucketName)
                        .prefix(path)
                .build());
        List<String> fileList = new ArrayList<>();
        for (Result<Item> result : results) {
            Item item = result.get();
            if (item != null && !item.isDir()) {
                fileList.add(item.objectName().replace(path,"").replace("/",""));
            }
        }
        return fileList;
    }

    public boolean fileExists(String bucketName, String path) {
        MinioClient minioClient = getMinioClient();
        Iterable<Result<Item>> results = minioClient.listObjects(ListObjectsArgs.builder()
                .bucket(bucketName)
                .prefix(path)
                .build());
        for (Result<Item> result : results) {
            return true;
        }
        return false;
    }

    public boolean isDirectory(String bucketName, String path) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        MinioClient minioClient = getMinioClient();
        Iterable<Result<Item>> results = minioClient.listObjects(ListObjectsArgs.builder()
                .bucket(bucketName)
                .prefix(path)
                .build());
        for (Result<Item> result : results) {
            Item item = result.get();
            if(item != null) {
                return true;
            }
        }
        return false;
    }

    public long getFileModificationTime(String bucketName, String path) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        MinioClient minioClient = getMinioClient();
        Iterable<Result<Item>> results = minioClient.listObjects(ListObjectsArgs.builder()
                .bucket(bucketName)
                .prefix(path)
                .build());
        for (Result<Item> result : results) {
            Item item = result.get();
            if(item != null) {
                return item.lastModified().toEpochSecond();
            }
        }
        return -1;
    }
    public List<String> getDirectoryContents(String bucketName, String path) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        MinioClient minioClient = getMinioClient();
        Iterable<Result<Item>> results = minioClient.listObjects(ListObjectsArgs.builder()
                .bucket(bucketName)
                .prefix(path)
                .build());
        List<String> nameList = new ArrayList<>();
        for (Result<Item> result : results) {
            Item item = result.get();
            if(item != null) {
                nameList.add(item.objectName().replace(path,"").replace("/",""));
            }
        }
        return nameList;
    }

    public List<String> getDirectorySubdirs(String bucketName, String path) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        MinioClient minioClient = getMinioClient();
        Iterable<Result<Item>> results = minioClient.listObjects(ListObjectsArgs.builder()
                .bucket(bucketName)
                .prefix(path)
                .build());
        List<String> nameList = new ArrayList<>();
        for (Result<Item> result : results) {
            Item item = result.get();
            if(item != null && item.isDir()) {
                nameList.add(item.objectName().replace(path,"").replace("/",""));
            }
        }
        return nameList;
    }

    public void readTextFile(HttpServletResponse response, String bucketName, String path) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        MinioClient minioClient = getMinioClient();
        ServletOutputStream outputStream = response.getOutputStream();
        response.setContentType("application/octet-stream");
        InputStream stream = minioClient.getObject(GetObjectArgs.builder()
                .bucket(bucketName)
                .object(path)
                .build());
        response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(path.substring(path.lastIndexOf('/')+1), "UTF-8"));
        byte[] buf = new byte[16384];
        int bytesRead;
        while ((bytesRead = stream.read(buf, 0, buf.length)) >= 0) {
            outputStream.write(buf, 0, bytesRead);
        }
        stream.close();
        outputStream.close();
    }
}

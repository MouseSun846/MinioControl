package com.example.minio.Controller;

import com.example.minio.Client.MinioClientManager;
import com.example.minio.Util.ResultVO;
import com.example.minio.Util.ResultVOUtil;
import io.minio.errors.*;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.websocket.server.PathParam;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

@RestController
@RequestMapping("/storage")
public class StorageController {

    @Resource
    private MinioClientManager minioClientManager;

    @PostMapping("/getDirectoryFiles")
    public ResultVO getDirectoryFiles(@RequestParam("bucketName") String bucketName, @RequestParam("path") String path) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        List<String> directoryFiles = minioClientManager.getDirectoryFiles(bucketName, path);
        return ResultVOUtil.success(directoryFiles);
    }

    @PostMapping("/fileExists")
    public ResultVO fileExists(@RequestParam("bucketName") String bucketName, @RequestParam("path") String path) {
        return ResultVOUtil.success(minioClientManager.fileExists(bucketName, path));
    }

    @PostMapping("/isDirectory")
    public ResultVO isDirectory(@RequestParam("bucketName") String bucketName, @RequestParam("path") String path) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        return ResultVOUtil.success(minioClientManager.isDirectory(bucketName, path));
    }

    @PostMapping("/getFileModificationTime")
    public ResultVO getFileModificationTime(@RequestParam("bucketName") String bucketName, @RequestParam("path") String path) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        return ResultVOUtil.success(minioClientManager.getFileModificationTime(bucketName, path));
    }

    @PostMapping("/getDirectoryContents")
    public ResultVO getDirectoryContents(@RequestParam("bucketName") String bucketName, @RequestParam("path") String path) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        return ResultVOUtil.success(minioClientManager.getDirectoryContents(bucketName, path));
    }

    @PostMapping("/getDirectorySubdirs")
    public ResultVO getDirectorySubdirs(@RequestParam("bucketName") String bucketName, @RequestParam("path") String path) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        return ResultVOUtil.success(minioClientManager.getDirectorySubdirs(bucketName, path));
    }

    @GetMapping("/readTextFile")
    public void readTextFile(HttpServletResponse response, @PathParam("bucketName") String bucketName, @PathParam("path") String path) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        minioClientManager.readTextFile(response, bucketName, path);
    }
}

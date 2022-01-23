package com.numble.karrot.aws.service;


import com.amazonaws.AmazonServiceException;
import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.numble.karrot.product.domain.Product;
import com.numble.karrot.product_image.domain.ProductImage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * S3 버킷에 이미지 등록 기능을 제공하는 클래스 입니다.
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class S3Uploader {

    private final AmazonS3Client amazonS3Client;

    @Value("${cloud.aws.s3.bucket}")
    public String bucket;

    private String originalFileName = "";

    /**
     * 상품이미지를 버킷에 올리고 데이터베이스에 저장하기위해 엔티티 리스트를 반환합니다.
     * @param multipartFiles 이미지 파일리스트
     * @param dirName 등록할 이미지의 버킷 폴더경로
     * @param product 이미지와 대응되는 상품 엔티티
     * @return 상품이미지 엔티티 리스트
     * @throws IOException
     */
    public ArrayList<ProductImage> uploadList(List<MultipartFile> multipartFiles, String dirName, Product product) throws IOException {
        ArrayList<ProductImage> productImages = new ArrayList<>();
        for (MultipartFile multipartFile : multipartFiles) {
            String uploadImageUrl = upload(multipartFile, dirName);
            productImages.add(
                    ProductImage.builder()
                            .filePath(dirName + "/")
                            .originalFileName(originalFileName)
                            .serverFileName(uploadImageUrl)
                            .product(product)
                            .build());
        }
        return productImages;
    }

    /**
     * 버킷에 이미지를 File 클래스로로 변환합니다.
     * @param multipartFile 이미지 파일
     * @param dirName 저장할 버킷 폴더 경로
     * @return 업로드 된 이미지 서버 url
     * @throws IOException
     */
    public String upload(MultipartFile multipartFile, String dirName) throws IOException {
        File uploadFile = convert(multipartFile)
                .orElseThrow(() -> new IllegalArgumentException(
                        "error: MultipartFile -> File convert fail"
                ));

        return upload(uploadFile, dirName);
    }

    /**
     * 고유한 파일 이름을 부여하여 S3 버킷에 업로드합니다.
     * @param uploadFile 업로드 할 이미지 파일
     * @param dirName 저장할 버킷 폴더 경로
     * @return 저장된 버킷 이미지 url
     */
    public String upload(File uploadFile, String dirName) {
        originalFileName = UUID.randomUUID() + uploadFile.getName();
        String fileName = dirName + "/" + originalFileName;
        String uploadImageUrl = putS3(uploadFile, fileName);
        removeNewFile(uploadFile);
        return uploadImageUrl;
    }

    /**
     * 버킷에 이미지 파일을 업로드합니다.
     * @param uploadFile 이미지 파일
     * @param fileName 고유한 파일 이름
     * @return 저장된 버킷 이미지 url
     */
    private String putS3(File uploadFile, String fileName) {
        amazonS3Client.putObject(new PutObjectRequest(bucket, fileName,
                uploadFile).withCannedAcl(CannedAccessControlList.PublicRead));
        return amazonS3Client.getUrl(bucket, fileName).toString();
    }

    /**
     * 로컬에 파일을 지웁니다.
     * @param targetFile 변환된 파일
     */
    private void removeNewFile(File targetFile) {
        if (targetFile.delete()) {
            log.info("File delete success");
            return;
        }
        log.info("File delete fail");
    }

    /**
     * 이미지 파일을 파일로 변환합니다.
     * @param file 이미지 파일
     * @return 변환된 파일 클래스
     * @throws IOException
     */
    private Optional<File> convert(MultipartFile file) throws IOException {

        File convertFile = new File("/home/ec2-user/files/"+ file.getOriginalFilename());
//       File convertFile = new File("/Users/uh/Desktop/files"+ file.getOriginalFilename());
        if (convertFile.createNewFile()) {
            try (FileOutputStream fos = new FileOutputStream(convertFile)) {
                fos.write(file.getBytes());
            }
            return Optional.of(convertFile);
        }
        return Optional.empty();

    }

    /**
     * 상품을 삭제합니다.
     * @param serverFileName
     */
    public void delete(String serverFileName) {
        try {
            DeleteObjectRequest deleteObjectRequest = new DeleteObjectRequest(this.bucket, serverFileName);
            //Delete
            this.amazonS3Client.deleteObject(deleteObjectRequest);
            System.out.println(String.format("[%s] deletion complete", serverFileName));

        } catch (AmazonServiceException e) {
            e.printStackTrace();
        } catch (SdkClientException e) {
            e.printStackTrace();
        }
    }

}

package com.lckback.lckforall.s3.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.util.IOUtils;
import com.lckback.lckforall.base.api.error.CommonErrorCode;
import com.lckback.lckforall.base.api.error.S3ErrorCode;
import com.lckback.lckforall.base.api.exception.RestApiException;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@Slf4j
public class S3Service {

	private final AmazonS3 amazonS3;

	@Value("${s3.bucketName}")
	private String bucketName;

	@Value("${s3.region}")
	private String region;

	@Value("${s3.accessKey}")
	private String accessKey;

	@Value("${s3.secretKey}")
	private String secretKey;


	public String upload(MultipartFile file) throws RestApiException {

		return this.uploadFile(file);
	}

	private String uploadFile(MultipartFile image) {
		//this.validateImageFileExtension(image.getOriginalFilename());
		try {
			return this.uploadImageToS3(image);
		} catch (IOException e) {
			throw new RestApiException(S3ErrorCode.IO_EXCEPTION_ON_IMAGE_UPLOAD);
		}
	}

	private void validateImageFileExtension(String filename) {
		int lastDotIndex = filename.lastIndexOf(".");
		if (lastDotIndex == -1) {
			throw new RestApiException(CommonErrorCode.RESOURCE_NOT_FOUND);
		}

		String extension = filename.substring(lastDotIndex + 1).toLowerCase();
		List<String> allowedExtensions = Arrays.asList("jpg", "jpeg", "png", "gif");

		if (!allowedExtensions.contains(extension)) {
			throw new RestApiException(CommonErrorCode.RESOURCE_NOT_FOUND);
		}
	}

	public boolean isImage(MultipartFile file) throws RestApiException {
		try {
			String originalFilename = file.getOriginalFilename();
			File checkFile = new File(originalFilename);
			String type = Files.probeContentType(checkFile.toPath());

			if (type.startsWith("image")) {
				return true;
			}
			return false;
		}catch (IOException e ){
			throw new RestApiException(CommonErrorCode.INTERNAL_SERVER_ERROR);
		}
	}

	private String uploadImageToS3(MultipartFile file) throws IOException {
		String originalFilename = file.getOriginalFilename();
		String extension = originalFilename.substring(originalFilename.lastIndexOf("."));

		File checkFile = new File(originalFilename);
		String type = Files.probeContentType(checkFile.toPath());

		log.info(type);

		String s3FileName =
			UUID.randomUUID().toString().substring(0, 10) + originalFilename;

		InputStream is = file.getInputStream();
		byte[] bytes = IOUtils.toByteArray(is);

		ObjectMetadata metadata = new ObjectMetadata();
		metadata.setContentType(file.getContentType());
		metadata.setContentLength(bytes.length);
		ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);

		try {
			PutObjectRequest putObjectRequest =
				new PutObjectRequest(bucketName, s3FileName, byteArrayInputStream, metadata)
					.withCannedAcl(CannedAccessControlList.PublicRead);

			amazonS3.putObject(putObjectRequest);

		} catch (Exception e) {

			e.printStackTrace();
			throw new RestApiException(CommonErrorCode.INTERNAL_SERVER_ERROR);
		} finally {
			byteArrayInputStream.close();
			is.close();
		}

		return "https://" + bucketName + ".s3." + region + ".amazonaws.com/" + s3FileName;
	}

	public void deleteImageFromS3(String imageAddress) {
		String key = getKeyFromImageAddress(imageAddress);
		try {
			amazonS3.deleteObject(new DeleteObjectRequest(bucketName, key));
		} catch (Exception e) {
			throw new RestApiException(CommonErrorCode.INTERNAL_SERVER_ERROR);
		}
	}

	private String getKeyFromImageAddress(String imageAddress) {
		try {
			URL url = new URL(imageAddress);
			String decodingKey = URLDecoder.decode(url.getPath(), "UTF-8");
			return decodingKey.substring(1);
		} catch (MalformedURLException | UnsupportedEncodingException e) {
			throw new RestApiException(CommonErrorCode.INTERNAL_SERVER_ERROR);
		}
	}
}

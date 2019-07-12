package com.bridgelabz.fundoo.user.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.Optional;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.bridgelabz.fundoo.user.model.Response;
import com.bridgelabz.fundoo.user.model.User;
import com.bridgelabz.fundoo.user.repository.IUserRepository;
import com.bridgelabz.fundoo.utility.ITokenGenerator;
import com.bridgelabz.fundoo.utility.ResponseUtility;



@Service
public class AmazonClient {

	

	private AmazonS3 s3Client;

	@Value("${amazonProperties.endpointUrl}")
	private String endPointUrl;

	@Value("${amazonProperties.accessKey}")
	private String accessKey;

	@Value("${amazonProperties.secretKey}")
	private String secretKey;

	@Value("${amazonProperties.bucketName}")
	private String bucketName;
	
	@Autowired
	private IUserRepository iUserRepository;
	
	@Autowired
	private Environment environment;
	
	@Autowired
	private ITokenGenerator tokenGenerator;

	@SuppressWarnings("deprecation")
	@PostConstruct
	private void initializeAmazon() {
		AWSCredentials credentials = new BasicAWSCredentials(this.accessKey, this.secretKey);
		this.s3Client = new AmazonS3Client(credentials);
	}

	/*
	 * S3 bucket uploading method requires a File as a parameter,but we have
	 * MultiPartFile, so we need to add method to convert it
	 */
	private File convertMultiPartToFile(MultipartFile file) throws IOException {
		File convertFile = new File(file.getOriginalFilename());
		FileOutputStream fileOutputStream = new FileOutputStream(convertFile);
		fileOutputStream.write(file.getBytes());
		fileOutputStream.close();
		return convertFile;
	}

	/*
	 * Also user can upload same file multiple times, we generate unique name for
	 * each one of them.
	 */
	private String generateFileName(MultipartFile multipartFile) {
		return new Date().getTime() + "-" + multipartFile.getOriginalFilename().replace(" ", "_");
	}

	/*
	 * In this method we are adding PublicRead permissions to file to make sure that
	 * each user can see the image.
	 */
	private void uploadFileToS3Bucket(String filename, File file) {
		s3Client.putObject(
				new PutObjectRequest(bucketName, filename, file).withCannedAcl(CannedAccessControlList.PublicRead));
	}

	/*
	 * method:UploadFile. purpose: This method is called in the controller This
	 * method will save a file in S3 Bucket and return a fileUrl which we can store
	 * in the database. Ex.We can attach this fileUrl to user's model if it is a
	 * profile image.
	 */
	public Response uploadFile(MultipartFile multiPartFile, String token) throws IOException {
		String id = tokenGenerator.verifyToken(token);
		Optional<User> optionalUser = iUserRepository.findByUserId(id);
		if (optionalUser.isPresent()) {
			User user = optionalUser.get();
			String fileUrl = "";

			File file = convertMultiPartToFile(multiPartFile);
			String fileName = generateFileName(multiPartFile);
			fileUrl = endPointUrl + "/" + bucketName + "/" + fileName;
			uploadFileToS3Bucket(fileName, file);
			file.delete();
			user.setImage(fileUrl);
			iUserRepository.save(user);
			Response response = ResponseUtility.getResponse(200, "", environment.getProperty("file.uploadtoS3.success"));
			return response;
		}
		Response response=ResponseUtility.getResponse(500, "", environment.getProperty("file.uploadtoS3.failed"));
		return response;
	}

	/*
	 * method: Delete File(image) S3 Bucket Cannot delete file by Url. It requires a
	 * bucket name and a filename. Thats why we retrieve filename from the Url.
	 */
	public Response deleteFileFromS3Bucket(String fileUrl, String token) {
		String id = tokenGenerator.verifyToken(token);
		Optional<User> optionalUser = iUserRepository.findByUserId(id);
		if (optionalUser.isPresent()) {
			User user=optionalUser.get();
			String fileName = fileUrl.substring(fileUrl.lastIndexOf("/") + 1);
			s3Client.deleteObject(new DeleteObjectRequest(bucketName , fileName));
			user.setImage(null);
			iUserRepository.save(user);
			Response response=ResponseUtility.getResponse(200, "", environment.getProperty("file.deleteFromS3.success"));
			return response;
		}
		Response response=ResponseUtility.getResponse(500, "", environment.getProperty("file.deleteFromS3.Failed"));
		return response;
	}
	
	public URL getImageUrl(String token) {
		String userID=tokenGenerator.verifyToken(token);
		boolean isUser=iUserRepository.findById(userID).isPresent();
		if(!isUser) {
			res
		}
}

}

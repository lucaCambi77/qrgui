package it.cambi.qrgui.config;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import it.cambi.qrgui.services.WorkBookService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:multitenant.properties")
public class WorkBookServiceConf {

  @Value("${amazon.aws.s3.host:http://127.0.0.1:4566}")
  public String host;

  @Value("${amazon.aws.s3.region:us-east-1}")
  public String defaultRegion;

  @Value("${amazon.aws.s3.accessKey:accesskey}")
  public String accessKey;

  @Value("${amazon.aws.s3.secretKey:secretkey}")
  public String secretKey;

  @Value("${amazon.aws.s3.bucket.name:bucket}")
  public String bucketName;

  @Bean
  public WorkBookService workBookService(AmazonS3 amazonS3) {
    return new WorkBookService(amazonS3);
  }

  @Bean
  public AmazonS3 amazonS3() {
    AmazonS3 amazonS3 =
        AmazonS3ClientBuilder.standard()
            .withEndpointConfiguration(
                new AwsClientBuilder.EndpointConfiguration(host, defaultRegion))
            .withCredentials(
                new AWSStaticCredentialsProvider(new BasicAWSCredentials(accessKey, secretKey)))
            .build();

    amazonS3.createBucket(bucketName);
    return amazonS3;
  }
}

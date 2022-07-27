package it.cambi.qrgui.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.fasterxml.jackson.databind.ObjectMapper;

import it.cambi.qrgui.model.Temi13DtbInf;
import it.cambi.qrgui.model.Temi14UteCat;
import it.cambi.qrgui.model.Temi15UteQue;
import it.cambi.qrgui.model.Temi16QueCatAss;
import it.cambi.qrgui.model.Temi17UteRou;
import it.cambi.qrgui.model.Temi18RouQue;
import it.cambi.qrgui.model.Temi20AnaTipCat;
import it.cambi.qrgui.query.model.QueryToJson;
import it.cambi.qrgui.response.model.ErtaGuiUser;
import it.cambi.qrgui.services.WorkBookService;
import it.cambi.qrgui.util.TreeNode;
import it.cambi.qrgui.util.objectMapper.ObjectMapperFactory;
import it.cambi.qrgui.util.wrappedResponse.WrappedResponse;
import it.cambi.qrgui.util.wrappedResponse.XWrappedResponse;

@Configuration
public class ServiceConfig {

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
  public WorkBookService workBookService() {
    return new WorkBookService(amazonS3());
  }

  private AmazonS3 amazonS3() {
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

  @Bean
  public ObjectMapperFactory objectMapperFactory() {
    return new ObjectMapperFactory();
  }

  @Bean
  public ObjectMapper objectMapper(ObjectMapperFactory objectMapperFactory) {
    return objectMapperFactory.getObjectMapper();
  }

  /** Wrapped Response */
  @Bean
  public WrappedResponse<List<Temi13DtbInf>> responseListDbInfo(
      ObjectMapperFactory objectMapperFactory) {
    return new WrappedResponse<>(objectMapperFactory);
  }

  @Bean
  public WrappedResponse<Temi15UteQue> responseQuery(ObjectMapperFactory objectMapperFactory) {
    return new WrappedResponse<>(objectMapperFactory);
  }

  @Bean
  public WrappedResponse<List<Temi15UteQue>> responseListQuery(
      ObjectMapperFactory objectMapperFactory) {
    return new WrappedResponse<>(objectMapperFactory);
  }

  @Bean
  public WrappedResponse<List<Temi16QueCatAss>> responseQueryCateAssoc(
      ObjectMapperFactory objectMapperFactory) {
    return new WrappedResponse<>(objectMapperFactory);
  }

  @Bean
  public WrappedResponse<List<Temi20AnaTipCat>> responseListTipCat(
      ObjectMapperFactory objectMapperFactory) {
    return new WrappedResponse<>(objectMapperFactory);
  }

  @Bean
  public WrappedResponse<Temi18RouQue> responseRoutineQuery(
      ObjectMapperFactory objectMapperFactory) {
    return new WrappedResponse<>(objectMapperFactory);
  }

  @Bean
  public WrappedResponse<Temi17UteRou> responseRoutine(ObjectMapperFactory objectMapperFactory) {
    return new WrappedResponse<>(objectMapperFactory);
  }

  @Bean
  public WrappedResponse<List<Temi17UteRou>> responseRoutineList(
      ObjectMapperFactory objectMapperFactory) {
    return new WrappedResponse<>(objectMapperFactory);
  }

  @Bean
  public XWrappedResponse<Temi14UteCat, List<TreeNode<Temi14UteCat, Integer>>> responseCategories(
      ObjectMapperFactory objectMapperFactory) {
    return new XWrappedResponse<>(objectMapperFactory);
  }
  @Bean
  public XWrappedResponse<Temi15UteQue, List<Object>> responseExecutor(
      ObjectMapperFactory objectMapperFactory) {
    return new XWrappedResponse<>(objectMapperFactory);
  }

  @Bean
  public WrappedResponse<XWrappedResponse<Temi15UteQue, List<Object>>> responseTemi15ListObject(
      ObjectMapperFactory objectMapperFactory) {
    return new WrappedResponse<>(objectMapperFactory);
  }

  @Bean
  public WrappedResponse<List<XWrappedResponse<Temi15UteQue, List<Object>>>>
      responseListTemi15ListObject(ObjectMapperFactory objectMapperFactory) {
    return new XWrappedResponse<>(objectMapperFactory);
  }

  @Bean
  public WrappedResponse<QueryToJson> responseQueryToJson(ObjectMapperFactory objectMapperFactory) {
    return new WrappedResponse<>(objectMapperFactory);
  }

  @Bean
  public WrappedResponse<ErtaGuiUser> responseUser(ObjectMapperFactory objectMapperFactory) {
    return new WrappedResponse<>(objectMapperFactory);
  }

  @Bean
  public WrappedResponse<String> responseString(ObjectMapperFactory objectMapperFactory) {
    return new WrappedResponse<>(objectMapperFactory);
  }

  @Bean
  public WrappedResponse<Integer> responseInteger(ObjectMapperFactory objectMapperFactory) {
    return new WrappedResponse<>(objectMapperFactory);
  }
}

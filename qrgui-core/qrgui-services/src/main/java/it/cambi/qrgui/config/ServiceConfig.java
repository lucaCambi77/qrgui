package it.cambi.qrgui.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.cambi.qrgui.model.Temi13DtbInf;
import it.cambi.qrgui.model.Temi14UteCat;
import it.cambi.qrgui.model.Temi15UteQue;
import it.cambi.qrgui.model.Temi16QueCatAss;
import it.cambi.qrgui.model.Temi17UteRou;
import it.cambi.qrgui.model.Temi18RouQue;
import it.cambi.qrgui.model.Temi20AnaTipCat;
import it.cambi.qrgui.query.model.QueryExecutionResponse;
import it.cambi.qrgui.query.model.QueryToJson;
import it.cambi.qrgui.response.model.ErtaGuiUser;
import it.cambi.qrgui.util.TreeNode;
import it.cambi.qrgui.util.objectMapper.ObjectMapperFactory;
import it.cambi.qrgui.util.wrappedResponse.WrappedResponse;
import it.cambi.qrgui.util.wrappedResponse.XWrappedResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class ServiceConfig {

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

  @Bean
  public WrappedResponse<QueryExecutionResponse> responseQueryExecution(
      ObjectMapperFactory objectMapperFactory) {
    return new WrappedResponse<>(objectMapperFactory);
  }
}

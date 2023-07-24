package it.cambi.qrgui.rest;

import it.cambi.qrgui.api.wrappedResponse.WrappedResponse;
import it.cambi.qrgui.model.Temi15UteQue;
import it.cambi.qrgui.model.Temi16QueCatAss;
import it.cambi.qrgui.services.emia.api.ITemi16Service;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@RequestMapping("/queCatAssoc")
@RestController
@Slf4j
@RequiredArgsConstructor
public class QueryCategoryController {

  private final ITemi16Service<Temi16QueCatAss> temi16Service;
  private final ObjectMapper mapper;

  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  public WrappedResponse<?> getQueCatAssoc(@RequestParam("tipCateg") List<String> functions)
      throws JsonProcessingException {

    List<Temi16QueCatAss> temi16QueCatAsses = temi16Service.findByCategory(functions);

    return WrappedResponse.baseBuilder()
        .entity(
            mapper.readValue(
                mapper.writeValueAsString(temi16QueCatAsses),
                new TypeReference<List<Temi16QueCatAss>>() {}))
        .count(temi16QueCatAsses.size())
        .build();
  }

  @PostMapping(
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @RequestMapping("post")
  public WrappedResponse<?> addQueriesToCategory(@RequestBody List<Temi16QueCatAss> temi16) {
    return WrappedResponse.baseBuilder().count(temi16Service.addQueriesToCateg(temi16)).build();
  }
}

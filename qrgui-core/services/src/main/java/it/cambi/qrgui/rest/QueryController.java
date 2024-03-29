package it.cambi.qrgui.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.cambi.qrgui.api.model.UteQueDto;
import it.cambi.qrgui.api.wrappedResponse.WrappedResponse;
import it.cambi.qrgui.model.Temi15UteQue;
import it.cambi.qrgui.model.Temi15UteQueId;
import it.cambi.qrgui.services.emia.api.ITemi15Service;
import jakarta.servlet.http.HttpServletRequest;
import java.text.ParseException;
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

@RequestMapping("/query")
@RestController
@Slf4j
@RequiredArgsConstructor
public class QueryController {
  private final ITemi15Service<Temi15UteQue> temi15Service;
  private final ObjectMapper mapper;

  @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
  @RequestMapping("tipCateg")
  public WrappedResponse<?> getByTipCateg(
      @RequestParam(value = "tipCatInput", required = false) List<String> tipCatInput,
      @RequestParam(value = "tipCat", required = false) List<String> functions,
      @RequestBody(required = false) List<Temi15UteQue> queries,
      HttpServletRequest sr)
      throws JsonProcessingException {

    List<Temi15UteQue> temi15UteQues = temi15Service.getByTipCateg(tipCatInput, queries, functions);

    return WrappedResponse.baseBuilder()
        .entity(
            mapper.readValue(
                mapper.writeValueAsString(temi15UteQues),
                new TypeReference<List<Temi15UteQue>>() {}))
        .count(temi15UteQues.size())
        .build();
  }

  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  @RequestMapping("associatedQuery")
  public WrappedResponse<?> getAlreadyAssociatedQuery(
      @RequestParam("tipCat") String tipCat,
      @RequestParam("cat") Integer ccat,
      @RequestParam("insCat") String insCat,
      HttpServletRequest sr)
      throws ParseException {
    List<Object> list = temi15Service.getAlreadyAssociatedQuery(ccat, insCat);

    return WrappedResponse.baseBuilder().entity(list).count(list.size()).build();
  }

  @PostMapping(
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  public WrappedResponse<?> postQuery(@RequestBody UteQueDto que, HttpServletRequest sr)
      throws JsonProcessingException {
    log.info("... salvo una nuova query");

    return WrappedResponse.baseBuilder()
        .entity(
            mapper.readValue(
                mapper.writeValueAsString(temi15Service.postQuery(que, sr.getLocale().toString())),
                Temi15UteQue.class))
        .build();
  }

  @PostMapping(
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @RequestMapping("delete")
  public WrappedResponse<?> deleteQuery(@RequestBody Temi15UteQueId key, HttpServletRequest sr)
      throws JsonProcessingException {
    log.info("... cancello associazione categorie - query");

    return WrappedResponse.baseBuilder()
        .entity(
            mapper.readValue(
                mapper.writeValueAsString(temi15Service.deleteQuery(key)), Temi15UteQue.class))
        .build();
  }
}

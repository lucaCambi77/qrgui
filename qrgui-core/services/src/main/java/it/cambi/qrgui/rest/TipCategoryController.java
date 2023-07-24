package it.cambi.qrgui.rest;

import it.cambi.qrgui.api.wrappedResponse.WrappedResponse;
import it.cambi.qrgui.model.Temi20AnaTipCat;
import it.cambi.qrgui.services.emia.api.ITemi20Service;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@RequestMapping("/anaTipCat")
@RestController
@Slf4j
@RequiredArgsConstructor
public class TipCategoryController {
  private final ITemi20Service<Temi20AnaTipCat> temi20Service;
  private final ObjectMapper mapper;

  @GetMapping
  public WrappedResponse<?> getAnaTipCat(@RequestParam("tipCateg") List<String> functions)
      throws JsonProcessingException {
    List<Temi20AnaTipCat> temi20AnaTipCats = temi20Service.getByCategory(functions);

    return WrappedResponse.baseBuilder()
        .entity(
            mapper.readValue(
                mapper.writeValueAsString(temi20AnaTipCats),
                new TypeReference<List<Temi20AnaTipCat>>() {}))
        .count(temi20AnaTipCats.size())
        .build();
  }
}

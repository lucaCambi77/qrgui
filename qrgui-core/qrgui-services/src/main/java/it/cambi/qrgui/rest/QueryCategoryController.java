package it.cambi.qrgui.rest;

import it.cambi.qrgui.api.wrappedResponse.WrappedResponse;
import it.cambi.qrgui.model.Temi16QueCatAss;
import it.cambi.qrgui.services.emia.api.ITemi16Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/queCatAssoc")
@RestController
@Slf4j
@RequiredArgsConstructor
public class QueryCategoryController {
  private final ITemi16Service<Temi16QueCatAss> temi16Service;

  @GetMapping
  public WrappedResponse<?> getQueCatAssoc(@RequestParam("tipCateg") List<String> functions) {

    List<Temi16QueCatAss> temi16QueCatAsses = temi16Service.findByCategory(functions);

    return WrappedResponse.baseBuilder()
        .entity(temi16QueCatAsses)
        .count(temi16QueCatAsses.size())
        .build();
  }

  @PostMapping
  @RequestMapping("post")
  public WrappedResponse<?> addQueriesToCategory(
      @RequestBody List<Temi16QueCatAss> temi16) {
    return WrappedResponse.baseBuilder().count(temi16Service.addQueriesToCateg(temi16)).build();
  }
}

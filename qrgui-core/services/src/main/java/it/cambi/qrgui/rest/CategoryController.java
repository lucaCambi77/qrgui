package it.cambi.qrgui.rest;

import it.cambi.qrgui.api.model.CategoryDto;
import it.cambi.qrgui.api.wrappedResponse.WrappedResponse;
import it.cambi.qrgui.model.Temi14UteCat;
import it.cambi.qrgui.model.Temi16QueCatAss;
import it.cambi.qrgui.services.emia.api.ITemi14Service;
import it.cambi.qrgui.util.TreeNode;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import lombok.RequiredArgsConstructor;
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

@RequestMapping("/category")
@RestController
@RequiredArgsConstructor
public class CategoryController {
  private final ITemi14Service<Temi14UteCat> temi14Service;
  private final ObjectMapper mapper;

  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  public WrappedResponse<?> getCategories(@RequestParam("tipCateg") List<String> functions)
      throws JsonProcessingException {

    List<TreeNode<Temi14UteCat, Integer>> categories = temi14Service.findAll(functions, null);

    return WrappedResponse.baseBuilder()
        .entity(
            mapper.readValue(
                mapper.writeValueAsString(categories),
                new TypeReference<List<TreeNode<Temi14UteCat, Integer>>>() {}))
        .count(categories.size())
        .build();
  }

  @PostMapping(
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  public WrappedResponse<?> postCategory(
      @RequestParam("tipCateg") List<String> functions,
      @RequestBody Temi14UteCat temi14,
      HttpServletRequest sr)
      throws JsonProcessingException {
    List<TreeNode<Temi14UteCat, Integer>> categories =
        temi14Service.saveCategory(functions, temi14);

    return WrappedResponse.baseBuilder()
        .entity(
            mapper.readValue(
                mapper.writeValueAsString(categories),
                new TypeReference<List<TreeNode<Temi14UteCat, Integer>>>() {}))
        .count(categories.size())
        .build();
  }

  @PostMapping(
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @RequestMapping("delete")
  public WrappedResponse<?> deleteCategory(
      @RequestParam("tipCateg") List<String> functions, @RequestBody CategoryDto cat)
      throws JsonProcessingException {

    temi14Service.reAssocAndDeleteOld(cat);
    List<TreeNode<Temi14UteCat, Integer>> categories = temi14Service.deleteCategory(functions, cat);

    return WrappedResponse.baseBuilder()
        .entity(
            mapper.readValue(
                mapper.writeValueAsString(categories),
                new TypeReference<List<TreeNode<Temi14UteCat, Integer>>>() {}))
        .count(categories.size())
        .build();
  }
}

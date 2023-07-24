package it.cambi.qrgui.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.cambi.qrgui.api.wrappedResponse.WrappedResponse;
import it.cambi.qrgui.model.Temi17UteRou;
import it.cambi.qrgui.model.Temi17UteRouId;
import it.cambi.qrgui.services.emia.api.ITemi17Service;
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

@RequestMapping("/routine")
@RestController
@Slf4j
@RequiredArgsConstructor
public class RoutineController {
  private final ITemi17Service<Temi17UteRou> temi17Service;
  private final ObjectMapper mapper;

  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  public WrappedResponse<?> getRoutines(@RequestParam("tipCateg") List<String> functions)
      throws JsonProcessingException {
    log.info("... cerco tutte le routines");

    List<Temi17UteRou> temi17UteRous = temi17Service.findAll(functions);

    return WrappedResponse.baseBuilder()
        .entity(
            mapper.readValue(
                mapper.writeValueAsString(temi17UteRous),
                new TypeReference<List<Temi17UteRou>>() {}))
        .count(temi17UteRous.size())
        .build();
  }

  @PostMapping(
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  public WrappedResponse<?> postRoutine(@RequestBody Temi17UteRou temi17)
      throws JsonProcessingException {
    log.info("... creo una nuova routine");

    return WrappedResponse.baseBuilder()
        .entity(
            mapper.readValue(
                mapper.writeValueAsString(temi17Service.merge(temi17)), Temi17UteRou.class))
        .build();
  }

  @PostMapping(
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @RequestMapping("delete")
  public WrappedResponse<?> deleteRoutine(@RequestBody Temi17UteRouId crou) {
    log.info("... cancella la routine " + crou);
    temi17Service.delete(crou);
    return WrappedResponse.baseBuilder().entity(crou).build();
  }
}

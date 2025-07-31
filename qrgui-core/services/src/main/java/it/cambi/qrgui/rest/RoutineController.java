package it.cambi.qrgui.rest;

import it.cambi.qrgui.api.wrappedResponse.WrappedResponse;
import it.cambi.qrgui.dto.Temi17UteRouDto;
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

  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  public WrappedResponse<?> getRoutines(@RequestParam("tipCateg") List<String> functions) {
    log.info("... cerco tutte le routines");

    List<Temi17UteRouDto> temi17UteRous = temi17Service.findAll(functions);

    return WrappedResponse.baseBuilder().entity(temi17UteRous).count(temi17UteRous.size()).build();
  }

  @PostMapping(
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  public WrappedResponse<?> postRoutine(@RequestBody Temi17UteRou temi17) {
    log.info("... creo una nuova routine");

    return WrappedResponse.baseBuilder().entity(temi17Service.merge(temi17)).build();
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

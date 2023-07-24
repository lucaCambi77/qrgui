package it.cambi.qrgui.rest;

import it.cambi.qrgui.api.wrappedResponse.WrappedResponse;
import it.cambi.qrgui.model.Temi18RouQue;
import it.cambi.qrgui.model.Temi18RouQueId;
import it.cambi.qrgui.services.emia.api.ITemi18Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/routQuery")
@RestController
@Slf4j
@RequiredArgsConstructor
public class RoutineQueryController {
  private final ITemi18Service<Temi18RouQue> temi18Service;

  @PostMapping(
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  public WrappedResponse<?> postQueRoutine(@RequestBody Temi18RouQueId temi18Pk) {

    return WrappedResponse.baseBuilder().entity(temi18Service.merge(temi18Pk)).build();
  }

  @PostMapping(
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE)
  @RequestMapping("delete")
  public WrappedResponse<?> deleteQueRoutineAssoc(@RequestBody Temi18RouQueId temi18Pk) {

    return WrappedResponse.baseBuilder()
        .entity(temi18Service.deleteQueRoutineAssoc(temi18Pk))
        .build();
  }
}

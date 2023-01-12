package it.cambi.qrgui.rest;

import it.cambi.qrgui.api.wrappedResponse.WrappedResponse;
import it.cambi.qrgui.model.Temi13DtbInf;
import it.cambi.qrgui.services.emia.api.ITemi13Service;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/dbInfo")
@RestController
@RequiredArgsConstructor
public class DbInfoController {

    private final ITemi13Service<Temi13DtbInf> databaseInfoService;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public WrappedResponse<?> getDatabaseInfoList() {

        List<Temi13DtbInf> temi13DtbInfList = databaseInfoService.findAll();

        return WrappedResponse.baseBuilder()
                .entity(temi13DtbInfList)
                .count(temi13DtbInfList.size())
                .build();
    }
}

package it.cambi.qrgui.rest;

import it.cambi.qrgui.api.model.UteQueDto;
import it.cambi.qrgui.api.wrappedResponse.WrappedResponse;
import it.cambi.qrgui.model.Temi15UteQue;
import it.cambi.qrgui.model.Temi15UteQueId;
import it.cambi.qrgui.services.emia.api.ITemi15Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.util.List;

@RequestMapping("/query")
@RestController
@Slf4j
@RequiredArgsConstructor
public class QueryController {
    private final ITemi15Service<Temi15UteQue> temi15Service;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @RequestMapping("tipCateg")
    public WrappedResponse<?> getByTipCateg(
            @RequestParam(value = "tipCatInput", required = false) List<String> tipCatInput,
            @RequestParam(value = "tipCat", required = false) List<String> functions,
            @RequestBody(required = false) List<Temi15UteQue> queries,
            HttpServletRequest sr) {

        List<Temi15UteQue> temi15UteQues = temi15Service.getByTipCateg(tipCatInput, queries, functions);

        return WrappedResponse.baseBuilder().entity(temi15UteQues).count(temi15UteQues.size()).build();
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @RequestMapping("associatedQuery")
    public WrappedResponse<?> getAlreadyAssociatedQuery(
            @RequestParam("tipCat") String tipCat,
            @RequestParam("cat") Integer ccat,
            @RequestParam("insCat") String insCat,
            HttpServletRequest sr) throws ParseException {
        List<Object> list = temi15Service.getAlreadyAssociatedQuery(ccat, insCat);

        return WrappedResponse.baseBuilder().entity(list).count(list.size()).build();
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public WrappedResponse<?> postQuery(@RequestBody UteQueDto que, HttpServletRequest sr) {
        log.info("... salvo una nuova query");

        return WrappedResponse.baseBuilder()
                .entity(temi15Service.postQuery(que, sr.getLocale().toString()))
                .build();
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @RequestMapping("delete")
    public WrappedResponse<?> deleteQuery(@RequestBody Temi15UteQueId key, HttpServletRequest sr) {
        log.info("... cancello associazione categorie - query");
        return WrappedResponse.baseBuilder().entity(temi15Service.deleteQuery(key)).build();
    }
}
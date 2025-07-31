package it.cambi.qrgui.client;

import it.cambi.qrgui.api.wrappedResponse.WrappedResponse;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@org.springframework.cloud.openfeign.FeignClient(name = "categoryService", url = "${services.contextPath}")
public interface FeignClient {

    @GetMapping("/category")
    WrappedResponse<?> getCategory(@RequestParam("tipCateg") List<String> tipCateg);

    @GetMapping("/anaTipCat")
    WrappedResponse<?> getTipCategory(@RequestParam("tipCateg") List<String> tipCateg);
}
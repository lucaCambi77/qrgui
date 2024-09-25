package it.cambi.qrgui.client;

import it.cambi.qrgui.api.wrappedResponse.WrappedResponse;
import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "categoryService", url = "${services.contextPath}")
public interface CategoryFeignClient {

    @GetMapping("/category")
    WrappedResponse<?> getCategory(@RequestParam("tipCateg") List<String> tipCateg);
}
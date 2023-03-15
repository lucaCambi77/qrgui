package it.cambi.qrgui.rest;

import it.cambi.qrgui.api.model.CategoryDto;
import it.cambi.qrgui.api.wrappedResponse.WrappedResponse;
import it.cambi.qrgui.dao.entity.api.ITemi15Dao;
import it.cambi.qrgui.model.Temi14UteCat;
import it.cambi.qrgui.model.Temi15UteQue;
import it.cambi.qrgui.model.Temi15UteQueId;
import it.cambi.qrgui.services.emia.api.ITemi14Service;
import it.cambi.qrgui.util.TreeNode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RequestMapping("/category")
@RestController
@RequiredArgsConstructor
public class CategoryController {
    private final ITemi14Service<Temi14UteCat> temi14Service;
    private final ITemi15Dao<Temi15UteQue, Temi15UteQueId> queryDao;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public WrappedResponse<?> getCategories(@RequestParam("tipCateg") List<String> functions) {

        List<TreeNode<Temi14UteCat, Integer>> categories = temi14Service.findAll(functions, null);

        return WrappedResponse.baseBuilder().entity(categories).count(categories.size()).build();
    }

    @PostMapping
    public WrappedResponse<?> postCategory(
            @RequestParam("tipCateg") List<String> functions,
            @RequestBody Temi14UteCat temi14,
            HttpServletRequest sr) {
        List<TreeNode<Temi14UteCat, Integer>> categories =
                temi14Service.saveCategory(functions, temi14);

        return WrappedResponse.baseBuilder().entity(categories).count(categories.size()).build();
    }

    @PostMapping
    @RequestMapping("delete")
    public WrappedResponse<?> deleteCategory(
            @RequestParam("tipCateg") List<String> functions,
            @RequestBody CategoryDto cat) {

        temi14Service.reAssocAndDeleteOld(cat);
        List<TreeNode<Temi14UteCat, Integer>> categories = temi14Service.deleteCategory(functions, cat);

        return WrappedResponse.baseBuilder().entity(categories).count(categories.size()).build();
    }
}
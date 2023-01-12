package it.cambi.qrgui.services.emia.api;

import it.cambi.qrgui.api.model.CategoryDto;
import it.cambi.qrgui.model.Temi14UteCat;
import it.cambi.qrgui.model.Temi14UteCatId;
import it.cambi.qrgui.util.TreeNode;

import java.util.List;

public interface ITemi14Service<T> {
    List<TreeNode<Temi14UteCat, Integer>> findAll(List<String> functions, Temi14UteCatId id);

    List<TreeNode<Temi14UteCat, Integer>> saveCategory(List<String> functions, Temi14UteCat temi14);

    List<TreeNode<Temi14UteCat, Integer>> deleteCategory(List<String> functions, CategoryDto categoryDeleteDto);

    void reAssocAndDeleteOld(CategoryDto cat);
}

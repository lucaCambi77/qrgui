package it.cambi.qrgui.services;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import it.cambi.qrgui.db.model.Temi14UteCat;
import it.cambi.qrgui.db.model.Temi14UteCatId;
import it.cambi.qrgui.services.exception.NoCategoriesAllowedException;
import it.cambi.qrgui.services.util.TreeNode;
import it.cambi.qrgui.services.util.wrappedResponse.XWrappedResponse;

public interface ITemi14Service<T>
{
    XWrappedResponse<Temi14UteCat, List<TreeNode<T, Long>>> findAll(HttpServletRequest sr, Temi14UteCatId id) throws NoCategoriesAllowedException;

    XWrappedResponse<Temi14UteCat, List<TreeNode<T, Long>>> saveCategory(HttpServletRequest sr, Temi14UteCat temi14)
            throws NoCategoriesAllowedException;

    XWrappedResponse<Temi14UteCat, List<TreeNode<T, Long>>> deleteCategory(HttpServletRequest sr, Temi14UteCat pk)
            throws NoCategoriesAllowedException;

}

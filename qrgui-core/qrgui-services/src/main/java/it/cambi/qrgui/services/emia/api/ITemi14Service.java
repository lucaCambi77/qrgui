package it.cambi.qrgui.services.emia.api;

import it.cambi.qrgui.model.Temi14UteCat;
import it.cambi.qrgui.model.Temi14UteCatId;
import it.cambi.qrgui.services.exception.NoCategoriesAllowedException;
import it.cambi.qrgui.util.TreeNode;
import it.cambi.qrgui.util.wrappedResponse.XWrappedResponse;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface ITemi14Service<T> {
  XWrappedResponse<Temi14UteCat, List<TreeNode<T, Integer>>> findAll(
      HttpServletRequest sr, Temi14UteCatId id) throws NoCategoriesAllowedException;

  XWrappedResponse<Temi14UteCat, List<TreeNode<T, Integer>>> saveCategory(
      HttpServletRequest sr, Temi14UteCat temi14) throws NoCategoriesAllowedException;

  XWrappedResponse<Temi14UteCat, List<TreeNode<T, Integer>>> deleteCategory(
      HttpServletRequest sr, Temi14UteCat pk) throws NoCategoriesAllowedException;
}

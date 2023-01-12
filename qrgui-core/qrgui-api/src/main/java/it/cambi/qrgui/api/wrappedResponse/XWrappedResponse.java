package it.cambi.qrgui.api.wrappedResponse;

import lombok.experimental.SuperBuilder;

/**
 * Classe utilizzata nel caso che oltre alla entity della WrappedResponse si voglia utilizzare
 * un'ulteriore entity da wrappare come nel caso del task executor in cui oltre al result set ho
 * necessità di salvare la query
 *
 * @author luca
 * @param <T>
 * @param <X>
 */
@SuperBuilder(toBuilder = true)
public class XWrappedResponse<T, X> extends WrappedResponse<X> {
  private T xentity;

  public T getXentity() {
    return xentity;
  }

  public XWrappedResponse() {
    super();
  }
}

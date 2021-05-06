package it.cambi.qrgui.util.wrappedResponse;

import java.util.List;

/**
 * Classe utilizzata nel caso che oltre alla entity della WrappedResponse si voglia utilizzare un'ulteriore entity da wrappare come nel caso del task
 * executor in cui oltre al result set ho necessità di salvare la query
 * 
 * @author luca
 *
 * @param <T>
 * @param <X>
 */
public class XWrappedResponse<T, X> extends WrappedResponse<X>
{
    private T xentity;

    public T getXentity()
    {
        return xentity;
    }

    public XWrappedResponse<T, X> setXentity(T xentity)
    {
        this.xentity = xentity;
        return this;
    }

    @Override
    public XWrappedResponse<T, X> setSuccess(boolean success)
    {
        super.setSuccess(success);
        return this;
    }

    @Override
    public XWrappedResponse<T, X> setErrorMessages(List<String> errorMessage)
    {
        super.setErrorMessages(errorMessage);
        return this;
    }

    @Override
    public XWrappedResponse<T, X> setEntity(X entity)
    {
        super.setEntity(entity);
        return this;
    }

    @Override
    public XWrappedResponse<T, X> setCount(Integer count)
    {
        super.setCount(count);
        return this;
    }

    @Override
    public XWrappedResponse<T, X> setResponse()
    {
        super.setResponse();
        return this;
    }
}

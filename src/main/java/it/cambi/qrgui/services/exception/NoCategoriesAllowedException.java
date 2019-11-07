package it.cambi.qrgui.services.exception;

public class NoCategoriesAllowedException extends Exception
{

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public NoCategoriesAllowedException()
    {
        super("L'utente corrente non ha accesso alla visualizzazione di nessuna categoria");
    }
}

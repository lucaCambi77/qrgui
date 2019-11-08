package it.cambi.qrgui.enums;

public enum Providers
{

    HIBERNATE("org.hibernate"), ECLIPSE_LINK("org.eclipse");

    private String provider;

    Providers(String provider)
    {
        this.provider = provider;
    }

    public String getProvider()
    {
        return provider;
    }

}

package it.cambi.qrgui.query.model;

import it.cambi.qrgui.enums.JavaTypes;

public class SelectColumns
{

    private String as;
    private JavaTypes type;

    public String getAs()
    {
        return as;
    }

    public void setAs(String as)
    {
        this.as = as;
    }

    public JavaTypes getType()
    {
        return type;
    }

    public void setType(JavaTypes type)
    {
        this.type = type;
    }
}

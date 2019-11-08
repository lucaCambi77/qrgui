package it.cambi.qrgui.query.model;

import it.cambi.qrgui.enums.JavaTypes;

public class Parameter
{

    private String name;
    private JavaTypes type;
    private String def;
    private String value;

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public JavaTypes getType()
    {
        return type;
    }

    public void setType(JavaTypes type)
    {
        this.type = type;
    }

    public String getDef()
    {
        return def;
    }

    public void setDef(String def)
    {
        this.def = def;
    }

    public String getValue()
    {
        return value;
    }

    public void setValue(String value)
    {
        this.value = value;
    }
}

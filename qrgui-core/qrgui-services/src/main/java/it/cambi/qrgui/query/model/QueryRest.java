package it.cambi.qrgui.query.model;

import it.cambi.qrgui.model.Temi15UteQue;

public class QueryRest
{

    private Temi15UteQue temi15;
    private QueryToJson json;

    public Temi15UteQue getTemi15()
    {
        return temi15;
    }

    public void setTemi15(Temi15UteQue temi15)
    {
        this.temi15 = temi15;
    }

    public QueryToJson getJson()
    {
        return json;
    }

    public void setJson(QueryToJson json)
    {
        this.json = json;
    }

}

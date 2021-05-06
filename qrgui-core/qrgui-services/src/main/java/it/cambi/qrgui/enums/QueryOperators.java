package it.cambi.qrgui.enums;

public enum QueryOperators
{

    GT(">"), GE(">="), LT("<"), LE("<="), EQ("="), NE("!="), IN("IN"), LIKE("LIKE");

    private String operator;

    QueryOperators(String operator)
    {
        this.operator = operator;
    }

    public String getOperator()
    {
        return operator;
    }
}

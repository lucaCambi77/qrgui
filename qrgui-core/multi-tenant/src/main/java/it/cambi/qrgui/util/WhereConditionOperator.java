/**
 *
 */
package it.cambi.qrgui.util;

/**
 * @author luca
 */
public enum WhereConditionOperator {
    GT(">"),
    GE(">="),
    LT("<"),
    LE("<="),
    EQ("="),
    NE("!="),
    IN("IN"),
    BETWEEN("BETWEEN"),
    ISNULL("ISNULL"),
    ISNOTNULL("ISNOTNULL"),
    LIKE("LIKE");

    WhereConditionOperator(String operator) {

        this.operator = operator;
    }

    private String operator;

    public String getName() {
        return operator;
    }
}

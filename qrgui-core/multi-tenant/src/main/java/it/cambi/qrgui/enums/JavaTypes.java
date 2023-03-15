package it.cambi.qrgui.enums;

import java.util.HashMap;
import java.util.Map;

public enum JavaTypes {
  BOOLEAN("java.lang.Boolean"),
  BIGDECIMAL("java.math.BigDecimal"),
  BYTE("java.lang.Byte"),
  DATE("java.util.Date"),
  DATE_TRUNC("java.sql"),
  DOUBLE("java.lang.Double"),
  FLOAT("java.lang.Float"),
  INTEGER("java.lang.Integer"),
  LONG("java.lang.Long"),
  SHORT("java.lang.Short"),
  STRING("java.lang.String"),
  NUMBER("java.lang.number");

  private final String clazz;
  private static final Map<String, JavaTypes> map = new HashMap<>();

  static {
    for (JavaTypes legEnum : JavaTypes.values()) {
      map.put(legEnum.getClazz(), legEnum);
    }
  }

  JavaTypes(String classString) {
    this.clazz = classString;
  }

  public String getClazz() {
    return clazz;
  }

  public static JavaTypes getJavaType(String clazz) {
    return map.get(clazz);
  }
}

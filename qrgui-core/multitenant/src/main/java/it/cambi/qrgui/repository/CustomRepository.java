package it.cambi.qrgui.repository;

import java.util.List;

public interface CustomRepository {

    List<Object> getByNativeQuery(String nativeQuery, Integer page);

    List<Object> getByNativeQuery(String nativeQuery, Integer page, Integer pageSize);

    Long executeQueryCount(String finalQuery);
}

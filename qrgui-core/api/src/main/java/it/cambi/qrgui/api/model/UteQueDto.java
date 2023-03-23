package it.cambi.qrgui.api.model;
// Generated Apr 10, 2018 11:58:47 AM by Hibernate Tools 3.6.0.Final

import lombok.Builder;
import lombok.With;

import java.util.Date;
import java.util.Objects;
import java.util.Set;

@Builder
@With
public record UteQueDto(String tenant, String nam, String json, Set<QueCatAssDto> Temi16QueCatAsses,
                        Set<RouQueDto> rouQues, long que, Date insQue) {
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UteQueDto that = (UteQueDto) o;
        return que == that.que && insQue.equals(that.insQue);
    }

    @Override
    public int hashCode() {
        return Objects.hash(que, insQue);
    }
}

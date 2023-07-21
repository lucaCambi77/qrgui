package it.cambi.qrgui.api.model;

import java.util.Date;
import java.util.Objects;
import java.util.Set;
import lombok.Builder;
import lombok.With;

@Builder
@With
public record UteQueDto(String tenant, String nam, String json, Set<QueCatAssDto> temi16QueCatAsses,
                        Set<RouQueDto> temi18RouQues, long que, Date insQue) {
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

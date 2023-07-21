package it.cambi.qrgui.taskExecutor.repository;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class DummyEntity {

  @Id private Long id;
}

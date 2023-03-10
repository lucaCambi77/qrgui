package it.cambi.qrgui.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GenericRepository extends JpaRepository<DummyEntity, Long>, CustomRepository {

}

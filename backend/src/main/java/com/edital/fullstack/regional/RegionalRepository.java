package com.edital.fullstack.regional;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RegionalRepository extends JpaRepository<Regional, Long> {
  Optional<Regional> findFirstByRegionalIdAndAtivoTrue(Integer regionalId);
  List<Regional> findByAtivoTrue();
}

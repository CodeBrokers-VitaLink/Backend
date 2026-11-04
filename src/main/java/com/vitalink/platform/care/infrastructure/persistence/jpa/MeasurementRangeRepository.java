package com.vitalink.platform.care.infrastructure.persistence.jpa;

import com.vitalink.platform.care.domain.model.aggregates.MeasurementRange;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MeasurementRangeRepository extends JpaRepository<MeasurementRange, String> {}

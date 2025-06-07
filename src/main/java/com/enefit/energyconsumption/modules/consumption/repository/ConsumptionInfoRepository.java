package com.enefit.energyconsumption.modules.consumption.repository;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.enefit.energyconsumption.modules.consumption.models.entity.Consumption;

/**
 * Repository to handle all the Consumption CRUD operations
 *
 * @author Keshani
 * @since 2025/05/31
 */
@Repository
public interface ConsumptionInfoRepository extends JpaRepository<Consumption, String> {
    @Query("""
    SELECT c FROM Consumption c
    WHERE c.meteringPoint.user.userId = :userId
      AND c.consumptionTime BETWEEN :startDate AND :endDate
     order by c.consumptionTime ASC
""")
    List<Consumption> findAllByUserIdAndConsumptionDatePeriod(@Param("userId") Long userId,
            @Param("startDate") OffsetDateTime startDate,
            @Param("endDate") OffsetDateTime endDate);
}


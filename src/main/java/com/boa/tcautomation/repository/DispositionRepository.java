package com.boa.tcautomation.repository;

import com.boa.tcautomation.model.DispositionMetrics;
import org.springframework.data.jpa.repository.JpaRepository;

import java.sql.Date;
import java.util.List;

public interface DispositionRepository extends JpaRepository<DispositionMetrics, Integer> {
    List<DispositionMetrics> findByAppId(Integer appId);
    List<DispositionMetrics> findByKtbId(Integer ktbId);
    List<DispositionMetrics> findByDbType(String dbType);
    List<DispositionMetrics> findByTotalCount(Integer totalCount);
    List<DispositionMetrics> findByCompletedCount(Integer completedCount);
    List<DispositionMetrics> findByFalsePositiveCount(Integer falsePositiveCount);
    List<DispositionMetrics> findByUddCount(Integer uddCount);
    List<DispositionMetrics> findByProvidedBy(String providedBy);
    List<DispositionMetrics> findByProvidedOn(Date providedOn);
    List<DispositionMetrics> findByDispositionOrderBy(Integer dispositionOrderBy);
    List<DispositionMetrics> findByIsActive(Integer isActive);
    List<DispositionMetrics> findBySustainId(Integer sustainId);
    List<DispositionMetrics> findBySustainOrder(Integer sustainOrder);
    List<DispositionMetrics> findByValidationTimeStart(Date validationTimeStart);
    List<DispositionMetrics> findByValidationTimeEnd(Date validationTimeEnd);
    List<DispositionMetrics> findByDiscoveryType(String discoveryType);
}

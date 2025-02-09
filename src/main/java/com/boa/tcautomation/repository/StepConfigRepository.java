// StepConfigRepository.java
package com.boa.tcautomation.repository;

import com.boa.tcautomation.model.StepConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StepConfigRepository extends JpaRepository<StepConfig, String> {
}

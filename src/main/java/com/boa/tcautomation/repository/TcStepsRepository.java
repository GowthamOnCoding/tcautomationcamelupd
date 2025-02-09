
package com.boa.tcautomation.repository;

import com.boa.tcautomation.model.TcSteps;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TcStepsRepository extends JpaRepository<TcSteps, Long> {
}

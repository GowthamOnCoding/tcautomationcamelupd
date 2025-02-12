package com.boa.tcautomation.repository;

import com.boa.tcautomation.model.HostDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface HostDetailsRepository extends JpaRepository<HostDetails, String> {
    List<HostDetails> findByIsActiveTrue();
}

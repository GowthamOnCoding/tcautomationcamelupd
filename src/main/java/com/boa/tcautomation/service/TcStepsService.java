package com.boa.tcautomation.service;

import com.boa.tcautomation.model.TcMaster;
import com.boa.tcautomation.repository.TcMasterRepository;
import com.boa.tcautomation.repository.TcStepsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;
import com.boa.tcautomation.model.TcSteps; // Adjust the package as necessary

@Service
public class TcStepsService {

    private static final Logger logger = LoggerFactory.getLogger(TcStepsService.class);

    @Autowired
    private TcStepsRepository repository;

    public List<TcSteps> findAll() {
        logger.info("Entering findAll method");
        List<TcSteps> result = repository.findAll();
        logger.info("Exiting findAll method with result: {}", result);
        return result;
    }

    public TcSteps save(TcSteps tcSteps) {
       logger.info("Entering save method with TcSteps: {}", tcSteps);
        TcSteps result = repository.save(tcSteps);
        logger.info("Exiting save method with result: {}", result);
        return result;
    }

    public TcSteps findById(String id) {
        logger.info("Entering findById method with id: {}", id);
        Long longId;
        try {
            longId = Long.valueOf(id);
        } catch (NumberFormatException e) {
            logger.error("Invalid ID format: {}", id);
            return null; // or handle it as needed
        }
        TcSteps result = repository.findById(longId).orElse(null);
        logger.info("Exiting findById method with result: {}", result);
        return result;
    }

    public void deleteById(String id) {
        logger.info("Entering deleteById method with id: {}", id);
        Long longId;
        try {
            longId = Long.valueOf(id);
        } catch (NumberFormatException e) {
            logger.error("Invalid ID format: {}", id);
            return; // or handle it as needed
        }
        repository.deleteById(longId);
        logger.info("Exiting deleteById method");
    }
}

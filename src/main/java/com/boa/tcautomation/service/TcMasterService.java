package com.boa.tcautomation.service;

import com.boa.tcautomation.model.TcMaster;
import com.boa.tcautomation.repository.TcMasterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@Service
public class TcMasterService {

    private static final Logger logger = LoggerFactory.getLogger(TcMasterService.class);

    @Autowired
    private TcMasterRepository repository;

    public List<TcMaster> findAll() {
        logger.info("Entering findAll method");
        List<TcMaster> result = repository.findAll();
        logger.info("Exiting findAll method with result: {}", result);
        return result;
    }

    public TcMaster save(TcMaster tcMaster) {
        logger.info("Entering save method with TcMaster: {}", tcMaster);
        TcMaster result = repository.save(tcMaster);
        logger.info("Exiting save method with result: {}", result);
        return result;
    }

    public TcMaster findById(String id) {
        logger.info("Entering findById method with id: {}", id);
        TcMaster result = repository.findById(id).orElse(null);
        logger.info("Exiting findById method with result: {}", result);
        return result;
    }

    public void deleteById(String id) {
        logger.info("Entering deleteById method with id: {}", id);
        repository.deleteById(id);
        logger.info("Exiting deleteById method");
    }
}

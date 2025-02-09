package com.boa.tcautomation.service;

import com.boa.tcautomation.model.ParameterSchema;
import com.boa.tcautomation.repository.ParameterSchemaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@Service
public class ParameterSchemaService {

    private static final Logger logger = LoggerFactory.getLogger(ParameterSchemaService.class);

    @Autowired
    private ParameterSchemaRepository repository;

    public List<ParameterSchema> findAll() {
        logger.info("Entering findAll method");
        List<ParameterSchema> result = repository.findAll();
        logger.info("Exiting findAll method with result: {}", result);
        return result;
    }

    public ParameterSchema save(ParameterSchema parameter) {
        logger.info("Entering save method with ParameterSchema: {}", parameter);
        ParameterSchema result = repository.save(parameter);
        logger.info("Exiting save method with result: {}", result);
        return result;
    }

    public ParameterSchema findById(String id) {
        logger.info("Entering findById method with id: {}", id);
        ParameterSchema result = repository.findById(id).orElse(null);
        logger.info("Exiting findById method with result: {}", result);
        return result;
    }

    public void deleteById(String id) {
        logger.info("Entering deleteById method with id: {}", id);
        repository.deleteById(id);
        logger.info("Exiting deleteById method");
    }
}

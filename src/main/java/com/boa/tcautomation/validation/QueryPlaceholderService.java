package com.boa.tcautomation.validation;

import com.boa.tcautomation.model.TcMaster;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class QueryPlaceholderService {
    private static final Logger log = LoggerFactory.getLogger(QueryPlaceholderService.class);

    public String processQueryPlaceholders(String query, TcMaster tcMaster) {
        if (query == null) {
            return null;
        }

        String processedQuery = query;

        // Process AIT_NO with or without prefixes
        String aitNo = tcMaster.getAitNo();
        String plainAitNo = aitNo;
        if (plainAitNo.startsWith("SDD_AIT_")) {
            plainAitNo = plainAitNo.replace("SDD_AIT_", "");
        } else if (plainAitNo.startsWith("AIT_")) {
            plainAitNo = plainAitNo.replace("AIT_", "");
        }

        processedQuery = processedQuery.replace("<AIT_NO>", plainAitNo);
        processedQuery = processedQuery.replace("<FULL_AIT_NO>", aitNo);
        processedQuery = processedQuery.replace("<TC_ID>", tcMaster.getTcId());

        // Additional placeholders can be added here as needed

        log.debug("Processed query: {}", processedQuery);
        return processedQuery;
    }
}

package com.boa.tcautomation.route;

import com.boa.tcautomation.model.TcMaster;
import com.boa.tcautomation.model.TcSteps;
import com.boa.tcautomation.service.TcExecutionService;
import com.boa.tcautomation.helper.TcMasterServiceHelper;
import com.boa.tcautomation.util.DbUtil;
import com.boa.tcautomation.util.QueryConstants;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;

/**
 * RouteBuilder implementation to query active TcMaster records and process each test case.
 */
@Component
public class QueryTcMasterRoute extends RouteBuilder {

    @Autowired
    private DbUtil dbUtil;

    @Autowired
    private TcExecutionService tcExecutionService;

    @Autowired
    private TcMasterServiceHelper tcMasterServiceHelper;

    /**
     * Configures the routes for querying TcMaster records and processing each test case.
     */
    @Override
    public void configure() {

        // Route to query active TcMaster records
        from("direct:queryTcMaster")
                .process(exchange -> {
                    // SQL query to select active TcMaster records
                    String sql = QueryConstants.SELECT_ACTIVE_TC_MASTERS;
                    // Execute the query and map the results to a list of TcMaster objects
                    List<TcMaster> activeTcMasters = dbUtil.queryForListWithMapping(sql, TcMaster.class);

                    // Create an ExecutorService to run tasks in virtual threads
                    ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor();
                    // Submit a task for each TcMaster to process the test case
                    for (TcMaster tcMaster : activeTcMasters) {
                        executor.submit(() -> {
                            // Send the TcMaster object to the direct:processTestCase route
                            getContext().createProducerTemplate().sendBody("direct:processTestCase", tcMaster);
                        });
                    }
                    // Shutdown the executor service
                    executor.shutdown();

                    // Set the list of active TcMasters as the body of the exchange
                    exchange.getIn().setBody(activeTcMasters);
                })
                .log("Active TcMasters: ${body}");

        // Route to process each test case
        from("direct:processTestCase")
                .process(exchange -> {
                    TcMaster tcMaster = exchange.getIn().getBody(TcMaster.class);
                    // Getting TcSteps for testcase from TC_STEPS table
                    List<TcSteps> tcSteps = tcMasterServiceHelper.getTcStepsByTcId(tcMaster.getTcId());
                    log.info("Tc steps for tcId: " + tcMaster.getTcId() + " are: " + tcSteps);
                    // Process each test case step
                    tcExecutionService.processTestCase(tcMaster);
                });

        // Run once timer to call the queryTcMaster endpoint
        from("timer://runOnce?repeatCount=1&delay=1000")
                .to("direct:queryTcMaster");
    }
}

package com.boa.tcautomation.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;

@Component
public class VirtualThreadExecutorService {
    private static final Logger logger = LoggerFactory.getLogger(VirtualThreadExecutorService.class);

    public CompletableFuture<Void> executeInVirtualThread(Runnable task, String taskName) {
        return CompletableFuture.runAsync(() -> {
            Thread thread = Thread.ofVirtual()
                    .name(taskName)
                    .unstarted(task);
            thread.setUncaughtExceptionHandler((t, e) ->
                    logger.error("Error in virtual thread: " + t.getName(), e));
            thread.start();
        }, Executors.newVirtualThreadPerTaskExecutor());
    }
}

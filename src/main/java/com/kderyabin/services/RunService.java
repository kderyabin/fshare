package com.kderyabin.services;


import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Threads managing service.
 */
@Service
public class RunService {
    /**
     * Executor service manages threads for the application.
     */
    ExecutorService executorService;

    public RunService() {
        executorService = Executors.newFixedThreadPool(6);
    }

    /**
     * @param nThreads Number of threads for the pool size. Default: 6 threads.
     */
    public RunService( Integer nThreads ) {
        executorService = Executors.newFixedThreadPool(nThreads);
    }

    /**
     * .
     * @return  Instance of application ExecutorService.
     */
    public ExecutorService getExecutorService() {
        return executorService;
    }
}

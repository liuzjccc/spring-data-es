package com.example.springdataes.utils;

import com.google.common.util.concurrent.ThreadFactoryBuilder;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ThreadPooUtils {
    /**
     * 基础线程池
     */
    public static final ThreadPoolExecutor threadPool;
    
    static {
        threadPool = new ThreadPoolExecutor(
                5,
                8,
                2,
                TimeUnit.SECONDS,
                new ArrayBlockingQueue(3),
                new ThreadFactoryBuilder().setNameFormat("XX-task-%d").build(),
                new ThreadPoolExecutor.CallerRunsPolicy());
    }
    
    private ThreadPooUtils() {
    }
}

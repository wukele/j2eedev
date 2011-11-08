package com.iteye.melin;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class MemoryFillerTaskExecutor {

    private static final Collection<Double> GARBAGE = new ArrayList<Double>();

    @Scheduled(fixedDelay = 1000)
    public void addGarbage() {
        System.out.println("Adding data...");
        for (int i = 0; i < 1000000; i++) {
            GARBAGE.add(Math.random());
        }

    }

}
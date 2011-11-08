package com.iteye.melin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MemoryWarningListener {

    @Autowired
    private MemoryThreadDumper threadDumper;

    public void memoryUsageLow(long usedMemory, long maxMemory) {
        System.out.println("Memory usage low!!!");
        double percentageUsed = (double) usedMemory / maxMemory;
        System.out.println("percentageUsed = " + percentageUsed);

        if (null == threadDumper) {
            throw new IllegalStateException("The Thread dumper cannot be null!");
        }

        threadDumper.dumpStacks();

    }

}

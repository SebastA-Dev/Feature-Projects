package com.register.Configuration;

import io.micronaut.context.annotation.ConfigurationProperties;

@ConfigurationProperties("micronaut.security.hash-configuration")
public class HashConfig {
    private String type;
    private int iterations;
    private int memory;
    private int parallelism;
    private int hashLength;
    private int saltLength;

    // Getters and Setters
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getIterations() {
        return iterations;
    }

    public void setIterations(int iterations) {
        this.iterations = iterations;
    }

    public int getMemory() {
        return memory;
    }

    public void setMemory(int memory) {
        this.memory = memory;
    }

    public int getParallelism() {
        return parallelism;
    }

    public void setParallelism(int parallelism) {
        this.parallelism = parallelism;
    }

    public int getHashLength() {
        return hashLength;
    }

    public void setHashLength(int hashLength) {
        this.hashLength = hashLength;
    }

    public int getSaltLength() {
        return saltLength;
    }

    public void setSaltLength(int saltLength) {
        this.saltLength = saltLength;
    }
}
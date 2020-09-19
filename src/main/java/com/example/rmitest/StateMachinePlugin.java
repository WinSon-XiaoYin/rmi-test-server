package com.example.rmitest;

import org.springframework.statemachine.config.StateMachineBuilder;

public interface StateMachinePlugin {

    public byte[] configure(byte[] data) throws Exception;
}

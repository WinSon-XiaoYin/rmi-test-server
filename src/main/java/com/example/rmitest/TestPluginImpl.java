package com.example.rmitest;

import org.springframework.context.annotation.Configuration;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.rmi.server.RMIClientSocketFactory;
import java.rmi.server.RMIServerSocketFactory;
import java.rmi.server.UnicastRemoteObject;

@Configuration
public class TestPluginImpl implements TestPlugin, Serializable {

    @Override
    public void test() {
        System.out.println("123");
    }
}

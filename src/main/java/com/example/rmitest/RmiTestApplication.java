package com.example.rmitest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.remoting.httpinvoker.HttpInvokerServiceExporter;
import org.springframework.remoting.rmi.RmiServiceExporter;
import org.springframework.web.client.RestTemplate;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

@SpringBootApplication
public class RmiTestApplication {

    @Autowired
    TestPlugin testPlugin;

    @Autowired
    DraftPlugin draftPlugin;

    @Bean
    public RmiServiceExporter rmiServiceExporter(){
        RmiServiceExporter rmiServiceExporter = new RmiServiceExporter();
        rmiServiceExporter.setServiceName("draftPlugin");
        rmiServiceExporter.setService(draftPlugin);
        rmiServiceExporter.setServiceInterface(StateMachinePlugin.class);
        rmiServiceExporter.setRegistryPort(1099);// 默认为1099，注意占用问题
        try {
            rmiServiceExporter.afterPropertiesSet();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        return rmiServiceExporter;
    }

//    @Bean(name = "/draftPlugin")
//    public HttpInvokerServiceExporter httpInvokerServiceExporter() {
//        HttpInvokerServiceExporter exporter = new HttpInvokerServiceExporter();
//        exporter.setService(draftPlugin);
//        exporter.setServiceInterface(StateMachinePlugin.class);
//        return exporter;
//    }

    public static void main(String[] args) throws RemoteException, MalformedURLException {
        SpringApplication.run(RmiTestApplication.class, args);
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}

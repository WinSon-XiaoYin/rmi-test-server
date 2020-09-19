package com.example.rmitest;

//import com.alibaba.fastjson.JSON;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.esotericsoftware.kryo.serializers.BeanSerializer;
import com.esotericsoftware.kryo.serializers.ExternalizableSerializer;
import com.esotericsoftware.kryo.serializers.JavaSerializer;
import de.javakaffee.kryoserializers.UnmodifiableCollectionsSerializer;
import org.objenesis.strategy.StdInstantiatorStrategy;
import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.config.StateMachineBuilder;

import java.io.*;
import java.util.HashSet;
import java.util.Set;

@Configuration
public class DraftPlugin implements StateMachinePlugin {

    private static final ThreadLocal<Kryo> kryoThreadLocal = new ThreadLocal<Kryo>() {

        @SuppressWarnings("rawtypes")
        @Override
        protected Kryo initialValue() {
            Kryo kryo = new Kryo();
            kryo.setReferences(true);
            kryo.setRegistrationRequired(false);
            kryo.register(StateMachineBuilder.Builder.class, new ExternalizableSerializer());
//            UnmodifiableCollectionsSerializer.registerSerializers(kryo);
            ((Kryo.DefaultInstantiatorStrategy) kryo.getInstantiatorStrategy())
                    .setFallbackInstantiatorStrategy(new StdInstantiatorStrategy());
            return kryo;
        }
    };

    public Kryo getInstance() {
        return kryoThreadLocal.get();
    }

    public byte[] serialize(Object obj) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        Output output = new Output(out);
        Kryo kryo = getInstance();
        kryo.writeClassAndObject(output, obj);
        output.flush();
        return out.toByteArray();

    }

    @SuppressWarnings("unchecked")
    private StateMachineBuilder.Builder deserialize(byte[] data) {
        if (data == null || data.length == 0) {
            return null;
        }
        Kryo kryo = getInstance();
        ByteArrayInputStream in = new ByteArrayInputStream(data);
        Input input = new Input(in);
        return kryo.readObject(input, StateMachineBuilder.Builder.class);
    }

    @Override
    public byte[] configure(byte[] data) throws Exception {
        StateMachineBuilder.Builder<String, String> builder = deserialize(data);
        System.out.println("1234556");
        Set<String> stateSets = new HashSet<String>();
        stateSets.add("S1");
        stateSets.add("S2");
        builder.configureStates().withStates().initial("S1").states(stateSets);
        builder.configureTransitions().withExternal()
                .source("S1")
                .target("S2")
                .event("E1");
        return serialize(builder);
    }
}

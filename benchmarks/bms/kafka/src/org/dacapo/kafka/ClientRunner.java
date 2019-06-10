package org.dacapo.kafka;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ClientRunner{

    Method clientStarter;

    public ClientRunner() throws Exception{
        initialize();
    }

    public void initialize() throws Exception{
        ClassLoader loader = Thread.currentThread().getContextClassLoader();

        Class trogdorClient = Class.forName("org.apache.kafka.trogdor.coordinator.CoordinatorClient", true, loader);
        clientStarter = trogdorClient.getMethod("main", String[].class);
    }

    /**
     *
     * @param produceBench information about the benchmark
     */
    protected void runClient(String produceBench) throws Exception{
        // Running the benchmark
        clientStarter.invoke(null, (Object) new String[]{"createTask", "-t", "localhost:8889", "-i", "produce1", "--spec", produceBench});

        // Return the information about the benchmark
        while (!System.getProperty("TaskState").equals("DONE")) {
            clientStarter.invoke(null, (Object) new String[]{"showTask", "-t", "localhost:8889", "-i", "produce1"});
            Thread.sleep(100);
        }
    }
}

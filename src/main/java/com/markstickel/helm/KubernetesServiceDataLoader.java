package com.markstickel.helm;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.markstickel.helm.json.ObjectRoot;
import com.markstickel.helm.json.Port;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

public class KubernetesServiceDataLoader {

    private static final Logger logger = LoggerFactory.getLogger(KubernetesServiceDataLoader.class);

    private ObjectMapper objectMapper = new ObjectMapper();

    public ObjectRoot loadServiceData(String namespace) {
        String[] cmd = {"/bin/sh", "-c", "kubectl get services --output json --namespace " + namespace};
        logger.info("Running: {}", cmd[2]);
        CompletableFuture<ObjectRoot> future = CompletableFuture.supplyAsync(() -> {
            try {
                Process proc = Runtime.getRuntime().exec(cmd);
                Reader isr = new BufferedReader(new InputStreamReader(proc.getInputStream()));
                return objectMapper.readValue(isr, ObjectRoot.class);
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        });
        try {
            return future.get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    public Map<String, KubernetesService> loadServiceDataMap(String namespace) {
        return Arrays.stream(loadServiceData(namespace).getItems())
                .filter(i -> "NodePort".equals(i.getSpec().getType()))
                .filter(i -> i.getSpec().getSelector().getName() != null)
                .map(i -> {
                    KubernetesService kubernetesService = new KubernetesService();
                    kubernetesService.setServiceName(i.getSpec().getSelector().getName());
                    Map<String, Integer> ports = Arrays.stream(i.getSpec().getPorts())
                            .filter(p -> p.getNodePort() != null)
                            .collect(Collectors.toMap(Port::getName, p -> Integer.parseInt(p.getNodePort())));
                    kubernetesService.setNodePorts(ports);
                    return kubernetesService;
                }).collect(Collectors.toMap(KubernetesService::getServiceName, k -> k));
    }
}

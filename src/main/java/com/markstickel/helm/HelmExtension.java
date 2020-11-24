package com.markstickel.helm;

import com.google.common.base.Joiner;
import org.junit.jupiter.api.extension.AfterAllCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class HelmExtension implements BeforeAllCallback, AfterAllCallback {

    private static final Logger logger = LoggerFactory.getLogger(HelmExtension.class);

    private static final String helmGroup = "it" + LocalTime.now().format(DateTimeFormatter.ofPattern("hhmmss"));

    @Override
    public void beforeAll(ExtensionContext extensionContext) throws Exception {
        logger.info("helmInstall()");
        HelmIntegrationTest kitAnnotation = extensionContext.getRequiredTestClass().getAnnotation(HelmIntegrationTest.class);

        String namespace = kitAnnotation.namespace();
        String helmChartDir = kitAnnotation.helmChartDir();
        String serviceName = kitAnnotation.helmServiceName();
        String valuesOverride = kitAnnotation.helmValuesOverride();
        String readinessPath = kitAnnotation.readinessPath();
        String kubernetesNodeHost = kitAnnotation.kubernetesNodeHost();

        String[] depUpCmd = {"/bin/sh", "-c", "helm dep up " + helmChartDir + " --namespace " + namespace};
        runStatic(depUpCmd);
        String[] cmd = {"/bin/sh", "-c", "helm install " + (!StringUtils.isEmpty(valuesOverride) ? "-f " + valuesOverride : "") + " " + helmGroup + " " + helmChartDir + " --namespace " + namespace};
        runStatic(cmd);
        waitForKubernetesReady(serviceName, namespace);
        int servicePort = setTestPort(helmGroup, serviceName, namespace);
        waitForServiceReady(kubernetesNodeHost, servicePort, readinessPath);
    }

    @Override
    public void afterAll(ExtensionContext extensionContext) throws Exception {
        logger.info("helmDelete()");
        HelmIntegrationTest kitAnnotation = extensionContext.getRequiredTestClass().getAnnotation(HelmIntegrationTest.class);

        String namespace = kitAnnotation.namespace();

        String del = "helm delete " + helmGroup + " --namespace " + namespace;
        logger.info("Command to delete: " + del);
        String[] cmd = {"/bin/sh", "-c", del};
        runStatic(cmd);

    }

    private static void runStatic(String[] cmd) throws IOException, InterruptedException {
        Process proc = Runtime.getRuntime().exec(cmd);
        StreamGobbler errorGobbler = new
                StreamGobbler(proc.getErrorStream(), "ERROR");

        StreamGobbler outputGobbler = new
                StreamGobbler(proc.getInputStream(), "INFO");

        errorGobbler.start();
        outputGobbler.start();

        int exitCode = proc.waitFor();
        logger.debug("Command \"" + Joiner.on(" ").join(cmd) + "\" completed.");
        assertEquals(0, exitCode);
    }

    private static void waitForKubernetesReady(String serviceName, String namespace) {
        boolean ready;
        String kubectl = "kubectl get deployments --namespace " + namespace;
        String[] cmd = {"/bin/sh", "-c", kubectl};
        logger.info("Executing command: " + kubectl);
        while (true) {
            try {
                CompletableFuture<Boolean> future = CompletableFuture.supplyAsync(() -> {
                    try {
                        Process proc = Runtime.getRuntime().exec(cmd);
                        InputStream is = proc.getInputStream();
                        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
                        String line;
                        while ((line = reader.readLine()) != null) {
                            if (line.contains(serviceName) && line.contains("1/1")) {
                                logger.info("line: " + line);
                                return true;
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return false;
                });
                ready = future.get();
                if (ready) {
                    break;
                }
                logger.info("Not ready");
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
        logger.info("Kubernetes Ready");
    }

    private static void waitForServiceReady(String serviceHost, int servicePort, String readinessPath) {
        String curl = "curl -s -o /dev/null -w \"%{http_code}\" http://" + serviceHost + ":" + servicePort + readinessPath;
        String[] curlCmd = {"/bin/sh", "-c", curl};
        logger.info("Executing command: " + curl);

        Process proc = null;
        retry:
        while (true) {
            try {
                proc = Runtime.getRuntime().exec(curlCmd);
                InputStream is = proc.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(is));
                logger.info("Waiting for endpoint to return a value...");
                String line;
                while ((line = reader.readLine()) != null) {
                    logger.info("Read line: " + line);
                    if (line.contains("200")) {
                        break retry;
                    }
                }
                Thread.sleep(3000);
            } catch (IOException e) {
                logger.error(e.getMessage(), e);
            } catch (InterruptedException e) {
                // continue
            }
        }
        logger.info("Service Ready");
    }

    private static int setTestPort(String helmGroup, String serviceName, String namespace) throws ExecutionException, InterruptedException {
        String[] cmd = {"/bin/sh", "-c", "kubectl get service " + helmGroup + "-" + serviceName + " --output jsonpath='{.spec.ports[?(@.name==\"http\")].nodePort}' --namespace " + namespace};
        logger.info("Running: {}", cmd[2]);
        CompletableFuture<Integer> future = CompletableFuture.supplyAsync(() -> {
            try {
                Process proc = Runtime.getRuntime().exec(cmd);
                InputStream is = proc.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(is));
                String line;
                while ((line = reader.readLine()) != null) {
                    return Integer.parseInt(line);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return -1;
        });
        int servicePort = future.get();
        logger.info("Set service port to {}", servicePort);
        return servicePort;
    }

}

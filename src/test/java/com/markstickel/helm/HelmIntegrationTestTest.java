package com.markstickel.helm;

import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;

import static org.junit.jupiter.api.Assertions.assertTrue;

@HelmIntegrationTest(namespace = "helm-test-support",
        helmChartDir = "src/test/helm/kubernetes-test-support",
        helmServiceName = "kubernetes-test-support",
        kubernetesNodeHost = "192.168.99.106")
@ContextConfiguration(classes = HelmIntegrationTestTest.Config.class)
public class HelmIntegrationTestTest {

    @Test
    public void testExtensionContext() {
        assertTrue(true);
    }

    @Configuration
    static class Config {

    }

}

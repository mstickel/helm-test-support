package com.markstickel.helm;

import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@HelmIntegrationTest(namespace = "helm-test-support",
        helmServiceName = "kubernetes-test-support",
        helmChartDir = "src/test/helm/kubernetes-test-support",
        kubernetesNodeHost = "192.168.99.106")
@ContextConfiguration(classes = HelmIntegrationTestTest.Config.class)
public class HelmIntegrationTestTest {

    @HelmTestContext("serviceName")
    private static String serviceName;
    @HelmTestContext("kubernetesNodeHost")
    private static String kubernetesNodeHost;
    @HelmTestContext("servicePort")
    private static Integer servicePort;
    @HelmTestContext("serviceMap")
    private static Map<String, KubernetesService> serviceMap;

    @Test
    public void testExtensionContext() {
        assertEquals("kubernetes-test-support", serviceName);
        assertEquals("192.168.99.106", kubernetesNodeHost);
        assertTrue(servicePort > 0);
        assertNotNull(serviceMap);
        assertEquals(1, serviceMap.size());
        KubernetesService serviceDescription = serviceMap.get("kubernetes-test-support");
        assertNotNull(serviceDescription);
        assertEquals(31980, serviceDescription.getNodePorts().get("http"));
    }

    @Configuration
    static class Config {

    }

}

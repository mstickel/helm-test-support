package com.markstickel.helm;

import com.markstickel.helm.json.ObjectRoot;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class KubernetesServiceDataLoaderTest {

    private static final Logger logger = LoggerFactory.getLogger(KubernetesServiceDataLoaderTest.class);

    private KubernetesServiceDataLoader kubernetesServiceDataLoader;

    @BeforeEach
    public void setUp() {
        kubernetesServiceDataLoader = new KubernetesServiceDataLoader();
    }

    @Test
    public void testLoadServiceData() {
        String namespace = "msgboardit";
        ObjectRoot result = kubernetesServiceDataLoader.loadServiceData(namespace);
        assertNotNull(result);
        logger.info(result.toString());
    }

    @Test
    public void testLoadServiceDataMap() {
        String namespace = "msgboardit";
        Map<String, KubernetesService> result = kubernetesServiceDataLoader.loadServiceDataMap(namespace);
        assertNotNull(result);
        logger.info(result.toString());
    }
}

package com.markstickel.helm;

import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@ExtendWith(HelmExtension.class)
@SpringBootTest
@ActiveProfiles("test")
public @interface HelmIntegrationTest {

    String namespace() default "default";
    String helmChartDir(); // required
    String helmServiceName(); // required
    String helmValuesOverride() default "";
    String readinessPath() default "/"; // TODO detect from k8s service descriptor
    String kubernetesNodeHost() default "127.0.0.1"; // TODO detect from current k8s context
    boolean helmDeleteAfterTests() default true;
}

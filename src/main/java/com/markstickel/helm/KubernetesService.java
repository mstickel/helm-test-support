package com.markstickel.helm;

import java.util.Map;

public class KubernetesService {

    private String serviceName;
    private Map<String, Integer> nodePorts;

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public Map<String, Integer> getNodePorts() {
        return nodePorts;
    }

    public void setNodePorts(Map<String, Integer> nodePorts) {
        this.nodePorts = nodePorts;
    }

    @Override
    public String toString() {
        return "KubernetesService{" +
                "serviceName='" + serviceName + '\'' +
                ", nodePorts=" + nodePorts +
                '}';
    }
}

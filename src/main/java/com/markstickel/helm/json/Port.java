package com.markstickel.helm.json;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Port {

    private String name;
    private String nodePort;
    private String port;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNodePort() {
        return nodePort;
    }

    public void setNodePort(String nodePort) {
        this.nodePort = nodePort;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    @Override
    public String toString() {
        return "Port{" +
                "name='" + name + '\'' +
                ", nodePort='" + nodePort + '\'' +
                ", port='" + port + '\'' +
                '}';
    }
}

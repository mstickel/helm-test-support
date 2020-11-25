package com.markstickel.helm.json;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Arrays;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Spec {

    private String type;
    private Port[] ports;
    private Selector selector;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Port[] getPorts() {
        return ports;
    }

    public void setPorts(Port[] ports) {
        this.ports = ports;
    }

    public Selector getSelector() {
        return selector;
    }

    public void setSelector(Selector selector) {
        this.selector = selector;
    }

    @Override
    public String toString() {
        return "Spec{" +
                "type='" + type + '\'' +
                ", ports=" + Arrays.toString(ports) +
                ", selector=" + selector +
                '}';
    }
}

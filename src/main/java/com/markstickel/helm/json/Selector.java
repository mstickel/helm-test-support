package com.markstickel.helm.json;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Selector {

    @JsonProperty("app.kubernetes.io/instance")
    private String instance;
    @JsonProperty("app.kubernetes.io/name")
    private String name;

    public String getInstance() {
        return instance;
    }

    public void setInstance(String instance) {
        this.instance = instance;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Selector{" +
                "instance='" + instance + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}

package com.markstickel.helm.json;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Arrays;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ObjectRoot {

    private String apiVersion;
    private Item[] items;

    public String getApiVersion() {
        return apiVersion;
    }

    public void setApiVersion(String apiVersion) {
        this.apiVersion = apiVersion;
    }

    public Item[] getItems() {
        return items;
    }

    public void setItems(Item[] items) {
        this.items = items;
    }

    @Override
    public String toString() {
        return "ObjectRoot{" +
                "apiVersion='" + apiVersion + '\'' +
                ", items=" + Arrays.toString(items) +
                '}';
    }
}

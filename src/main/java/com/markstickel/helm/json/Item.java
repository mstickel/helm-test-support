package com.markstickel.helm.json;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Item {

    private String kind;
    private Spec spec;

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public Spec getSpec() {
        return spec;
    }

    public void setSpec(Spec spec) {
        this.spec = spec;
    }

    @Override
    public String toString() {
        return "Item{" +
                "kind='" + kind + '\'' +
                ", spec=" + spec +
                '}';
    }
}

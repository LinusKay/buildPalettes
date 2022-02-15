package com.libus.buildPalettes;

import java.util.List;

public class Palette {

    private String name;
    private String owner;
    private String privacy;
    private List<String> blocks;

    public Palette(String name, String owner, String privacy, List<String> blocks) {
        this.name = name;
        this.owner = owner;
        this.privacy = privacy;
        this.blocks = blocks;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getOwner() { return this.owner; }

    public void setOwner(String owner) { this.owner = owner; }


    public String getPrivacy() { return this.privacy; }

    public void setPrivacy(String privacy) { this.privacy = privacy; }


    public List<String> getBlocks() {
        return this.blocks;
    }

    public void setBlocks(List<String> blocks) {
        this.blocks = blocks;
    }
}

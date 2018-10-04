package com.songoda.epicvouchers;

public class References {

    private String prefix;

    public References() {
        prefix = EpicVouchers.getInstance().getLocale().getMessage("general.nametag.prefix") + " ";
    }

    public String getPrefix() {
        return this.prefix;
    }
}

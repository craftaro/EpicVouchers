package com.songoda.epicvouchers.voucher;

import java.util.*;

public class VoucherManager {

    private final Map<String, Voucher> registeredVouchers = new HashMap<>();

    public void addVoucher(String name, Voucher voucher) {
        registeredVouchers.put(name, voucher);
    }

    public Voucher getVoucher(String name) {
        for (Voucher voucher : registeredVouchers.values()) {
            if (voucher.getKey().equalsIgnoreCase(name.trim())) return voucher;
        }
        return null;
    }

    public List<Voucher> getVouchers() {
        return new ArrayList<>(registeredVouchers.values());
    }

}

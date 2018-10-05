package com.songoda.epicvouchers.voucher;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VoucherManager {

    private final Map<String, Voucher> registeredVouchers = new HashMap<>();

    public Voucher addVoucher(String name, Voucher voucher) {
        return registeredVouchers.put(name, voucher);
    }

    public Voucher removeVoucher(Voucher voucher) {
        return registeredVouchers.remove(voucher);
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

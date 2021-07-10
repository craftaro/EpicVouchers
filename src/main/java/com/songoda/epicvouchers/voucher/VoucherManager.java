package com.songoda.epicvouchers.voucher;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class VoucherManager {
    private final Map<String, Voucher> registeredVouchers = new HashMap<>();

    public Voucher addVoucher(Voucher voucher) {
        return registeredVouchers.put(voucher.getKey(), voucher);
    }

    public Voucher removeVoucher(Voucher voucher) {
        return registeredVouchers.remove(voucher);
    }

    public Voucher getVoucher(String key) {
        return registeredVouchers.get(key);
    }

    public Collection<Voucher> getVouchers() {
        return registeredVouchers.values();
    }

    public void clearVouchers() {
        registeredVouchers.clear();
    }
}

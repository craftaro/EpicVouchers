package com.craftaro.epicvouchers.voucher;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class VoucherManager {
    private final Map<String, Voucher> registeredVouchers = new HashMap<>();

    public Voucher addVoucher(Voucher voucher) {
        return this.registeredVouchers.put(voucher.getKey(), voucher);
    }

    public Voucher removeVoucher(Voucher voucher) {
        return this.registeredVouchers.remove(voucher);
    }

    public Voucher getVoucher(String key) {
        return this.registeredVouchers.get(key);
    }

    public Collection<Voucher> getVouchers() {
        return this.registeredVouchers.values();
    }

    public void clearVouchers() {
        this.registeredVouchers.clear();
    }
}

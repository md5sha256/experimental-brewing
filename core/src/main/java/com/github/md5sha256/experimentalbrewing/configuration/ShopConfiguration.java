package com.github.md5sha256.experimentalbrewing.configuration;

import com.github.md5sha256.experiementalbrewing.api.drugs.IDrugComponent;
import org.jetbrains.annotations.NotNull;

public interface ShopConfiguration {

    double unitPrice(@NotNull IDrugComponent drugComponent);

    default double price(@NotNull IDrugComponent drugComponent, int amount) {
        return unitPrice(drugComponent) * amount;
    }

}

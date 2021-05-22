package com.github.md5sha256.experimentalbrewing.configuration;

import com.github.md5sha256.experiementalbrewing.api.drugs.DrugMeta;
import com.github.md5sha256.experiementalbrewing.api.drugs.IDrug;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

public interface DrugConfiguration {

    @NotNull Set<IDrug> drugs();

    void drugs(@NotNull Collection<IDrug> drugs);

    @NotNull Map<IDrug, @NotNull DrugMeta> drugMeta();

    void drugMeta(@NotNull Map<IDrug, @NotNull DrugMeta> metaMap);

}

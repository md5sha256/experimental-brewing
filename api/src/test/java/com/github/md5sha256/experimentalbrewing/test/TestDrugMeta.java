package com.github.md5sha256.experimentalbrewing.test;

import com.github.md5sha256.experiementalbrewing.api.drugs.DrugMeta;
import com.github.md5sha256.experiementalbrewing.api.drugs.impl.DrugMetaBuilder;
import org.bukkit.potion.PotionEffect;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.concurrent.TimeUnit;

public class TestDrugMeta {

    @Test
    public void testMetaKey() {
        Assertions.assertEquals("DrugMeta", DrugMeta.KEY.name());
        Assertions.assertEquals(DrugMeta.class, DrugMeta.KEY.type().getType());
    }

    @Test
    public void testMetaSimilarity() {
        final DrugMetaBuilder builder = DrugMeta.builder()
                                                .enabled(false)
                                                .overdoseThreshold(10)
                                                .slurDurationMillis(1000);
        final DrugMeta meta = builder.build();
        Assertions.assertTrue(meta.isSimilar(meta));
    }

    @Test
    public void testMetaCloning() {
        final DrugMetaBuilder builder = DrugMeta.builder()
                                                .enabled(false)
                                                .overdoseThreshold(10)
                                                .slurDurationMillis(1000);
        final DrugMeta meta = builder.build();
        final DrugMetaBuilder copy = new DrugMetaBuilder(builder);
        Assertions.assertTrue(meta.isSimilar(copy.build()));
        Assertions.assertTrue(meta.isSimilar(meta.toBuilder().build()));
    }

    @Test
    public void testMetaBuilderValidation() {
        final DrugMetaBuilder builder = DrugMeta.builder();
        Assertions.assertThrows(NullPointerException.class,
                                () -> builder.effects((Collection<PotionEffect>) null));
        Assertions.assertThrows(NullPointerException.class,
                                () -> builder.effects((PotionEffect[]) null));
        final DrugMetaBuilder valid = builder.enabled(false)
                                             .overdoseThreshold(10)
                                             .slurDurationMillis(1000);
        try {
            valid.build();
        } catch (Exception ex) {
            Assertions.fail(ex);
            return;
        }
        final DrugMetaBuilder invalidOverdose = new DrugMetaBuilder(valid).overdoseThreshold(0);
        Assertions.assertThrows(IllegalArgumentException.class, invalidOverdose::build);
        final DrugMetaBuilder invalidSlurDuration = new DrugMetaBuilder(valid)
                .slurDurationMillis(-1);
        Assertions.assertThrows(IllegalArgumentException.class, invalidSlurDuration::build);
    }

    @Test
    public void testMetaGetters() {
        final DrugMeta meta = DrugMeta.builder()
                                      .enabled(false)
                                      .overdoseThreshold(10)
                                      .slurDurationMillis(1000)
                                      .build();
        Assertions.assertFalse(meta.drugEnabled());
        Assertions.assertEquals(10, meta.overdoseThreshold());
        Assertions.assertEquals(1000, meta.slurDurationMillis());
        Assertions.assertEquals(1, meta.slurDuration(TimeUnit.SECONDS));
        Assertions.assertTrue(meta.effects().isEmpty());
        Assertions.assertFalse(meta.effect().isPresent());
    }

}

package com.github.md5sha256.experimentalbrewing.implementation;

import com.github.md5sha256.experiementalbrewing.api.slur.ISlurEffect;
import com.github.md5sha256.experiementalbrewing.api.slur.SlurEffectState;
import com.github.md5sha256.experiementalbrewing.api.util.AbstractSlurHandler;
import com.github.md5sha256.experimentalbrewing.util.Utils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

@SuppressWarnings("deprecation")
public final class SpigotSlurHandler extends AbstractSlurHandler implements Listener {

    public SpigotSlurHandler(@NotNull Plugin plugin, long intervalTicks, @NotNull SlurEffectState slurEffectState) {
        super(plugin, intervalTicks, slurEffectState);
    }

    public static String convertToFormat(final Player p, final String msg) {
        final String str = p.getDisplayName();
        // FIXME slur format
        final String rawmsg = "slur format";
        final String formatted = rawmsg.replace("{PLAYER}", str).replace("{MESSAGE}", msg);
        return Utils.legacyColorize(formatted);
    }

    @EventHandler
    public void onAsyncPlayerChat(@NotNull AsyncPlayerChatEvent event) {
        final Player player = event.getPlayer();
        final Optional<ISlurEffect> optional = this.slurEffectState.currentSlurEffect(player.getUniqueId());
        if (!optional.isPresent()) {
            return;
        }
        final String message = convertToFormat(player, event.getMessage());
        final Component component = LegacyComponentSerializer.legacySection().deserialize(Utils.legacyColorize(message));
        final ISlurEffect effect = optional.get();
        final Component formatted = effect.formatMessage(player, component);
        final String legacyText = LegacyComponentSerializer.legacySection().serialize(formatted);
        event.setMessage(legacyText);
        event.setFormat("");
    }

}

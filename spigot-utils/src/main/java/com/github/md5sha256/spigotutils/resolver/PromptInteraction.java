package com.github.md5sha256.spigotutils.resolver;

import org.bukkit.ChatColor;
import org.bukkit.conversations.ConversationContext;

import java.util.List;

public abstract class PromptInteraction {

    private final String[] exitKeywords;

    public PromptInteraction(final String... exitKeywords) {
        this.exitKeywords = exitKeywords == null ? new String[0] : exitKeywords;

    }

    public abstract List<String> handleReplacements(final List<String> toReplace, final ConversationContext ctx, final String promptInput);

    public boolean exitConditionReached(final ConversationContext ctx, final String promptInput) {
        if (promptInput == null) {
            return true;
        }
        for (String s : exitKeywords) {
            if (s == null) {
                continue;
            }
            if (s.equalsIgnoreCase(ChatColor.stripColor(promptInput))) {
                return true;
            }
        }
        return false;
    }

}

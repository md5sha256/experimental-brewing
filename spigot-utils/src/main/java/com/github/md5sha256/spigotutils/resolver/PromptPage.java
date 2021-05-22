package com.github.md5sha256.spigotutils.resolver;

public abstract class PromptPage extends PromptInteraction{

    public PromptPage(String... exitKeywords) {
        super(exitKeywords);
    }

    public abstract PromptPage getNextPage();


}

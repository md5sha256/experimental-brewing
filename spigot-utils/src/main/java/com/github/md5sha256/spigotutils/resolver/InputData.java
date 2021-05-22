package com.github.md5sha256.spigotutils.resolver;

import java.util.List;

public class InputData<T> {

    private List<String> rawInput;
    private List<String> display;

    InputData(final Resolver<T> resolver, List<String> notes) {
    }

    public List<String> getRawInput() {
        return rawInput;
    }
}

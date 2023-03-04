package com.example.wbtestapi.client;

class   FixedUri extends Uri {

    private final String uri;

    FixedUri(String uri) {
        this.uri = uri;
    }

    @Override
    public String toString() {
        return uri;
    }
}

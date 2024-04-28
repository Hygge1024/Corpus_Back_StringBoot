package com.lt.domain;

import java.util.List;

public class CorpusRequest {
    private List<String> keywords;

    public CorpusRequest() {
    }

    public List<String> getKeywords() {
        return keywords;
    }

    public void setKeywords(List<String> keywords) {
        this.keywords = keywords;
    }
}

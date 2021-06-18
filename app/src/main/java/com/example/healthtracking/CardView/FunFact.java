package com.example.healthtracking.CardView;

public class FunFact {
    private String title;
    private String quote;

    public FunFact(String title, String quote){
        this.quote = quote;
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getQuote() {
        return quote;
    }

    public void setQuote(String quote) {
        this.quote = quote;
    }
}

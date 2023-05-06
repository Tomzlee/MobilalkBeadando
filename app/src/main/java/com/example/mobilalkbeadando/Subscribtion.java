package com.example.mobilalkbeadando;

public class Subscribtion {

    String kurzus;
    String email;

    public Subscribtion() {
    }

    public Subscribtion(String kurzus, String email) {
        this.kurzus = kurzus;
        this.email = email;
    }

    public String getKurzus() {
        return kurzus;
    }

    public void setKurzus(String kurzus) {
        this.kurzus = kurzus;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}

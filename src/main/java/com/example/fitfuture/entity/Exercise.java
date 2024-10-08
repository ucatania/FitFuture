package com.example.fitfuture.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "exercises")
public class Exercise {

    @Id
    private String id;
    private String nome;
    private String gruppoMuscolare;

    // Costruttori, getters e setters
    public Exercise() {}

    public Exercise(String nome, String gruppoMuscolare) {
        this.nome = nome;
        this.gruppoMuscolare = gruppoMuscolare;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getGruppoMuscolare() {
        return gruppoMuscolare;
    }

    public void setGruppoMuscolare(String gruppoMuscolare) {
        this.gruppoMuscolare = gruppoMuscolare;
    }
}


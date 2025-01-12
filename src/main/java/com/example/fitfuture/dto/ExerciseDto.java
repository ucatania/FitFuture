package com.example.fitfuture.dto;

public class ExerciseDto {
    private String nome;
    private String gruppoMuscolare;

    public ExerciseDto(String nome, String gruppoMuscolare) {
        this.nome = nome;
        this.gruppoMuscolare = gruppoMuscolare;
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

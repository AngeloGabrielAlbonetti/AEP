package com.example.aep;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("OPERADOR")
public class Operador extends Usuario{

    protected Operador(){
    }
    public Operador(String nome, String email) {
        super(nome, email);
    }
}

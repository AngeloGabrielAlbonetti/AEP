package com.example.aep;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;


@Entity
@DiscriminatorValue("ADMINISTRADOR")
public class Administrador extends Usuario{
    protected Administrador(){}

    public Administrador(String nome, String email) {
        super(nome, email);
    }


}

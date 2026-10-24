package com.example.aep;

import jakarta.persistence.*;

@Entity
@Table(name = "lote")
public class Lote {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_estabelecimento", nullable = false)
    private Estabelecimento estabelecimento;
}


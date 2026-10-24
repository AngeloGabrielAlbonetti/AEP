package com.example.aep;
import jakarta.persistence.*;

import java.util.ArrayList;

@Entity
@Table(name = "estabelecimento")
public class Estabelecimento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @ManyToOne(optional = false)
    @JoinColumn(name = "id_usuario", nullable = false)
    private Administrador administrador;
    @Column(name = "nome_estabelecimento", nullable = false, length = 200)
    private String nome;
    @Column(length = 200)
    private String codigo;
    @OneToMany(mappedBy = "estabelecimento", cascade = CascadeType.ALL, orphanRemoval = true)
    ArrayList<Lote>lotes = new ArrayList<>();
    protected Estabelecimento() {
    }
    public Estabelecimento(Administrador administrador, String nome, String codigo) {
        this.administrador = administrador;
        this.nome = nome;
        this.codigo = codigo;
    }

    public Integer getId() {
        return id;
    }

    public Administrador getAdministrador() {
        return administrador;
    }

    public String getNome() {
        return nome;
    }

    public String getCodigo() {
        return codigo;
    }

    public ArrayList<Lote> getLotes() {
        return lotes;
    }

}
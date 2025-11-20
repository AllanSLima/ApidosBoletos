package br.com.universidade.apiboletos.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "boletos_suspeitos")
public class BoletoSuspeito {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "ra_aluno", nullable = false)
    private String raAluno;

    @Column(name = "linha_digitavel", nullable = false, unique = true)
    private String linhaDigitavel;

    @Column(name = "data_tentativa", nullable = false)
    private LocalDateTime dataTentativa;

    // Getters e setters

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getRaAluno() { return raAluno; }
    public void setRaAluno(String raAluno) { this.raAluno = raAluno; }

    public String getLinhaDigitavel() { return linhaDigitavel; }
    public void setLinhaDigitavel(String linhaDigitavel) { this.linhaDigitavel = linhaDigitavel; }

    public LocalDateTime getDataTentativa() { return dataTentativa; }
    public void setDataTentativa(LocalDateTime dataTentativa) { this.dataTentativa = dataTentativa; }
}

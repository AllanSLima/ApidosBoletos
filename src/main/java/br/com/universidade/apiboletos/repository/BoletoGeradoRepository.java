package br.com.universidade.apiboletos.repository;

import br.com.universidade.apiboletos.model.BoletoGerado;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BoletoGeradoRepository extends JpaRepository<BoletoGerado, Long> {

    Optional<BoletoGerado> findByLinhaDigitavel(String linhaDigitavel);

    List<BoletoGerado> findByRaAluno(String raAluno);

}

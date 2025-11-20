package br.com.universidade.apiboletos.repository;

import br.com.universidade.apiboletos.model.BoletoSuspeito;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BoletoSuspeitoRepository extends JpaRepository<BoletoSuspeito, Long> {
    Optional<BoletoSuspeito> findByLinhaDigitavel(String linhaDigitavel);
}

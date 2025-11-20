package br.com.universidade.apiboletos.service;

import br.com.universidade.apiboletos.model.BoletoGerado;
import br.com.universidade.apiboletos.model.BoletoSuspeito;
import br.com.universidade.apiboletos.repository.BoletoGeradoRepository;
import br.com.universidade.apiboletos.repository.BoletoSuspeitoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class BoletoGeradoService {

    @Autowired
    private BoletoGeradoRepository boletoGeradoRepository;

    @Autowired
    private BoletoSuspeitoRepository boletoSuspeitoRepository;

    public Optional<BoletoGerado> buscarPorLinhaDigitavel(String linhaDigitavel) {
        return boletoGeradoRepository.findByLinhaDigitavel(linhaDigitavel);
    }

    public List<BoletoGerado> buscarPorRaAluno(String raAluno) {
        return boletoGeradoRepository.findByRaAluno(raAluno);
    }

    public BoletoGerado salvarBoleto(BoletoGerado boleto) {
        return boletoGeradoRepository.save(boleto);
    }

    public boolean existeLinhaDigitavel(String linhaDigitavel) {
        return boletoGeradoRepository.findByLinhaDigitavel(linhaDigitavel).isPresent();
    }

    public Optional<BoletoGerado> atualizarStatus(String linhaDigitavel, String novoStatus) {
        Optional<BoletoGerado> boletoOpt = boletoGeradoRepository.findByLinhaDigitavel(linhaDigitavel);
        if (boletoOpt.isPresent()) {
            BoletoGerado boleto = boletoOpt.get();
            boleto.setStatus(novoStatus);
            return Optional.of(boletoGeradoRepository.save(boleto));
        }
        return Optional.empty();
    }

    // Novo método para registrar linha digitavel suspeita
    public void registrarBoletoSuspeito(String raAluno, String linhaDigitavel) {
        // só registra se não existir em boletos_gerados nem em boletos_suspeitos
        boolean existeGerado = boletoGeradoRepository.findByLinhaDigitavel(linhaDigitavel).isPresent();
        boolean existeSuspeito = boletoSuspeitoRepository.findByLinhaDigitavel(linhaDigitavel).isPresent();

        if (!existeGerado && !existeSuspeito) {
            BoletoSuspeito suspeito = new BoletoSuspeito();
            suspeito.setRaAluno(raAluno);
            suspeito.setLinhaDigitavel(linhaDigitavel);
            suspeito.setDataTentativa(LocalDateTime.now());
            boletoSuspeitoRepository.save(suspeito);
        }
    }
}

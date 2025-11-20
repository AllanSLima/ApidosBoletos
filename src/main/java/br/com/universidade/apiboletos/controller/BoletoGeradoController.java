package br.com.universidade.apiboletos.controller;

import br.com.universidade.apiboletos.model.BoletoGerado;
import br.com.universidade.apiboletos.repository.BoletoGeradoRepository;
import br.com.universidade.apiboletos.service.BoletoGeradoService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/boletos")
public class BoletoGeradoController {

    @Autowired
    private BoletoGeradoService boletoGeradoService;

    @PostMapping
    public ResponseEntity<?> salvarBoleto(
            @RequestBody BoletoGerado boleto,
            @RequestHeader(value = "X-API-KEY", required = false) String apiKey) {

        final String API_KEY_SECRETA = "tokenSuperSecreto123";

        if (apiKey == null || !apiKey.equals(API_KEY_SECRETA)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("API key inválida");
        }

        if (boletoGeradoService.existeLinhaDigitavel(boleto.getLinhaDigitavel())) {
            return ResponseEntity.badRequest().body("Boletos com essa linha digitável já existem");
        }
        BoletoGerado salvo = boletoGeradoService.salvarBoleto(boleto);
        return ResponseEntity.status(201).body(salvo);
    }

    @GetMapping("/linha/{linhaDigitavel}")
    public ResponseEntity<BoletoGerado> consultarPorLinhaDigitavel(
            @PathVariable String linhaDigitavel,
            @RequestHeader(value = "raAluno", required = false) String raAluno) {

        Optional<BoletoGerado> boletoOpt = boletoGeradoService.buscarPorLinhaDigitavel(linhaDigitavel);

        if (boletoOpt.isPresent()) {
            return ResponseEntity.ok(boletoOpt.get());
        } else {
            // Se informar RA do aluno via header, registra a tentativa suspeita
            if (raAluno != null && !raAluno.isEmpty()) {
                boletoGeradoService.registrarBoletoSuspeito(raAluno, linhaDigitavel);
            }
            // Retorna 404 normalmente
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/aluno/{raAluno}")
    public ResponseEntity<List<BoletoGerado>> consultarPorRaAluno(@PathVariable String raAluno) {
        List<BoletoGerado> boletos = boletoGeradoService.buscarPorRaAluno(raAluno);
        return ResponseEntity.ok(boletos);
    }

    @PutMapping("/status/{linhaDigitavel}")
    public ResponseEntity<?> atualizarStatus(
            @PathVariable String linhaDigitavel,
            @RequestParam String status,
            @RequestHeader(value = "X-API-KEY", required = false) String apiKey) {

        final String API_KEY_SECRETA = "tokenSuperSecreto123"; // Defina sua chave secreta aqui

        if (apiKey == null || !apiKey.equals(API_KEY_SECRETA)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("API key inválida");
        }

        Optional<BoletoGerado> boletoAtualizado = boletoGeradoService.atualizarStatus(linhaDigitavel, status);
        return boletoAtualizado.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

}

package br.com.universidade.apiboletos.controller;

import br.com.universidade.apiboletos.model.BoletoSuspeito;
import br.com.universidade.apiboletos.service.BoletoGeradoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/boletos/suspeitos")
public class BoletoSuspeitoController {

    @Autowired
    private BoletoGeradoService boletoGeradoService;

    @PostMapping
    public ResponseEntity<?> registrarSuspeito(@RequestBody BoletoSuspeito boletoSuspeito) {
        // Validações básicas podem ser adicionadas aqui

        boletoGeradoService.registrarBoletoSuspeito(
                boletoSuspeito.getRaAluno(),
                boletoSuspeito.getLinhaDigitavel()
        );

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}

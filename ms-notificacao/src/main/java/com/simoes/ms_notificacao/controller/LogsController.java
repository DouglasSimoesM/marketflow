package com.simoes.ms_notificacao.controller;

import com.simoes.ms_notificacao.entity.LogsPedido;
import com.simoes.ms_notificacao.service.LogsService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/logs")
@Tag(name = "Notificações", description = "Endpoint Para gerenciar Logs")
public class LogsController {

    private final LogsService logsService;

    @Autowired
    public LogsController(LogsService logsService) {
        this.logsService = logsService;
    }

    @GetMapping
    public ResponseEntity<List<LogsPedido>> buscarAllLogs(){
        return ResponseEntity.ok(logsService.todosLogs());
    }

    @GetMapping("/{id}")
    public ResponseEntity <List<LogsPedido>> buscarPorId(@RequestParam Long idUsuario){
        return ResponseEntity.ok(logsService.buscarLogsPorId(idUsuario));
    }
}

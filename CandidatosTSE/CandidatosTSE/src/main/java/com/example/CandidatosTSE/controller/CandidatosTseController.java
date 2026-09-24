package com.example.CandidatosTSE.controller;

import java.util.List;

import org.springframework.stereotype.Controller;

import com.example.CandidatosTSE.model.Candidato;
import com.example.CandidatosTSE.service.CandidatosTseService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.ui.Model;

import java.util.List;


@Controller 
public class CandidatosTseController {
    //atributo
    private final CandidatosTseService candidatosTseService;

    //construtor
    public CandidatosTseController(CandidatosTseService candidatosTseService) {
        this.candidatosTseService = candidatosTseService;
    }

    @GetMapping("/")
    public String index(@RequestParam (required = false) String cargo, @RequestParam (required = false) String partido,@RequestParam (required = false) String texto, Model model) {
        //Lista de candidatos (filtros vazios são ignorados pelo service)
        List<Candidato> candidatos = candidatosTseService.filtrar(cargo, partido, texto);
        //Total candidatos encontrados
        int candidatosEncontrados = candidatos.size();
        //Filtros
        List<String> cargos = candidatosTseService.listarCargos();
        List<String> partidos = candidatosTseService.listarPartidos();
        //


        model.addAttribute("candidatos", candidatos);
        model.addAttribute("totalEncontrado", candidatosEncontrados);
        model.addAttribute("cargo", cargos);
        model.addAttribute("partido", partidos);
        model.addAttribute("cargoSelecionado", cargo);

        return "index";
    }

    
}

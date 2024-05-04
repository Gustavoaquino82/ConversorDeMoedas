package com.desafioalura.modelos;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class GeradorArquivos {
    private final String caminhoArquivo;

    public GeradorArquivos() {
        this.caminhoArquivo = "historico/historico.json";
    }

    public void salvarJson(double valorInserido, String moedaOrigem, double valorConvertido, String moedaDestino, double taxaConversao) throws IOException {
        DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        LocalDateTime dataAtual = LocalDateTime.now();
        System.out.println(dataAtual.format(formato));

        Dados dados = new Dados();
        dados.setData(dataAtual.format(formato));
        dados.setValorInserido(valorInserido);
        dados.setMoedaOrigem(moedaOrigem);
        dados.setValorConvertido(valorConvertido);
        dados.setMoedaDestino(moedaDestino);
        dados.setTaxaConversao(taxaConversao);

        List<Dados> historicoConversoes = lerHistoricoJson();
        historicoConversoes.add(dados);

        escreverHistoricoJson(historicoConversoes);

        System.out.println("Salvo em histórico, será atualizado assim que finalizar o programa.");
    }

    private List<Dados> lerHistoricoJson() throws IOException {
        Gson gson = new Gson();
        File arquivo = new File(caminhoArquivo);

        if (!arquivo.exists()) {
            return new ArrayList<>();
        }

        try (Reader reader = new FileReader(caminhoArquivo)) {
            Type tipoLista = new TypeToken<List<Dados>>(){}.getType();
            return gson.fromJson(reader, tipoLista);
        }
    }

    private void escreverHistoricoJson(List<Dados> historicoConversoes) throws IOException {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        try (FileWriter escrita = new FileWriter(caminhoArquivo)) {
            gson.toJson(historicoConversoes, escrita);
        }
    }

    public class Dados {
        private String data;
        private double valorInserido;
        private String moedaOrigem;
        private double valorConvertido;
        private String moedaDestino;
        private double taxaConversao;

        public String getData() {
            return data;
        }

        public void setData(String data) {
            this.data = data;
        }

        public double getValorInserido() {
            return valorInserido;
        }

        public void setValorInserido(double valorInserido) {
            this.valorInserido = valorInserido;
        }

        public String getMoedaOrigem() {
            return moedaOrigem;
        }

        public void setMoedaOrigem(String moedaOrigem) {
            this.moedaOrigem = moedaOrigem;
        }

        public double getValorConvertido() {
            return valorConvertido;
        }

        public void setValorConvertido(double valorConvertido) {
            this.valorConvertido = valorConvertido;
        }

        public String getMoedaDestino() {
            return moedaDestino;
        }

        public void setMoedaDestino(String moedaDestino) {
            this.moedaDestino = moedaDestino;
        }

        public double getTaxaConversao() {
            return taxaConversao;
        }

        public void setTaxaConversao(double taxaConversao) {
            this.taxaConversao = taxaConversao;
        }
    }
}

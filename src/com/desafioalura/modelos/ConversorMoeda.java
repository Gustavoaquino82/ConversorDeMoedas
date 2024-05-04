package com.desafioalura.modelos;

import com.google.gson.Gson;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class ConversorMoeda {
    public double[] converterMoeda(String valor1, String valor2, double moeda) throws IOException, InterruptedException {
        String apiKey = "3952f7226cf55030a959fa13";
        URI endereco = URI.create("https://v6.exchangerate-api.com/v6/" +
                apiKey + "/pair/" + valor1 + "/" + valor2 + "?amount=" + moeda);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(endereco)
                .build();

        try {
            HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                MoedaResponse moedaResponse = new Gson().fromJson(response.body(), MoedaResponse.class);
                double taxaCambio = moedaResponse.conversion_rate();
                double valorConvertido = moeda * taxaCambio;
                return new double[]{valorConvertido, taxaCambio};
            } else {
                System.out.println("Erro ao obter resposta da API: " + response.statusCode());
                return null;
            }
        } catch (IOException | InterruptedException e) {
            System.out.println("Erro ao processar a requisição: " + e.getMessage());
            return null;
        }
    }

    public static class MoedaResponse {
        private double conversion_rate;

        public double conversion_rate() {
            return conversion_rate;
        }
    }

    public String getNomeMoeda(String codigoMoeda) {
        switch (codigoMoeda) {
            case "USD":
                return "Dólar";
            case "ARS":
                return "Peso Argentino";
            case "BRL":
                return "Real Brasileiro";
            case "COP":
                return "Peso Colombiano";
            default:
                return "Moeda Desconhecida";
        }
    }

    public String formatarTaxaCambio(double taxaCambio, String moedaOrigem) {
        String nomeMoedaOrigem = getNomeMoeda(moedaOrigem);
        return String.format("%.4f %s equivale a 1 %s", 1 / taxaCambio, nomeMoedaOrigem, nomeMoedaOrigem);
    }
}


package com.desafioalura.principal;
import com.desafioalura.modelos.GeradorArquivos;
import com.google.gson.Gson;
import com.desafioalura.modelos.ConversorMoeda;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Principal {
    public static void main(String[] args) throws IOException {
        Scanner leitura = new Scanner(System.in);
        ConversorMoeda conversor = new ConversorMoeda();
        GeradorArquivos gerador = new GeradorArquivos();


        String menu = """
                *********************************************************************
                Seja bem vindo/a ao Conversor de Moedas =]
                                
                1) Dólar =>> Peso Argentino
                2) Peso Argentino =>> Dólar
                3) Dólar =>> Real Brasileiro
                4) Real Brasileiro =>> Dólar
                5) Dólar =>> Peso Colombiano
                6) Peso Colombiano =>> Dólar
                7) Mostrar histórico de busca
                8) Sair
                Escolha uma opção válida:
                *********************************************************************
                """;

        boolean continuar = true;
        while (continuar) {
            System.out.println(menu);
            int escolha = leitura.nextInt();
            if (escolha >= 1 && escolha <= 8) {
                try {
                    if (escolha != 7 && escolha != 8) {
                        System.out.println("Digite um valor para converter: ");
                        while (!leitura.hasNextDouble()) {
                            System.out.println("Valor Inválido. Digite um número: ");
                            leitura.next();
                        }
                        double valor = leitura.nextDouble();
                        String moedaOrigem = "";
                        String moedaDestino = "";

                        switch (escolha) {
                            case 1:
                                moedaOrigem = "USD";
                                moedaDestino = "ARS";
                                break;
                            case 2:
                                moedaOrigem = "ARS";
                                moedaDestino = "USD";
                                break;
                            case 3:
                                moedaOrigem = "USD";
                                moedaDestino = "BRL";
                                break;
                            case 4:
                                moedaOrigem = "BRL";
                                moedaDestino = "USD";
                                break;
                            case 5:
                                moedaOrigem = "USD";
                                moedaDestino = "COP";
                                break;
                            case 6:
                                moedaOrigem = "COP";
                                moedaDestino = "USD";

                        }
                        double[] resultado = conversor.converterMoeda(moedaOrigem, moedaDestino, valor);
                        if (resultado != null) {
                            double valorConvertido = resultado[0];
                            double taxaCambio = resultado[1];

                            System.out.printf("O valor %.2f %s corresponde a %.2f %s (Taxa de câmbio: 1 %s é igual a %.4f %s)%n",
                                    valor, moedaOrigem, valorConvertido, moedaDestino, moedaOrigem, taxaCambio, moedaDestino);
                            gerador.salvarJson(valor, moedaOrigem, valorConvertido, moedaDestino, taxaCambio);

                        } else {
                            System.out.println("Erro ao converter moeda. Verifique os valores informados.");
                        }
                    } else if (escolha == 7) {
                        exibirHistorico();
                        continuar = false;
                    } else {
                        System.out.println("Saindo...");
                        continuar = false;
                        return;
                    }
                }catch (InterruptedException e) {
                        System.out.println("Erro de interrupção: " + e.getMessage());
                } catch (InputMismatchException e) {
                    System.out.println("Por favor insira um valor válido!");
                }
            }else {
                System.out.println("Opção inválida! Saindo...");
                continuar = false;
            }
        }
    }

    private static void exibirHistorico() throws IOException {
        File diretorio = new File("historico");
        File[] arquivos = diretorio.listFiles((dir, nome) -> nome.endsWith(".json"));

        if (arquivos != null && arquivos.length > 0) {
            for (File arquivo : arquivos) {
                try (FileReader reader = new FileReader(arquivo)) {
                    GeradorArquivos.Dados dados = new Gson().fromJson(reader, GeradorArquivos.Dados.class);
                    System.out.printf("Data: %s, valor: %.5f %s => %s%n", dados.getData(), dados.getValorInserido(), dados.getMoedaOrigem(), dados.getMoedaDestino());
                }
            }
        } else {
            System.out.println("Não há histórico de buscas.");
        }
    }
}
package com.talysonsilva.tabelafipe.view;

import com.talysonsilva.tabelafipe.model.Dados;
import com.talysonsilva.tabelafipe.model.Modelos;
import com.talysonsilva.tabelafipe.model.Veiculo;
import com.talysonsilva.tabelafipe.service.ConectApi;
import com.talysonsilva.tabelafipe.service.ConverteDados;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Tela {
    private Scanner ler = new Scanner(System.in);
    private ConectApi api = new ConectApi();
    private ConverteDados converteDados = new ConverteDados ();
    private  final String URL_BASE = "https://parallelum.com.br/fipe/api/v1/";

    public  void exibirMenu(){
        System.out.println("""
                Digite uma das seguintes opção:
                Carros
                Motos
                Caminhão
                """
        );
        //TODO separar em uma nova função
        String tipoVeiculo = ler.nextLine().toLowerCase().strip();

        //TODO Função
        String endereco;
        if (tipoVeiculo.contains("carr")){
             endereco = URL_BASE + "carros/marcas";
        }else if (tipoVeiculo.contains("mot")){
            endereco = URL_BASE + "motos/marcas";
        }else{
            endereco = URL_BASE + "caminhoes/marcas";
        }
        //TODO Função
        String json = api.obterDados(endereco);

        //TODO separa em uma função
        var marcas = converteDados.obterLista(json, Dados.class);
        marcas.stream()
                .sorted(Comparator.comparing(Dados::codigo))
                .forEach(System.out::println);

        //TODO separa em uma função
        System.out.println("\nDigite o codigo do modelo: ");
        String codigoMarca = ler.nextLine();

        //TODO separa em uma função
        endereco = endereco + "/" + codigoMarca + "/modelos";
        json = api.obterDados(endereco);
        var modeloLista = converteDados.obterDados(json, Modelos.class);

        //TODO separa em uma função
        System.out.println("\nModelos dessa marca: ");
        modeloLista.modelos().stream()
                .sorted(Comparator.comparing(Dados::codigo))
                .forEach(System.out::println);

        //TODO separa em uma função
        System.out.println("\nDigite um trecho do nome do veiculo a ser buscado: ");
        var veiculo = ler.nextLine();

        //TODO separa em uma função
        List<Dados> modelosFiltrados = modeloLista.modelos().stream()
                .filter(a ->a.nome().toLowerCase() .contains(veiculo))
                .collect(Collectors.toList());
        //TODO separa em uma função
        System.out.println("\nModelos filtrados");
        modelosFiltrados.forEach(System.out::println);

        //TODO separa em uma função
        System.out.println("\nDigite por favor o código do modelo: ");
        var codigoModelo = ler.nextLine();

        endereco = endereco +"/" +codigoModelo+ "/anos";
        json = api.obterDados(endereco);
        List<Dados> anos = converteDados.obterLista(json, Dados.class);

        //TODO separa em uma função
        List<Veiculo> veiculos = new ArrayList<>();

        for (int i = 0; i < anos.size() ; i++) {
            var enderecoAnos = endereco + "/" + anos.get(i).codigo();
            json = api.obterDados(enderecoAnos);
            Veiculo veiculoAno = converteDados.obterDados(json, Veiculo.class);
            veiculos.add(veiculoAno);
        }

        //TODO separa em uma função
        System.out.println("\nTodos os veiculos filtrados com avaliaçõees por ano: ");
        veiculos.forEach(System.out::println);

    }
}

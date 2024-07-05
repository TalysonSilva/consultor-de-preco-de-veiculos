package com.talysonsilva.tabelafipe.view;

import com.talysonsilva.tabelafipe.model.Data;
import com.talysonsilva.tabelafipe.model.VehicleModels;
import com.talysonsilva.tabelafipe.model.DetailedVehicleData;
import com.talysonsilva.tabelafipe.service.ConectApi;
import com.talysonsilva.tabelafipe.service.ConvertsData;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Screen {
    private Scanner read = new Scanner(System.in);
    private ConectApi api = new ConectApi();
    private ConvertsData convertsData = new ConvertsData();
    private  final String URL_BASE = "https://parallelum.com.br/fipe/api/v1/";

    public  void displayMenu(){
        System.out.println("""
                Digite uma das seguintes opção:
                Carros
                Motos
                Caminhão
                """
        );
        String vehicleType = read.nextLine().toLowerCase().strip();
        checkVehicleType(vehicleType);

        //TODO separa em uma função
        System.out.println("\nModelos dessa marca: ");
        modeloLista.modelos().stream()
                .sorted(Comparator.comparing(Data::codigo))
                .forEach(System.out::println);

        //TODO separa em uma função
        System.out.println("\nDigite um trecho do nome do veiculo a ser buscado: ");
        var veiculo = read.nextLine();

        //TODO separa em uma função
        List<Data> modelosFiltrados = modeloLista.modelos().stream()
                .filter(a -> a.nome().toLowerCase() .contains(veiculo))
                .collect(Collectors.toList());
        //TODO separa em uma função
        System.out.println("\nModelos filtrados");
        modelosFiltrados.forEach(System.out::println);

        //TODO separa em uma função
        System.out.println("\nDigite por favor o código do modelo: ");
        var codigoModelo = read.nextLine();

        address = address +"/" +codigoModelo+ "/anos";
        json = api.getAddressData(address);
        List<Data> anos = convertsData.getJsonList(json, Data.class);

        //TODO separa em uma função
        List<DetailedVehicleData> veiculos = new ArrayList<>();

        for (int i = 0; i < anos.size() ; i++) {
            var enderecoAnos = address + "/" + anos.get(i).codigo();
            json = api.getAddressData(enderecoAnos);
            DetailedVehicleData veiculoAno = convertsData.getJsonData(json, DetailedVehicleData.class);
            veiculos.add(veiculoAno);
        }

        //TODO separa em uma função
        System.out.println("\nTodos os veiculos filtrados com avaliaçõees por ano: ");
        veiculos.forEach(System.out::println);

    }

    private void checkVehicleType(String vehicleType) {
        String createdAddress;
        if (vehicleType.contains("carr")){
            createdAddress = URL_BASE + "carros/marcas";
        }else if (vehicleType.contains("mot")){
            createdAddress = URL_BASE + "motos/marcas";
        }else{
            createdAddress = URL_BASE + "caminhoes/marcas";
        }

        creatingJson(createdAddress);
    }

    private void creatingJson(String address) {
        String json = api.getAddressData(address);
        showVehicleBrands(json);
    }

    private void showVehicleBrands(String json) {
        var listBrands = convertsData.getJsonList(json, Data.class);
        listBrands.stream()
                .sorted(Comparator.comparing(Data::codigo))
                .forEach(System.out::println);

        enterVehicleModelCode();
    }

    private void enterVehicleModelCode() {
        System.out.println("\nDigite o codigo do modelo: ");
        String codigoMarca = read.nextLine();

        var address = address + "/" + codigoMarca + "/modelos";
        var json = api.getAddressData(address);
        var modeloLista = convertsData.getJsonData(json, VehicleModels.class);

    }
}

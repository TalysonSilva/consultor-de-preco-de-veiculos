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

    private String address;

    public  void displayMenu(){
        System.out.println("""
                --------------<CONSULTOR DE PREÇO DE VEICULO>-----------
                Digite uma das seguintes opção:
                Carros
                Motos
                Caminhão
                """
        );

        String vehicleType = read.nextLine().toLowerCase().strip();
        checkVehicleType(vehicleType);

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
        showVehicleBrands(json, address);

    }

    private void showVehicleBrands(String json, String address) {
        var listBrands = convertsData.getJsonList(json, Data.class);
        listBrands.stream()
                .sorted(Comparator.comparing(Data::codigo))
                .forEach(System.out::println);

        enterVehicleModelCode(address);
    }

    private void enterVehicleModelCode(String address) {
        System.out.println("\nDigite o codigo do modelo: ");
        String codigoMarca = read.nextLine();

        var addressModelos = address + "/" + codigoMarca + "/modelos";
        var json = api.getAddressData(addressModelos);
        var modeloLista = convertsData.getJsonData(json, VehicleModels.class);

        this.address = addressModelos;

        modelList(modeloLista);
    }

    private void modelList(VehicleModels modeloLista) {

        System.out.println("\nModelos dessa marca: ");
        modeloLista.modelos().stream()
                .sorted(Comparator.comparing(Data::codigo))
                .forEach(System.out::println);

        var veiculo = enterVehicleName();
        filterModelVehicleName(veiculo, modeloLista);
    }

    private String enterVehicleName() {
        System.out.println("\nDigite um trecho do nome do veiculo a ser buscado: ");
        var veiculo = read.next();

       return veiculo;
    }

    private void filterModelVehicleName(String veiculo, VehicleModels modeloLista) {
        List<Data> modelosFiltrados = modeloLista.modelos().stream()
                .filter(a -> a.nome().toLowerCase().contains(veiculo))
                .collect(Collectors.toList());

        System.out.println("\nModelos filtrados");
        modelosFiltrados.forEach(System.out::println);

        enterVehicleCode(modelosFiltrados);
    }

    private void enterVehicleCode(List<Data> modelosFiltrados) {
        System.out.println("\nDigite por favor o código do modelo: ");
        var codigoModelo = read.next();

        listVehiclesYear(codigoModelo);
    }

    private void listVehiclesYear(String codigoModelo) {

        address = address +"/" +codigoModelo+ "/anos";
        var json = api.getAddressData(address);
        List<Data> anos = convertsData.getJsonList(json, Data.class);

        ListVehicles(anos);
    }

    private void ListVehicles(List<Data> anos) {
        List<DetailedVehicleData> veiculos = new ArrayList<>();

        for (int i = 0; i < anos.size() ; i++) {
            var enderecoAnos = address + "/" + anos.get(i).codigo();
            var json = api.getAddressData(enderecoAnos);
            DetailedVehicleData veiculoAno = convertsData.getJsonData(json, DetailedVehicleData.class);
            veiculos.add(veiculoAno);
        }

        filterVehicleYear(veiculos);
    }

    private void filterVehicleYear(List<DetailedVehicleData> veiculos) {
        System.out.println("\nTodos os veiculos filtrados com avaliaçõees por ano: ");
        veiculos.forEach(System.out::println);
    }


}

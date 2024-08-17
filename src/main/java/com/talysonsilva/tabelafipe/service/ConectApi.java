package com.talysonsilva.tabelafipe.service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class ConectApi {

    public  String getAddressData(String Address){
    HttpClient client =  HttpClient.newHttpClient();
    HttpRequest request = HttpRequest.newBuilder().uri(URI.create(Address))
            .build();
        HttpResponse<String > response = null;
        try{
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
        }catch (IOException e ){
            throw new RuntimeException(e);
        }catch (InterruptedException e ){
            throw new RuntimeException(e);
        }
        return response.body();
    }

}

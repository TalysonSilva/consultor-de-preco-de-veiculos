package com.talysonsilva.tabelafipe;

import com.talysonsilva.tabelafipe.view.Tela;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TabelafipeApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(TabelafipeApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		Tela tela = new Tela();
		tela.exibirMenu();
	}
}

package com.organizatec.peoplemgmt;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Ponto de entrada principal para a aplicação Organizatec.
 * Esta classe inicializa o Spring Boot, que configura automaticamente o
 * servidor de aplicação embutido e carrega os componentes do sistema.
 *
 */
@SpringBootApplication
public class OrganizatecApp {

    public static void main(String[] args) {
        SpringApplication.run(OrganizatecApp.class, args);
    }
}
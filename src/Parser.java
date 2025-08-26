// src/Parser.java
import input.Instrucao;
import input.Memoria;
import input.Registradores;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class Parser {
    public void relatorioFinal(Memoria memoria, Registradores registradores) {
        System.out.println("\n--------------------Registradores Finais--------------------");
        for (int j = 0; j < registradores.getTamanho(); j++) {
            System.out.printf("%3d ", j);
        }
        System.out.println();
        for (int i = 0; i < registradores.getRegistradores().length; i++) {
            System.out.printf("%4d", i, registradores.getValor(i));
        }
        System.out.println();
        System.out.println("\n--------------------Memória Final--------------------");
        for (int j = 0; j < memoria.getTamanho(); j++) {
            System.out.printf("%3d ", j);
        }
        System.out.println();
        for (int i = 0; i < memoria.getTamanho(); i++) {
            System.out.printf("%4d", memoria.getValor(i));
        }
        System.out.println();
    }
    public List<Instrucao> iniciarInstrucoes() {
        List<Instrucao> instrucoes = new ArrayList<>();
        System.out.println("\n\n--------------------Iniciando Instruções--------------------\n");
        try (BufferedReader br = new BufferedReader(new FileReader("src/input/instrucoes.txt"))) {
            String linha;
            int i = 0;
            while ((linha = br.readLine()) != null) {
                String[] partes = linha.trim().split("\\s+");

                String op = partes[0];
                String p1 = partes[1];
                String p2 = partes.length > 2 ? partes[2] : " ";
                String p3 = partes.length > 3 ? partes[3] : " ";
                System.out.println("Instrução[" + i + "] = " + op + " " + p1 + " " + p2 + " " + p3);

                Instrucao instrucao = new Instrucao(op, p1, p2, p3);
                instrucoes.add(instrucao);

                i++;
            }
        } catch (Exception e) {
            System.out.println("Erro ao carregar instruções: " + e.getMessage());
        }
        return instrucoes;
    }

    public Memoria iniciarMemoria() {
        Memoria memoria = new Memoria();
        try {
            BufferedReader br = new BufferedReader(new FileReader("src/input/memoria.txt"));
            String linha;
            System.out.println("\n--------------------Iniciando Memoria--------------------");
            for (int j = 0; j < memoria.getTamanho(); j++) {
                System.out.printf("%3d ", j);
            }
            System.out.println();
            int i = 0;
            while ((linha = br.readLine()) != null && i < memoria.getTamanho()) {
                int valor = Integer.parseInt(linha);
                memoria.setValor(i, valor);
                System.out.printf("%4d", valor);
                i++;
            }
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return memoria;
    }

    public Registradores iniciarRegistradores() {
        Registradores registradores = new Registradores();
        try {
            BufferedReader br = new BufferedReader(new FileReader("src/input/registradores.txt"));
            String linha;
            System.out.println("\n--------------------Iniciando Registradores--------------------");
            for (int j = 0; j < registradores.getTamanho(); j++) {
                System.out.printf("%4d", j);
            }
            System.out.println();
            int i = 0;
            while ((linha = br.readLine()) != null && i < registradores.getTamanho()) {
                int valor = Integer.parseInt(linha);
                registradores.setValor(i, valor);
                System.out.printf("%4d", valor);
                i++;
            }
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return registradores;
    }
}
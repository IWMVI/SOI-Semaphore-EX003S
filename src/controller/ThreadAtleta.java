package controller;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Semaphore;

public class ThreadAtleta extends Thread {

    private static int totalThreads;
    private Semaphore semaforo;
    private int numeroAtleta;
    private int distanciaPercorrida;
    private int pontuacao;
    private static Map<Integer, Integer> pontuacoes = new HashMap<>();

    public ThreadAtleta(Semaphore semaforo, int numeroAtleta) {
        this.semaforo = semaforo;
        this.numeroAtleta = numeroAtleta;
        this.distanciaPercorrida = 0;
        this.pontuacao = 0;
        totalThreads++;
    }

    @Override
    public void run() {
        corrida();
        try {
            semaforo.acquire();
            tiroAlvo();
        } catch (InterruptedException e) {
            System.err.println(e.getMessage());
        } finally {
            semaforo.release();
            ciclismo();
            verificarMostrarColocacao();
        }
    }

    private void corrida() {
        while (distanciaPercorrida < 3000) {
            int percurso = (int) ((Math.random() * 26) + 20);
            try {
                sleep(40);
            } catch (InterruptedException e) {
                System.err.println(e.getMessage());
            }
            distanciaPercorrida += percurso;
            System.out.println("Atleta #" + numeroAtleta + " já percorreu " + distanciaPercorrida + "m.");
        }
        pontuar(260 - (pontuacoes.size() * 10));
    }

    private void tiroAlvo() {
        int pontuacaoTiro = 0;
        for (int i = 0; i < 3; i++) {
            int alvo = (int) ((Math.random() * 11) + 0);
            pontuacaoTiro += alvo;
            System.out.println("Atleta #" + numeroAtleta + " acertou a pontuação no alvo de " + alvo + " pontos.");
        }
        pontuar(pontuacaoTiro);
    }

    private void ciclismo() {
        int distanciaCiclismo = 0;
        while (distanciaCiclismo < 5000) {
            try {
                sleep(40);
                int percurso = (int) ((Math.random() * 41) + 30);
                distanciaCiclismo += percurso;
                System.out.println("Atleta #" + numeroAtleta + " percorreu " + distanciaCiclismo + " na prova de ciclismo.");
            } catch (InterruptedException e) {
                System.err.println(e.getMessage());
            }
        }
        pontuar(260 - (pontuacoes.size() * 10));
    }

    private synchronized void pontuar(int pontos) {
        pontuacao += pontos;
        pontuacoes.put(numeroAtleta, pontuacao);
    }

    private void verificarMostrarColocacao() {
        synchronized (ThreadAtleta.class) {
            totalThreads--;
            if (totalThreads == 0) {
                mostrarColocacao();
            }
        }
    }

    private static void mostrarColocacao() {
        System.out.println("\nColocação dos Atletas:");

        List<Map.Entry<Integer, Integer>> listaPontuacoes = new ArrayList<>(pontuacoes.entrySet());
        listaPontuacoes.sort(Map.Entry.comparingByValue(Comparator.reverseOrder()));

        int posicao = 1;
        for (Map.Entry<Integer, Integer> entry : listaPontuacoes) {
            System.out.println("Atleta #" + entry.getKey() + ": Pontuação = " + entry.getValue() + ", Colocação = " + posicao);
            posicao++;
        }
    }
}

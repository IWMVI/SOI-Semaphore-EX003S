package view;

import java.util.concurrent.Semaphore;

import controller.ThreadAtleta;

public class Principal {
	public static void main(String[] args) {
		Semaphore semaforo = new Semaphore(5);
		for (int i = 0; i < 25; i++) {
			Thread t = new ThreadAtleta(semaforo, i);
			t.start();
		}
	}
}
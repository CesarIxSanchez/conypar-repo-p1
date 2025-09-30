import java.util.concurrent.Semaphore;

public class Semaforo {
    static int contador = 0; // contador compartido
    static final Semaphore mutex = new Semaphore(1, true); // mutex

    static class Trabajador implements Runnable {
        private final int iteraciones;

        Trabajador(int iteraciones) {
            this.iteraciones = iteraciones;
        }

        @Override
        public void run() {
            for (int i = 0; i < iteraciones; i++) {
                try {
                    mutex.acquire(); // se entra a la sección crítica
                    contador++; // ya en la sección crítica
                } catch (InterruptedException ignored) {
                } finally {
                    mutex.release(); // se sale de la sección crítica
                }
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        int hilos = 4; // 4 hilos
        int iteraciones = 10_000;

        Thread[] ts = new Thread[hilos];
        for (int i = 0; i < hilos; i++) {
            ts[i] = new Thread(new Trabajador(iteraciones));
            ts[i].start();
        }
        for (Thread t : ts) t.join();

        // se imprimen los resultados
        System.out.println("Resultados");
        System.out.println("\nEsperado: " + (hilos * iteraciones));
        System.out.println("Obtenido: " + contador);
    }
}

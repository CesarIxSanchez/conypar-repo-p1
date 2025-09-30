public class Monitor {
    static class Contador {
        private int valor = 0;

        // solo un hilo entra a la vez
        public synchronized void incrementar() {
            valor++;
        }

        // para retornar el valor
        public synchronized int get() {
            return valor;
        }
    }

    static class Trabajador implements Runnable {
        private final Contador contador; // contador compartido
        private final int iteraciones;

        Trabajador(Contador contador, int iteraciones) {
            this.contador = contador;
            this.iteraciones = iteraciones;
        }

        @Override
        public void run() {
            for (int i = 0; i < iteraciones; i++) {
                contador.incrementar();  // sección crítica protegida por el monitor
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        int hilos = 4; // 4 hilos
        int iteraciones = 10_000;
        Contador contador = new Contador();

        Thread[] ts = new Thread[hilos];
        for (int i = 0; i < hilos; i++) {
            ts[i] = new Thread(new Trabajador(contador, iteraciones));
            ts[i].start();
        }
        for (Thread t : ts) t.join();

        // se imprimen los resultados
        System.out.println("Resultados");
        System.out.println("\nEsperado: " + (hilos * iteraciones));
        System.out.println("Obtenido: " + contador.get());
    }
}

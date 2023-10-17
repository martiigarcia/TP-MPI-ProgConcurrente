package ejemplosExplicacion;

import mpi.MPI;

public class Explicacion2 {
    public static void main(String[] args) throws Exception {
        // Inicializa MPI
        MPI.Init(args);
        // Obtén el rango del proceso
        int rank = MPI.COMM_WORLD.Rank();
        // Si el proceso es el maestro
        if (rank == 0) {
            // Crea un número entero
            int numero = 1234;

            // Envía el número a todos los procesos esclavos
            MPI.COMM_WORLD.Bcast(new int[]{numero}, 0, 1, MPI.INT, 0);
        } else {
            // Recibe el número del maestro
            int[] numero = new int[1];
            MPI.COMM_WORLD.Bcast(numero, 0, 1, MPI.INT, 0);
            // Imprime el número
            System.out.println("Proceso esclavo " + rank + ": " + numero[0]);
        }
        // Finaliza MPI
        MPI.Finalize();
    }
}


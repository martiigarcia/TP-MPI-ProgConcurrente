package ejemplosExplicacion;

import mpi.MPI;

import java.util.Arrays;

public class Explicacion3 {
    public static void main(String[] args) throws Exception {
        // Inicializa MPI
        MPI.Init(args);
        // Obtén el rango del proceso
        int rank = MPI.COMM_WORLD.Rank();
        // Si el proceso es el maestro
        if (rank == 0) {
            // Crea un vector de números enteros
            int[] vector = {1, 2, 3, 4, 5};
            System.out.println("Arreglo del proceso maestro: " + Arrays.toString(vector));
            // Envía el vector a todos los procesos esclavos
            MPI.COMM_WORLD.Bcast(vector, 0, vector.length, MPI.INT, 0);
        } else {
            // Recibe el vector del maestro
            int[] vector = new int[5];
            MPI.COMM_WORLD.Bcast(vector, 0, vector.length, MPI.INT, 0);
            // Imprime el vector
            System.out.println("Proceso esclavo " + rank + ": " + Arrays.toString(vector));
        }
        // Finaliza MPI
        MPI.Finalize();
    }
}

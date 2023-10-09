import mpi.MPI;

public class Ejercicio2 {
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
            MPI.COMM_WORLD.Bcast(numero, 0, MPI.INT, MPI.COMM_WORLD.Get_size() - 1);
        } else {
            // Recibe el número del maestro
            int numero = MPI.COMM_WORLD.Bcast(null, 0, MPI.INT, 0,
                    MPI.COMM_WORLD.Get_size() - 1);
            // Imprime el número
            System.out.println("Proceso esclavo " + rank + ": " + numero);
        }
        // Finaliza MPI
        MPI.Finalize();
    }
}
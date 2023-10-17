package entregables;

import mpi.*;

public class Ejercicio5 {

//    5. Producto Escalar Distribuido:
//    Divide dos arreglos en fragmentos y distribuye estos fragmentos entre los procesos MPI.
//    Cada proceso debe calcular el producto escalar de su fragmento y enviar el resultado al proceso maestro.
//    El proceso maestro debe calcular el producto escalar total.

    public static void main(String[] args) throws MPIException {
        MPI.Init(args);
        int rank = MPI.COMM_WORLD.Rank();
        int size = MPI.COMM_WORLD.Size();

        if (size < 2) {
            System.err.println("Se necesitan al menos dos procesos para este ejemplo");
            MPI.Finalize();
            return;
        }
    }
}

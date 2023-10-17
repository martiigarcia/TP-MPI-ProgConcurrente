package entregables;

import mpi.*;

public class Ejercicio4 {

//    4. Suma de Arreglos Distribuida:
//    Divide un arreglo grande en varios fragmentos y distribuye estos fragmentos entre los procesos MPI.
//    Luego, cada proceso suma su fragmento y envía el resultado al proceso maestro.
//    El proceso maestro debe calcular la suma total.

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

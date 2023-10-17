package entregables;

import mpi.*;

public class Ejercicio3 {

//    3. Comunicación en Anillo:
//    Crea un programa MPI en el que múltiples procesos se comuniquen en un patrón de anillo.
//    Cada proceso debe recibir un mensaje de su proceso vecino y luego pasar el mensaje al siguiente
//    proceso en el anillo hasta que vuelva al proceso original.

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

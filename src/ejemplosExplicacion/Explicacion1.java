package ejemplosExplicacion;

import mpi.*;

public class Explicacion1 {
    public static void main(String[] args) throws MPIException {
        MPI.Init(args);
        int rank = MPI.COMM_WORLD.Rank();
        int size = MPI.COMM_WORLD.Size();
        if (rank == 0) {
            // Proceso maestro
            int[] dataToSend = {1, 2, 3, 4, 5};
            MPI.COMM_WORLD.Send(dataToSend, 0, dataToSend.length, MPI.INT, 1, 0);
            System.out.println("Proceso " + rank + " envió datos al proceso 1.");
        } else if (rank == 1) {
            // Proceso esclavo
            int[] receivedData = new int[5];
            MPI.COMM_WORLD.Recv(receivedData, 0, receivedData.length, MPI.INT, 0, 0);
            System.out.print("Proceso " + rank + " recibió datos: ");
            for (int i : receivedData) {
                System.out.print(i + " ");
            }
            System.out.println();
        }
        MPI.Finalize();
    }
}


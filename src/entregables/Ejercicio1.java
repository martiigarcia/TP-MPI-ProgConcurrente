package entregables;

import mpi.*;

public class Ejercicio1 {

//    1. Comunicación Simple:
//    Crea un programa en Java utilizando MPJ Express que envíe un mensaje desde un proceso
//    maestro (rango 0) a un proceso esclavo (rango 1). El mensaje puede ser un simple saludo.

    public static void main(String[] args) throws MPIException {
        MPI.Init(args);
        int rank = MPI.COMM_WORLD.Rank();
        int size = MPI.COMM_WORLD.Size();

        if (size < 2) {
            System.err.println("Se necesitan al menos dos procesos para este ejemplo");
            MPI.Finalize();
            return;
        }

        if (rank == 0) {
            // Proceso maestro
            String saludo = "Hola soy el proceso maestro!!!";
            MPI.COMM_WORLD.Send(saludo.toCharArray(), 0, saludo.length(), MPI.CHAR, 1, 0);
            System.out.println("Proceso maestro envió el saludo: {'" + saludo + "'}");
        } else if (rank == 1) {
            // Proceso esclavo
            char[] receivedMessage = new char[30];
            MPI.COMM_WORLD.Recv(receivedMessage, 0, receivedMessage.length, MPI.CHAR, 0, 0);
            System.out.println("Proceso esclavo (" + rank + ") recibió el mensaje: ");
            System.out.println(new String(receivedMessage));

        }
        MPI.Finalize();
    }

}

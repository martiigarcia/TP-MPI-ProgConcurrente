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

        //Se declara a dónde debe dirigirse el mensaje, en este caso al proceso anterior o al siguiente
        int siguiente = (rank + 1) % size;
        int anterior = (rank - 1 + size) % size;

        String mensaje = "Soy tu proceso vecino.";
        char[] receivedMessage = new char[30];


        if (rank == 0) {
            MPI.COMM_WORLD.Send(mensaje.toCharArray(), 0, mensaje.length(), MPI.CHAR, siguiente, 0);
            System.out.println("El proceso " + rank + " ha enviado el mensaje");

        }

        MPI.COMM_WORLD.Recv(receivedMessage, 0, receivedMessage.length, MPI.CHAR, anterior, 0);
        System.out.println("El proceso " + rank + " ha recibido el mensaje: " + String.valueOf(receivedMessage));


        if (rank != 0) {
            MPI.COMM_WORLD.Send(mensaje.toCharArray(), 0, mensaje.length(), MPI.CHAR, siguiente, 0);
            System.out.println("El proceso " + rank + " ha enviado el mensaje");
        }

        MPI.Finalize();


    }
}
//
//import mpi.*;
//
//public class Ejercicio3 {
//    public static void main(String[] args) {
//        MPI.Init(args);
//
//        int rank = MPI.COMM_WORLD.Rank();
//        int size = MPI.COMM_WORLD.Size();
//        int message = 12;
//        int[] receivedMessage=new int[2];
//
//        if (size < 2) {
//            System.out.println("Se necesitan al menos 2 procesos para ejecutar este programa.");
//            MPI.Finalize();
//            return;
//        }
//
//        if (rank == 0) {
//            // Inicializa el mensaje en el proceso 0
//            System.out.println("Proceso " + rank + " envía mensaje " + message + " al proceso " + (rank + 1) % size);
//            MPI.COMM_WORLD.Send(new int[] { message },0, 1, MPI.INT, (rank + 1) % size, 0);
//        }
//
//        // Comunicación en anillo
//        MPI.COMM_WORLD.Recv( receivedMessage ,0, 1, MPI.INT, (rank + size - 1) % size, 0);
//        System.out.println("Proceso " + rank + " recibió mensaje " + receivedMessage[0] + " de proceso " + (rank + size - 1) % size);
//
//        if (rank != 0) {
//            System.out.println("Proceso " + rank + " envía mensaje " + receivedMessage[0] + " al proceso " + (rank + 1) % size);
//            MPI.COMM_WORLD.Send( receivedMessage ,0, 1, MPI.INT, (rank + 1) % size, 0);
//        }
//
//        MPI.Finalize();
//
//    }
//}
//
//

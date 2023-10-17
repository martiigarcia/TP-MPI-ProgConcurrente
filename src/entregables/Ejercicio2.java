package entregables;

import mpi.*;

public class Ejercicio2 {

//    2. Comunicación Bidireccional:
//    Modifica el ejercicio anterior para que el proceso esclavo también envíe un mensaje de respuesta al proceso maestro.

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

            // Proceso maestro : debe enviar el mensaje pero tambien recibir un mensaje del esclavo

            String saludo = "Hola soy el proceso maestro!!!";
            System.out.println("Proceso maestro envia el saludo: {'" + saludo + "'}");
            MPI.COMM_WORLD.Send(saludo.toCharArray(), 0, saludo.length(), MPI.CHAR, 1, 0);


            //RECIBIR RESPUESTA DEL ESCLAVO
            char[] respuesta = new char[35];
            MPI.COMM_WORLD.Recv(respuesta, 0, respuesta.length, MPI.CHAR, 1, 1);
            System.out.println("Proceso maestro recibió la respuesta: ");
            System.out.println(new String(respuesta));


        } else if (rank == 1) {

            // Proceso esclavo: debe recibir un mensaje del maestro y luego responderle al mismo

            char[] receivedMessage = new char[30];
            MPI.COMM_WORLD.Recv(receivedMessage, 0, receivedMessage.length, MPI.CHAR, 0, 0);
            System.out.println("Proceso esclavo (" + rank + ") recibió el mensaje: ");
            System.out.println(new String(receivedMessage));


            //RESPONDERLE AL MAESTRO
            String respuesta = "Hola, soy el proceso esclavo " + rank + "!";
            MPI.COMM_WORLD.Send(respuesta.toCharArray(), 0, respuesta.length(), MPI.CHAR, 0, 1);

        }
        MPI.Finalize();
    }
}

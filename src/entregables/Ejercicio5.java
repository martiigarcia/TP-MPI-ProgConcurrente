package entregables;

import mpi.*;

import java.util.Arrays;

public class Ejercicio5 {

//    5. Producto Escalar Distribuido:
//    Divide dos arreglos en fragmentos y distribuye estos fragmentos entre los procesos MPI.
//    Cada proceso debe calcular el producto escalar de su fragmento y enviar el resultado al proceso maestro.
//    El proceso maestro debe calcular el producto escalar total.

//    public static void main(String[] args) throws MPIException {
//Deben ser 5 procesos para que funcione correctamente: Un maestro y cuatro esclavos
//        MPI.Init(args);
//        int rank = MPI.COMM_WORLD.Rank();
//        int size = MPI.COMM_WORLD.Size();
//
//        if (size < 2) {
//            System.err.println("Se necesitan al menos dos procesos para este ejemplo");
//            MPI.Finalize();
//            return;
//        }
//
//        // Definir los arreglos a ser utilizados (deben ser del mismo tamaño)
////        int[] arrayA = {1, 2, 3};
////        int[] arrayB = {1, 5, 7};
//        // 1*1 + 2*5 + 3*7
//        //  1  + 10  +  21
//        // 32
//
//        int[] arrayA = {1, 2, 3, 4, 5};
//        int[] arrayB = {6, 7, 8, 9, 10};
//        //1*6 + 2*7 + 3*8 + 4*9 + 5*10
//        // 6+14+24+36+50
//        //130
//
//
//        // Calcular el tamaño de cada fragmento de los arreglos
//        // Calcular el tamaño de cada fragmento de los arreglos
//        int fragmentSize = arrayA.length / size;
//        int remainder = arrayA.length % size;
//
//        int[] localA = new int[fragmentSize + (rank < remainder ? 1 : 0)];
//        int[] localB = new int[fragmentSize + (rank < remainder ? 1 : 0)];
//
//        int displacement = 0;
//        for (int i = 0; i < rank; i++) {
//            displacement += (i < remainder) ? fragmentSize + 1 : fragmentSize;
//        }
//
//        // Distribuir fragmentos de los arreglos entre los procesos
//        MPI.COMM_WORLD.Scatter(arrayA, displacement, localA.length, MPI.INT, localA, 0, localA.length, MPI.INT, 0);
//        MPI.COMM_WORLD.Scatter(arrayB, displacement, localB.length, MPI.INT, localB, 0, localB.length, MPI.INT, 0);
//
//
//        // Calcular el producto escalar local
//        int localProduct = 0;
//        for (int i = 0; i < fragmentSize; i++) {
//            localProduct += localA[i] * localB[i];
//        }
//
//        // Recopilar resultados parciales en el proceso maestro
//        int[] allProducts = new int[size];
//        MPI.COMM_WORLD.Gather(new int[]{localProduct}, 0, 1, MPI.INT, allProducts, 0, 1, MPI.INT, 0);
//
//        // Calcular el producto escalar total en el proceso maestro
//        if (rank == 0) {
//            int totalProduct = 0;
//            System.out.println(Arrays.toString(allProducts));
//            for (int product : allProducts) {
//                totalProduct += product;
//            }
//            System.out.println("El producto escalar total es: " + totalProduct);
//        }
//
//        MPI.Finalize();
//
//    }
    public static void main(String[] args) throws MPIException {
        //Deben ser 5 procesos para que funcione correctamente. Un proceso maestro y 4 esclavos
        MPI.Init(args);
        int rank = MPI.COMM_WORLD.Rank();
        int size = MPI.COMM_WORLD.Size();

        if (size < 2) {
            System.err.println("Se necesitan al menos dos procesos para este ejemplo");
            MPI.Finalize();
            return;
        }

        int[] arrayA = {1, 2, 3, 4, 5};
        int[] arrayB = {6, 7, 8, 9, 10};

        int[] sendcounts = new int[size];
        int[] displacements = new int[size];
        int fragmentSize = arrayA.length / size;

        int elementsPerProcess = arrayA.length / size;
        int remainingElements = arrayA.length % size;

        // Calcular la cantidad de elementos a enviar a cada proceso
        Arrays.fill(sendcounts, elementsPerProcess);

        // Distribuir los elementos restantes
        for (int i = 0; i < remainingElements; i++) {
            sendcounts[i]++;
        }

        // Calcular los desplazamientos
        int displacement = 0;
        for (int i = 0; i < size; i++) {
            displacements[i] = displacement;
            displacement += sendcounts[i];
        }

        int[] localA = new int[sendcounts[rank]];
        int[] localB = new int[sendcounts[rank]];


        // Distribuir fragmentos de los arreglos entre los procesos
        MPI.COMM_WORLD.Scatter(arrayA, 0, fragmentSize, MPI.INT, localA, 0, fragmentSize, MPI.INT, 0);
        MPI.COMM_WORLD.Scatter(arrayB, 0, fragmentSize, MPI.INT, localB, 0, fragmentSize, MPI.INT, 0);

        // Calcular el producto escalar local
        int localProduct = 0;
        for (int i = 0; i < localA.length; i++) {
            localProduct += localA[i] * localB[i];
        }

        // Recopilar resultados parciales en el proceso maestro
        int[] allProducts = new int[size];
        MPI.COMM_WORLD.Gather(new int[]{localProduct}, 0, 1, MPI.INT, allProducts, 0, 1, MPI.INT, 0);

        // Calcular el producto escalar total en el proceso maestro
        if (rank == 0) {
            int totalProduct = 0;
            for (int product : allProducts) {
                totalProduct += product;
            }
            System.out.println(Arrays.toString(allProducts));
            System.out.println("El producto escalar total es: " + totalProduct);
        }

        MPI.Finalize();
    }
}

package entregables;

import mpi.*;

import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

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
        //se asigna un tamaño al arreglo en base a la cantidad procesos
        int arregloTotalSize = size * 2;

        if (rank == 0) {

            // Crea el arreglo completo en el proceso maestro (rango 0)
            int[] arrayA = new int[arregloTotalSize];
            int[] arrayB = new int[arregloTotalSize];

            //cargar los array
            for (int i = 0; i < arregloTotalSize; i++) {
                arrayA[i] = i + 1;
                arrayB[i] = i + 1; //se puede cambiar por un math.random
            }

            ArrayList<int[]> pairsArrays = new ArrayList<>();
            int[] subArray;
            //unir los elementos de los arreglos A y B para multiplicarlos
            for (int i = 0; i < arregloTotalSize; i++) {
                subArray = new int[2];
                subArray[0] = arrayA[i];
                subArray[1] = arrayB[i];
                pairsArrays.add(subArray);
            }
            System.out.println("resultado de arrays: " + pairsArrays.stream().map(m -> Arrays.toString(m)).collect(Collectors.joining(", ")));

            ArrayList<ArrayList<int[]>> allArrays = new ArrayList<>();


            int cont = 0;
            ArrayList<int[]> auxSubArrayList = new ArrayList<>();
            //se separa de forma equitativa los arrays para que luego sean enviados a los respectivos esclavos
            for (int i = 0; i < pairsArrays.size(); i++) {
                auxSubArrayList.add(pairsArrays.get(i));

                if (allArrays.size() <= cont) {
                    allArrays.add(cont, auxSubArrayList);
                    auxSubArrayList = new ArrayList<>();
                } else {
                    allArrays.get(cont).add(pairsArrays.get(i));
                }
                cont++;
                if (cont == size - 1) {// tamaño de los esclavos
                    cont = 0;
                }

            }

            //mostrar allArray
            cont = 1;
            System.out.print("Array a distribuir:[");
            for (ArrayList<int[]> innerList : allArrays) {
                System.out.print("Proceso " + cont + " =[");
                for (int[] arr : innerList) {
                    System.out.print("[");
                    for (int i = 0; i < arr.length; i++) {
                        System.out.print(arr[i]);
                        if (i < arr.length - 1) {
                            System.out.print(", ");
                        }
                    }
                    System.out.print("]");
                }
                cont++;
                System.out.print("],");
            }
            System.out.println("]");

            cont = 1;
            //se transforma el arraylist a un array para poderlo enviar por el send a los respectivos esclavos
            for (ArrayList<int[]> i : allArrays) {
                int[] sendArray = new int[i.size()];
                sendArray = i.stream()
                        .flatMapToInt(Arrays::stream)
                        .toArray();
                //  System.out.println("El Rank "+cont+" calcula el producto escalar de: " +Arrays.toString(sendArray));
                int length = sendArray.length;
                // Envía la longitud del arreglo primero
                MPI.COMM_WORLD.Send(new int[]{length}, 0, 1, MPI.INT, cont, 0);

                //envia el array
                MPI.COMM_WORLD.Send(sendArray, 0, length, MPI.INT, cont, 0);
                cont++;

            }
            MPI.COMM_WORLD.Barrier();
            //se reciben los resultados del producto escalar que realizaron los esclavos
            int[] resultadoRecibido = new int[1];
            int[] resultadosParciales = new int[size - 1];
            for (int i = 1; i < size; i++) {
                MPI.COMM_WORLD.Recv(resultadoRecibido, 0, resultadoRecibido.length, MPI.INT, i, 0);
                resultadosParciales[i - 1] = resultadoRecibido[0];
            }
            // Calcula la suma total
            int sumaTotalFinal = Arrays.stream(resultadosParciales).sum();
            System.out.println("Resultado total es: " + sumaTotalFinal);

        } else {

            //recibe el tamaño del arreglo
            int[] lengthArray = new int[1];
            MPI.COMM_WORLD.Recv(lengthArray, 0, 1, MPI.INT, 0, 0);
            //recibir el arreglo
            int[] scalarArray = new int[lengthArray[0]];
            MPI.COMM_WORLD.Recv(scalarArray, 0, scalarArray.length, MPI.INT, 0, 0);
            int result = 0;
            for (int i = 0; i < scalarArray.length; i += 2) {
                if ((i + 1) > scalarArray.length) {
                    break;
                }
                result += scalarArray[i] * scalarArray[i + 1];
            }
            System.out.println("Rank:" + rank + ",el resultado del producto escalar de " + Arrays.toString(scalarArray) + " es: " + result);
            MPI.COMM_WORLD.Barrier();
            MPI.COMM_WORLD.Send(new int[]{result}, 0, 1, MPI.INT, 0, 0);
            MPI.COMM_WORLD.Barrier();

        }


        MPI.Finalize();
    }
}




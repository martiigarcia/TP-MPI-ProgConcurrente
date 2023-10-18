package entregables;

import mpi.*;

import java.util.ArrayList;
import java.util.Arrays;

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

        int arregloTotalSize = size * 2;
//                (size%2==0)?size*3:size*2;
        int tamañoSubArray = 2;
        //(int) Math.ceil((double) arregloTotalSize / (size - 1));

        if (rank == 0) {
            // Crea el arreglo completo en el proceso maestro (rango 0)
            int[] arregloTotal = new int[arregloTotalSize];
            for (int i = 0; i < arregloTotalSize; i++) {
                arregloTotal[i] = i + 1;
            }

            ArrayList<ArrayList<Integer>> todosLosArrays = new ArrayList<>();
            ArrayList<Integer> subArray = new ArrayList<>();
            int cont = 0;

            for (int i = 0; i < arregloTotalSize; i++) {
                subArray.add(arregloTotal[i]);

                if (todosLosArrays.size()<=cont) {
                    todosLosArrays.add(cont, subArray);
                    subArray = new ArrayList<>();
                } else {
                    todosLosArrays.get(cont).add(arregloTotal[i]);
                }
                cont++;
                if (cont == size - 1) {// tamaño de los esclavos
                    cont = 0;
                }

            }

            System.out.println(todosLosArrays);
            cont = 0;
            for (ArrayList<Integer> i : todosLosArrays) {
                cont++;
                int[] arrayEnviados = i.stream().mapToInt(Integer::intValue).toArray();

                MPI.COMM_WORLD.Send(arrayEnviados, 0, arrayEnviados.length, MPI.INT, cont, 0);
            }
            MPI.COMM_WORLD.Barrier();

            int[] resultadoRecibido = new int[1];
            int[] resultadosParciales = new int[size - 1];
            for (int i = 1; i < size; i++) {
                MPI.COMM_WORLD.Recv(resultadoRecibido, 0, resultadoRecibido.length, MPI.INT, i, 0);
                resultadosParciales[i - 1] = resultadoRecibido[0];
            }
            // Calcula la suma total
            int sumaTotalFinal = Arrays.stream(resultadosParciales).sum();
            System.out.println("Resultado: " + sumaTotalFinal);
        } else {
            int[] sumArray = new int[tamañoSubArray+1];
            MPI.COMM_WORLD.Recv(sumArray, 0, sumArray.length, MPI.INT, 0, 0);
            System.out.println("el proceso:" + rank + " recibe el arreglo: " + Arrays.toString(sumArray));
            int resultado = Arrays.stream(sumArray).sum();
            MPI.COMM_WORLD.Barrier();
            MPI.COMM_WORLD.Send(new int[]{resultado}, 0, 1, MPI.INT, 0, 0);
            MPI.COMM_WORLD.Barrier();
            System.out.println("el proceso:" + rank + " envia el resultado del arreglo: " + resultado);
        }


        MPI.Finalize();
    }


}


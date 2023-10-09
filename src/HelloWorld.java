import mpi.*;

public class HelloWorld {
    public static void main(String[] args) throws MPIException {
        MPI.Init(args);
        int me = MPI.COMM_WORLD.Rank();
        int size = MPI.COMM_WORLD.Size();
        System.out.println("Hi from <" + me + ">" + " and the size is " + size);
        MPI.Finalize();
    }
}

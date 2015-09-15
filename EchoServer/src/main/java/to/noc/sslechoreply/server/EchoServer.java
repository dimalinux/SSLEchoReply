package to.noc.sslechoreply.server;

import javax.net.ServerSocketFactory;
import javax.net.ssl.SSLServerSocketFactory;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class EchoServer {

    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Server requires single parameter: the port to listen on for connections");
            System.exit(1);
        }

        //
        //  Sorry for the hack.  Using a negative port value will disable SSL.  When testing a load balancer
        //  that terminates SSL, I needed a way to use a non-SSL connection on the server (while still
        //  using SSL on the client).
        //
        int port = Integer.parseInt(args[0]);
        boolean skipSSL = port < 0;
        port = Math.abs(port);

        ExecutorService executor = Executors.newCachedThreadPool();

        try {
            ServerSocketFactory socketFactory = skipSSL ?
                    ServerSocketFactory.getDefault() : SSLServerSocketFactory.getDefault();
            ServerSocket serverSocket = socketFactory.createServerSocket(port);
            System.out.println("Listing on 0.0.0.0:" + port);

            while (true) {
                final Socket clientCommSocket = serverSocket.accept();
                clientCommSocket.setTcpNoDelay(true);
                System.out.println("New client connection from: " + clientCommSocket.getInetAddress().getHostAddress());
                executor.execute(new ClientComm(clientCommSocket));
            }
        } catch(Exception e) {
            System.out.println("Error accepting client connections" + e);
        }
    }
}

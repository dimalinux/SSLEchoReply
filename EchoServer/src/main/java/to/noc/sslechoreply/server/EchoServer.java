package to.noc.sslechoreply.server;

import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSocket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class EchoServer {

    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Server requires single parameter: the port to listen on for connections");
            System.exit(1);
        }

        int port = Integer.parseInt(args[0]);

        ExecutorService executor = Executors.newCachedThreadPool();

        try {
            SSLServerSocketFactory socketFactory = (SSLServerSocketFactory) SSLServerSocketFactory.getDefault();
            SSLServerSocket serverSocket = (SSLServerSocket) socketFactory.createServerSocket(port);
            System.out.println("Listing on 0.0.0.0:" + port);

            while (true) {
                final SSLSocket clientCommSocket = (SSLSocket) serverSocket.accept();
                clientCommSocket.setTcpNoDelay(true);
                System.out.println("New client connection from: " + clientCommSocket.getInetAddress());
                executor.execute(new ClientComm(clientCommSocket));
            }
        } catch(Exception e) {
            System.out.println("Error accepting client connections" + e);
        }
    }
}

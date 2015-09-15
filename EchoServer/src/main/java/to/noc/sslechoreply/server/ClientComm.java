package to.noc.sslechoreply.server;

import java.io.*;
import java.net.Socket;

public class ClientComm implements Runnable {

    final Socket clientCommSocket;
    final String clientIP;
    final PrintWriter writer;
    final BufferedReader reader;

    public ClientComm(Socket clientCommSocket) throws IOException {
        this.clientCommSocket = clientCommSocket;
        clientIP = clientCommSocket.getInetAddress().getHostAddress();
        this.writer = new PrintWriter(new OutputStreamWriter(clientCommSocket.getOutputStream()), true);
        this.reader = new BufferedReader(new InputStreamReader(clientCommSocket.getInputStream()));
    }

    @Override
    public void run() {
        writer.println("You (" + clientIP + ") have connected to the echo server");
        writer.flush();
        String line;
        try {
            while ((line = reader.readLine()) != null) {
                System.out.println("Received: " + line);
                writer.println(line); // echo back to client
                writer.flush();
            }
        } catch (IOException e) {
            System.err.println("Error communicating with client");
            e.printStackTrace();
        }

        try {
            clientCommSocket.close();
        } catch (IOException e) {
        }
        System.out.println("Connection from client IP '" + clientIP + "' is closed");
    }
}

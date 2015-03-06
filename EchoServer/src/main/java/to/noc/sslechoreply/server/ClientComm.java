package to.noc.sslechoreply.server;

import javax.net.ssl.SSLSocket;
import java.io.*;

public class ClientComm implements Runnable {

    final SSLSocket clientCommSocket;
    final PrintWriter writer;
    final BufferedReader reader;

    public ClientComm(SSLSocket clientCommSocket) throws IOException {
        this.clientCommSocket = clientCommSocket;
        this.writer = new PrintWriter(new OutputStreamWriter(clientCommSocket.getOutputStream()), true);
        this.reader = new BufferedReader(new InputStreamReader(clientCommSocket.getInputStream()));
    }

    @Override
    public void run() {
        writer.println("Connected to echo server");
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
    }
}

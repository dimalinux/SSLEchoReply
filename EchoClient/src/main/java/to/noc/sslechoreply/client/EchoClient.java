package to.noc.sslechoreply.client;

import javax.net.SocketFactory;
import javax.net.ssl.SSLParameters;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import java.io.*;
import java.net.Socket;
import java.net.SocketException;

public class EchoClient {

    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("Usage: java -jar EchoClient.jar <host> <port>");
            System.exit(1);
        }
        try {
            String hostname = args[0];

            //  Hack alert. Using a negative port number uses plain TCP instead of SSL.
            int port = Integer.parseInt(args[1]);
            boolean skipSSL = port < 0;
            port = Math.abs(port);

            System.out.println("About to connect to '" + hostname + "' on port " + port + " using SSL=" + !skipSSL);

            final Socket socket = createSocketToEchoServer(hostname, port, skipSSL);
            final BufferedReader sockReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            final PrintWriter sockWriter = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);

            // Thread to read standard input
            new Thread(new Runnable() {
                @Override
                public void run() {
                    BufferedReader inputReader = new BufferedReader(new InputStreamReader(System.in));
                    String inputLine;
                    try {
                        while ((inputLine = inputReader.readLine()) != null) {
                            if (inputLine.trim().equals("exit")) {
                                break;
                            }
                            sockWriter.println(inputLine);
                            sockWriter.flush();
                        }
                        socket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                        System.exit(1);
                    }
                }
            }
            ).start();


            try {
                String sockLine;
                while ((sockLine = sockReader.readLine()) != null) {
                    System.out.println("Echo reply: " + sockLine);
                }
            } catch (SocketException ex) {
                // exception normal, since the standard input reader closes the socket
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static Socket createSocketToEchoServer(String hostname, int port, boolean skipSSL) throws IOException {
        SocketFactory socketFactory = skipSSL ? SocketFactory.getDefault() : SSLSocketFactory.getDefault();
        final Socket socket = socketFactory.createSocket(hostname, port);

        if (!skipSSL) {
            // Hostname verification is not done by default in Java with raw SSL connections.
            // The next 3 lines enable it.
            SSLParameters sslParams = new SSLParameters();
            sslParams.setEndpointIdentificationAlgorithm("HTTPS");
            ((SSLSocket)socket).setSSLParameters(sslParams);
        }
        // it's interactive chat, so we don't want buffering delays
        socket.setTcpNoDelay(true);

        return socket;
    }
}

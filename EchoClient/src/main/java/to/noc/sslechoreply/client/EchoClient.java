package to.noc.sslechoreply.client;

import javax.net.ssl.SSLParameters;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import java.io.*;

public class EchoClient {

    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("Usage: java -jar EchoClient.jar <host> <port>");
            System.exit(1);
        }
        try {
            String hostname = args[0];
            int port = Integer.parseInt(args[1]);

            System.out.println("About to connect to '" + hostname + "' on port " + port);

            SSLSocketFactory socketFactory = (SSLSocketFactory) SSLSocketFactory.getDefault();
            SSLSocket socket = (SSLSocket) socketFactory.createSocket(hostname, port);

            // Hostname verification is not done by default in Java with raw SSL connections.
            // The next 3 lines enable it.
            SSLParameters sslParams = new SSLParameters();
            sslParams.setEndpointIdentificationAlgorithm("HTTPS");
            socket.setSSLParameters(sslParams);

            socket.setTcpNoDelay(true);

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
                            sockWriter.println(inputLine);
                            sockWriter.flush();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                        System.exit(1);
                    }
                }
            }
            ).start();


            String sockLine;
            while ( (sockLine = sockReader.readLine()) != null ) {
                System.out.println("Echo reply: " + sockLine);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

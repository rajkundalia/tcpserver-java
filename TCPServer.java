package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

// https://github.com/arpitbbhayani/tcpserver
// https://youtu.be/f9gUFy-9uCM
public class TCPServer {
    public static void main(String[] args) {
        ServerSocket listener = null;

        try {
            listener = new ServerSocket(8001);
            while (true) {
                System.out.println("waiting for the client to connect");
                // this accept is a blocking call
                Socket socket = listener.accept();
                // java needs a close to unless we use a try with resources
                System.out.println("client connected");
                Thread thread = new Thread(() -> {
                    someAction(socket);
                });
                thread.start();
                //new Thread(() -> someAction(socket)).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (listener != null) {
                try {
                    listener.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static void someAction(Socket socket) {
        InputStream input;
        try {
            input = socket.getInputStream();
            // read and write in sockets are blocking calls
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(input));
            String line = bufferedReader.readLine();
            //System.out.println(line);
            OutputStream outputStream = socket.getOutputStream();
            String response = "HTTP/1.1 200 OK\r\n\r\n hello world \r\n";
            System.out.println("processing the request");
            Thread.sleep(8000);
            outputStream.write(response.getBytes(StandardCharsets.UTF_8));
            outputStream.flush();
            socket.close();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

    }
}

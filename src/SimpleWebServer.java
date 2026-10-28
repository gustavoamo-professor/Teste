
import java.io.*;
import java.net.*;
import java.util.*;

public class SimpleWebServer {

    static final String WEB_ROOT = "./web";

    public static void main(String[] args) {
        try {
            ServerSocket serverSocket = new ServerSocket(8088);
            System.out.println("Server started on port 8088");

            while (true) {
                Socket clientSocket = serverSocket.accept();
                new Thread(new ClientHandler(clientSocket)).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

class ClientHandler implements Runnable {

    private Socket clientSocket;

    public ClientHandler(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    @Override
    public void run() {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream())); OutputStream out = clientSocket.getOutputStream()) {

            String requestLine = in.readLine();
            if (requestLine == null) {
                return;
            }

            StringTokenizer tokens = new StringTokenizer(requestLine);
            String method = tokens.nextToken();
            String requestedFile = tokens.nextToken();

            //System.out.println(requestedFile);
            if (requestedFile.equals("/")) {

                requestedFile = "/index.html";
            }

            File file = new File(SimpleWebServer.WEB_ROOT + requestedFile);

            //System.out.println(file.getAbsolutePath());
            if (file.exists() && !file.isDirectory()) {
                String mimeType = getMimeType(requestedFile);
                byte[] fileData = readFileData(file);

                //System.out.println("Aqui");
                out.write(("HTTP/1.1 200 OK\r\n").getBytes());
                out.write(("Content-Type: " + mimeType + "\r\n").getBytes());
                out.write(("Content-Length: " + fileData.length + "\r\n").getBytes());
                out.write(("\r\n").getBytes());
                out.write(fileData);
            } else {
                String response = "HTTP/1.1 404 Not Found\r\n\r\n<h1>404 Arquivo não encontrado</h1>";
                out.write(response.getBytes());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String getMimeType(String fileName) {
        if (fileName.endsWith(".html")) {
            return "text/html"; 
        }else if (fileName.endsWith(".css")) {
            return "text/css"; 
        }else if (fileName.endsWith(".js")) {
            return "application/javascript"; 
        }else if (fileName.endsWith(".jpg") || fileName.endsWith(".jpeg")) {
            return "image/jpeg"; 
        }else if (fileName.endsWith(".png")) {
            return "image/png"; 
        }else {
            return "application/octet-stream";
        }
    }

    private byte[] readFileData(File file) throws IOException {
        FileInputStream fileIn = null;
        byte[] fileData = new byte[(int) file.length()];

        try {
            fileIn = new FileInputStream(file);
            fileIn.read(fileData);
        } finally {
            if (fileIn != null) {
                fileIn.close();
            }
        }

        return fileData;
    }
}

/*
 * File:     Server
 * Package:  org.dromakin
 * Project:  netology_client_server
 *
 * Created by dromakin as 21.01.2023
 *
 * author - dromakin
 * maintainer - dromakin
 * version - 2023.01.21
 */

package org.dromakin;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private ServerSocket serverSocket;
    private Socket clientSocket;
    private PrintWriter out;
    private BufferedReader in;

    private static final Logger logger = LogManager.getLogger(Server.class);

    public void start(int port) throws ServerException {
        try {
            serverSocket = new ServerSocket(port);
            clientSocket = serverSocket.accept();
            out = new PrintWriter(clientSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            logger.info("Server started!");

            logger.info("New connection accepted");
            final String msg = in.readLine();
            out.println(String.format("Hi, your msg: \"%s\"", msg));

        } catch (IOException e) {
            throw new ServerException(e.getMessage(), e);
        }

    }

    public void stop() throws ServerException {
        try {
            in.close();
            out.close();
            clientSocket.close();
            serverSocket.close();
            logger.info("Server stopped!");
        } catch (IOException e) {
            throw new ServerException(e.getMessage(), e);
        }

    }

    public static void main(String[] args) {
        Server server=new Server();
        try {
            server.start(8888);
        } catch (ServerException e) {
            logger.error(e.getMessage(), e);
            try {
                server.stop();
            } catch (ServerException ex) {
                logger.error(e.getMessage(), e);
            }
        }
    }
}

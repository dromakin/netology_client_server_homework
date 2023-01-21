package org.dromakin;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ClientTest {
    Client client;

    final Runnable serverService = new Runnable() {
        private final Logger logger = LogManager.getLogger(Server.class);

        public void run() {
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
    };

    private Thread thread;

    @BeforeEach
    void setUp() throws ClientException {
        thread = new Thread(serverService);
        thread.start();
        client = new Client();
        client.startConnection("localhost", 8888);
    }

    @AfterEach
    void tearDown() throws ClientException {
        client.stopConnection();
        thread.interrupt();
    }

    @Test
    void givenClient_whenServerRespondsWhenStarted_thenCorrect() throws ClientException {
        String response = client.sendMessage("hello server");
        String expected = String.format("Hi, your msg: \"%s\"", "hello server");
        assertEquals(expected, response);
    }
}
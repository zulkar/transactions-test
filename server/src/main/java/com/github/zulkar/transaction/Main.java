package com.github.zulkar.transaction;

public class Main {
    public static void main(String[] args) throws Exception {
        int port = parsePort(args);
        TransactionServer transactionServer = new TransactionServer(port);
        transactionServer.join();
    }

    private static int parsePort(String[] args) {
        return 8090;
    }
}

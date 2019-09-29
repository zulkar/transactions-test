package com.github.zulkar.transaction;

import org.apache.commons.cli.*;

public class Main {
    private static final int DEFAULT_PORT = 8090;

    public static void main(String[] args) throws Exception {
        int port = parsePort(args);
        TransactionServer transactionServer = new TransactionServer(port);
        transactionServer.join();
    }

    private static int parsePort(String[] args) {
        Options options = new Options();
        options.addOption("p", "port", true, "port to start");
        CommandLineParser parser = new DefaultParser();
        try {
            CommandLine cmd = parser.parse(options, args);
            String port = cmd.getOptionValue("p");
            if (port == null) {
                return DEFAULT_PORT;
            }
            return Integer.parseInt(port);
        } catch (ParseException | NumberFormatException e) {
            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp("transactions test", options);
            System.exit(1);
            return -1;
        }
    }
}

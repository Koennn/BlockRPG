package me.koenn.blockrpg.util;

import me.koenn.blockrpg.BlockRPG;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Console implements Runnable {

    private final BufferedReader consoleReader;

    public Console() {
        this.consoleReader = new BufferedReader(new InputStreamReader(System.in));
    }

    @Override
    public void run() {
        String line = "";
        try {
            if ((line = consoleReader.readLine()) == null) {
                return;
            }
        } catch (IOException e) {
            BlockRPG.LOGGER.fatal("Error while reading console input: " + e);
        }

        switch (line) {
            case "exit":
                BlockRPG.LOGGER.info("Exit command received, exitting...");
                System.exit(0);
                break;
            default:
                BlockRPG.LOGGER.warn(String.format("Unknown command \'%s\'", line));
        }
    }
}

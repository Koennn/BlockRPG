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
            BlockRPG.getLogger().fatal("Error while reading console input: " + e);
        }

        switch (line) {
            case "exit":
                BlockRPG.getLogger().info("Exit command received, exitting...");
                System.exit(0);
                break;
            default:
                BlockRPG.getLogger().warn(String.format("Unknown command \'%s\'", line));
        }
    }
}

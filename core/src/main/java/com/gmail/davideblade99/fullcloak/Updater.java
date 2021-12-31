/*
 * Copyright (c) DavideBlade.
 *
 * All Rights Reserved.
 */

package com.gmail.davideblade99.fullcloak;

import com.gmail.davideblade99.fullcloak.util.MessageUtil;
import org.bukkit.Bukkit;
import org.bukkit.plugin.IllegalPluginAccessException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

final class Updater {

    public interface ResponseHandler {

        /**
         * Called when the updater finds a new version.
         *
         * @param newVersion - the new version
         */
        void onUpdateFound(final String newVersion);
    }

    private final FullCloak plugin;

    Updater(final FullCloak instance) {
        this.plugin = instance;
    }

    void checkForUpdate(final ResponseHandler responseHandler) {
        if (!plugin.getSettings().checkUpdate())
            return;

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    final HttpURLConnection con = (HttpURLConnection) new URL("https://api.spigotmc.org/legacy/update.php?resource=16214").openConnection();
                    final String newVersion = new BufferedReader(new InputStreamReader(con.getInputStream())).readLine();

                    if (isNewerVersion(newVersion)) {
                        Bukkit.getScheduler().runTaskAsynchronously(plugin, new Runnable() {
                            @Override
                            public void run() {
                                if (newVersion.contains(" "))
                                    responseHandler.onUpdateFound(newVersion.split(" ")[0]);
                                else
                                    responseHandler.onUpdateFound(newVersion);
                            }
                        });
                    }
                }
                catch (final IOException ignored) {
                    MessageUtil.sendMessageToConsole("&cCould not contact Spigot to check for updates.");
                }
                catch (final IllegalPluginAccessException ignored) {
                    // Plugin not enabled
                }
                catch (final Exception e) {
                    e.printStackTrace();
                    MessageUtil.sendMessageToConsole("&cUnable to check for updates: unhandled exception.");
                }
            }
        }).start();
    }

    /**
     * Compare the version found with the plugin's version
     */
    private boolean isNewerVersion(final String versionOnSpigot) {
        return !plugin.getDescription().getVersion().equals(versionOnSpigot);
    }
}
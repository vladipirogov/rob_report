package org.wincc.report.tray;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import java.awt.AWTException;
import java.awt.Desktop;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;

@Component
@Slf4j
public class Tray {

    /**
     *
     */
    public void restartApplication() {
        try {
            final String javaBin = System.getProperty("java.home") + File.separator + "bin" + File.separator + "java";
            final File currentJar = new File(Tray.class.getProtectionDomain().getCodeSource().getLocation().toURI());

            /* is it a jar file? */
            if(!currentJar.getName().endsWith(".jar"))
                return;

            /* Build command: java -jar application.jar */
            final ArrayList<String> command = new ArrayList<>();
            command.add(javaBin);
            command.add("-jar");
            command.add(currentJar.getPath());

            final ProcessBuilder builder = new ProcessBuilder(command);
            builder.start();
            System.exit(0);
        } catch (URISyntaxException ex) {
            log.error(ex.getMessage());
        } catch (IOException ex) {
            log.error(ex.getMessage());
        }
    }


    /**
     *
     */
    @PostConstruct
    private void createAndShowGUI() {

        /* Use an appropriate Look and Feel */
        try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
        } catch (Exception ex) {
            log.error(ex.getMessage());
        }
        /* Turn off metal's use of bold fonts */
        UIManager.put("swing.boldMetal", Boolean.FALSE);

        //Check the SystemTray support
        if (!SystemTray.isSupported()) {
            log.error("SystemTray is not supported");
            return;
        }
        final PopupMenu popup = new PopupMenu();
        final TrayIcon trayIcon =
                new TrayIcon(createImage("/static/images/logo.png", "tray icon"));
        trayIcon.setImageAutoSize(true);
        final SystemTray tray = SystemTray.getSystemTray();

        // Create a popup menu components
        MenuItem aboutItem = new MenuItem("О программе");
        MenuItem setupItem = new MenuItem("Настройки");
        MenuItem restartItem = new MenuItem("Перезапустить");
        MenuItem open = new MenuItem("Открыть");
        MenuItem exitItem = new MenuItem("Выход");

        //Add components to popup menu
        popup.add(aboutItem);
        popup.addSeparator();
        popup.add(setupItem);
        popup.addSeparator();
        popup.add(open);
        popup.add(restartItem);
        popup.addSeparator();
        popup.add(exitItem);

        trayIcon.setPopupMenu(popup);

        try {
            tray.add(trayIcon);
        } catch (AWTException e) {
            log.error("TrayIcon could not be added.");
        }


        aboutItem.addActionListener(e -> JOptionPane.showMessageDialog(null,
                "WinCC Reports"));

        setupItem.addActionListener(e -> openWebPage("http://localhost:8080/setting"));

        open.addActionListener(e -> openWebPage("http://localhost:8080"));

        restartItem.addActionListener(e -> restartApplication());

        exitItem.addActionListener(e -> {
            tray.remove(trayIcon);
            System.exit(0);
        });
    }

    /**
     * Obtains the image URL
     * @param path
     * @param description
     * @return
     */
    private Image createImage(String path, String description) {
        URL imageURL = Tray.class.getResource(path);

        if (imageURL == null) {
            log.error("Resource not found: " + path);
            return null;
        } else {
            return (new ImageIcon(imageURL, description)).getImage();
        }
    }

    /**
     *
     * @param urlString
     */
    public void openWebPage(String urlString) {
        try {
            Desktop.getDesktop().browse(new URL(urlString).toURI());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

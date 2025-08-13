package com.jagex.launcher;

import com.jagex.*;
import net.runelite.api.Skill;
import net.runelite.client.RuneLite;

import java.applet.Applet;
import java.awt.*;
import java.net.URL;
import java.util.Properties;

import javax.swing.*;

public abstract class Loader extends Applet {

    private static final long serialVersionUID = 7639088664641445302L;
    public static Properties client_parameters = new Properties();
    public JFrame client_frame;
    public JPanel client_panel = new JPanel();

    private static final double clientVersion = 1.0;

    public static String IP = "0.0.0.0";
    public static boolean hosted = true;

    public static boolean usingRS = false;
    public static boolean useIsaac = false;
    public static boolean useRoute = false;
    public static boolean useMapsTest = false;
    public boolean takeScreeny;
    private static JFrame splash;
    public int screenshot;

    public static String SERVER_NAME = "Avalon";
    public final static int PORT = 43594;
    public static boolean LOBBY_ENABLED = false;
    public static boolean DISABLE_XTEA_CRASH = true;
    public static boolean DISABLE_USELESS_PACKETS = false;
    public static boolean DISABLE_RSA = false;
    public static boolean DISABLE_CS_MAP_CHAR_CHECK = true;
    public static boolean DISABLE_SOFTWARE_MODE = false;
    public static final int REVISION = 718;
    public static final int LOBBY_PORT = PORT;
    public static int SUB_REVISION = 1;
    public static final boolean ACCOUNT_CREATION_DISABLED = true;
    public static final boolean RS = false;

    public static String LOBBY_IP = IP;
    public static Loader instance;
    public static int[] outSizes = new int[256];

    public static TrayIcon trayIcon;

    public static void main(String[] args) throws Exception {
        ItemPrices.init();
        System.setProperty("java.net.preferIPv4Stack", "true");
        System.setProperty("java.net.preferIPv6Addresses", "false");
        setParams();
        GameClient client = new GameClient();
        client.supplyApplet(client);
        showSplash();
        RuneLite.clientA = client;
        splash.dispose();

        RuneLite.main(new String[]{});
    }

    public static void showSplash() {
        splash = new JFrame("\\u00A9 RuneLite RSPS ~ Your best experience!");
        splash.setIconImage(Toolkit.getDefaultToolkit().getImage(ClassLoader.getSystemResource("runelite.png")));
        splash.add(new JLabel(new ImageIcon(Toolkit.getDefaultToolkit().getImage(ClassLoader.getSystemResource("runelite.png")))));
        splash.setUndecorated(true);
        splash.setBackground(new Color(0, 0, 0, 0));
        splash.pack();
        splash.setLocationRelativeTo(null);
        splash.setAlwaysOnTop(true);
        splash.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        System.currentTimeMillis();
        splash.setVisible(true);
        splash.toFront();
    }

    static void setParams() {
        client_parameters.put("separate_jvm", "true");
        client_parameters.put("boxbgcolor", "black");
        client_parameters.put("image", "http://www.runescape.com/img/game/splash2.gif");
        client_parameters.put("centerimage", "true");
        client_parameters.put("boxborder", "false");
        client_parameters.put("java_arguments", "-Xmx256m -Xss2m -Dsun.java2d.noddraw=true -XX:CompileThreshold=1500 -Xincgc -XX:+UseConcMarkSweepGC -XX:+UseParNewGC");
        client_parameters.put("27", "0");
        client_parameters.put("1", "0");
        client_parameters.put("16", "false");
        client_parameters.put("17", "false");
        client_parameters.put("21", "1"); // WORLD ID
        client_parameters.put("30", "false");
        client_parameters.put("20", LOBBY_IP);
        client_parameters.put("29", "");
        client_parameters.put("11", "true");
        client_parameters.put("25", "1378752098");
        client_parameters.put("28", "0");
        client_parameters.put("8", ".runescape.com");
        client_parameters.put("23", "false");
        client_parameters.put("32", "0");
        client_parameters.put("15", "wwGlrZHF5gKN6D3mDdihco3oPeYN2KFybL9hUUFqOvk");
        client_parameters.put("0", "IjGJjn4L3q5lRpOR9ClzZQ");
        client_parameters.put("2", "");
        client_parameters.put("4", "1"); // WORLD ID
        client_parameters.put("14", "");
        client_parameters.put("5", "8194");
        client_parameters.put("-1", "QlwePyRU5GcnAn1lr035ag");
        client_parameters.put("6", "0");
        client_parameters.put("24", "true,false,0,43,200,18,0,21,354,-15,Verdana,11,0xF4ECE9,candy_bar_middle.gif,candy_bar_back.gif,candy_bar_outline_left.gif,candy_bar_outline_right.gif,candy_bar_outline_top.gif,candy_bar_outline_bottom.gif,loadbar_body_left.gif,loadbar_body_right.gif,loadbar_body_fill.gif,6");
        client_parameters.put("3", "hAJWGrsaETglRjuwxMwnlA/d5W6EgYWx");
        client_parameters.put("12", "false");
        client_parameters.put("13", "0");
        client_parameters.put("26", "0");
        client_parameters.put("9", "77");
        client_parameters.put("22", "false");
        client_parameters.put("18", "false");
        client_parameters.put("33", "");
        client_parameters.put("haveie6", "false");
    }

    public String getParameter(String string) {
        return (String) client_parameters.get(string);
    }

    public URL getDocumentBase() {
        return getCodeBase();
    }

    @Override
    public URL getCodeBase() {
        try {
            return new URL("https://" + IP);
        } catch (Exception exception) {
            exception.printStackTrace();
            return null;
        }
    }

    public static GameClient getClient() {
        return (GameClient) RuneLite.clientA;
    }

    public abstract byte[][][] getTileSettings();

    public abstract int[][][] getTileHeights();

    public abstract int getPlane();

    public abstract int getBaseX();

    public abstract int getBaseY();

    public abstract boolean isInInstancedRegion();

    public abstract int[][][] getInstanceTemplateChunks();

    public abstract int[][] getCollisionMaps(int plane);

    public abstract boolean isClientThread();

    public abstract Player getLocalPlayer();

    public abstract Canvas getCanvas();

    public abstract int getRealSkillLevel(Skill skill);

    public abstract int getSkillExperience(Skill skill);
}

package medousa.security;/*
 * Copyright (c) 03/2014.
 * Author: Changhee Kang
 * Location: Seoul, South Korea
 * Company: bayesailab.com
 * E-mail: ds.chkang@gmail.com
 */

public class MyDirectGraphOSMonitor {

    public static String OS = System.getProperty("os.name").toLowerCase();

    public static boolean isWindows() { return OS.contains("win"); }
    public static boolean isMac() { return OS.contains("mac"); }
    public static boolean isUnix() { return (OS.contains("nix") || OS.contains("nux") || OS.contains("aix")); }
    public static boolean isSolaris() {
        return OS.contains("sunos");
    }
}


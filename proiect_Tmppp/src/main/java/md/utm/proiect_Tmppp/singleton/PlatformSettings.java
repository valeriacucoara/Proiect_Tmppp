package md.utm.proiect_Tmppp.singleton;

public class PlatformSettings {

    private static PlatformSettings instance;

    private String platformName;
    private String version;

    // constructor privat
    private PlatformSettings() {
        platformName = "Recruitment Platform";
        version = "1.0";
    }

    public static synchronized PlatformSettings getInstance() {

        if (instance == null) {   // ← IF important din Singleton
            instance = new PlatformSettings();
        }

        return instance;
    }

    public String getPlatformName() {
        return platformName;
    }

    public String getVersion() {
        return version;
    }
}
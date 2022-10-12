package fr.terrawild.twstaffchat;

import fr.terrawild.terrawildapi.TerraWildAPI;
import fr.terrawild.terrawildapi.acf.PaperCommandManager;
import fr.terrawild.terrawildapi.configuration.ConfigurationManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class TWStaffChat extends JavaPlugin {

    private static TWStaffChat INSTANCE;
    private TerraWildAPI terraWildAPI;
    private ConfigurationManager cm;

    @Override
    public void onEnable() {
        INSTANCE = this;
        this.terraWildAPI = TerraWildAPI.getINSTANCE();

        this.cm = new ConfigurationManager(this);
        cm.setupDataFolder();
        cm.initNewFile(".", "config.yml", Contents.Config.CONTENT);

        PaperCommandManager paperCommandManager = new PaperCommandManager(this);
        paperCommandManager.registerCommand(new TWStaffChatCommand());
    }

    @Override
    public void onDisable() {
        cm.saveFiles();
    }

    public static TWStaffChat getINSTANCE() {
        return INSTANCE;
    }

    public ConfigurationManager getConfigurationManager() {
        return cm;
    }

    public TerraWildAPI getTerraWildAPI() {
        return terraWildAPI;
    }

}

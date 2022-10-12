package fr.terrawild.twstaffchat;

import fr.terrawild.terrawildapi.acf.BaseCommand;
import fr.terrawild.terrawildapi.acf.annotation.CommandAlias;
import fr.terrawild.terrawildapi.acf.annotation.CommandPermission;
import fr.terrawild.terrawildapi.acf.annotation.Default;
import fr.terrawild.terrawildapi.configuration.ConfigurationManager;
import org.bukkit.entity.Player;

@CommandAlias("stc")
public class TWStaffChatCommand extends BaseCommand {

    private final static String BASE_PERMISSIONS = "twstaffchat.command";
    private final static ConfigurationManager CM = TWStaffChat.getINSTANCE().getConfigurationManager();

    @Default
    @CommandPermission(BASE_PERMISSIONS)
    public static void onCommand(Player player) {
        new STCInventoryProvider(CM).getRyseInventory().open(player);
    }

    // Todo: reload command

}

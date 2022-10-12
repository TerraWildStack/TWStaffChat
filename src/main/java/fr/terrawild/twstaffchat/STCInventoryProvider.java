package fr.terrawild.twstaffchat;

import fr.terrawild.terrawildapi.TerraWildAPI;
import fr.terrawild.terrawildapi.configuration.ConfigurationManager;
import fr.terrawild.terrawildapi.configuration.YAMLParser;
import fr.terrawild.terrawildapi.item.ItemBuilder;
import fr.terrawild.terrawildapi.utils.ColoredText;
import io.github.rysefoxx.SlotIterator;
import io.github.rysefoxx.content.IntelligentItem;
import io.github.rysefoxx.content.InventoryProvider;
import io.github.rysefoxx.pagination.InventoryContents;
import io.github.rysefoxx.pagination.Pagination;
import io.github.rysefoxx.pagination.RyseInventory;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class STCInventoryProvider implements InventoryProvider {

    private final ConfigurationManager cm;
    private final RyseInventory ryseInventory;

    public STCInventoryProvider(ConfigurationManager cm) {
        this.cm = cm;

        ryseInventory = RyseInventory.builder()
                .provider(this)
                .size(9*6)
                .manager(TerraWildAPI.getINSTANCE().getInventoryManager())
                .title("Staff Chat")
                .build(TWStaffChat.getINSTANCE());
    }

    @Override
    public void init(Player player, InventoryContents contents) {
        final FileConfiguration cfg = cm.getConfigurationFile("config.yml");
        final ConfigurationSection messages = cfg.getConfigurationSection("messages");

        if (messages == null) return;

        final Pagination pagination = contents.pagination();
        pagination.iterator(slotIterator());

        for (String key : messages.getKeys(false)) {
            final ConfigurationSection messageSection = messages.getConfigurationSection(key);
            if (messageSection == null) return;

            final YAMLParser yamlParser = new YAMLParser(cfg);
            final ItemStack itemStack = yamlParser.getItemStack(
                    messageSection.getCurrentPath() + ".item",
                    new ItemBuilder(Material.BARRIER)
                            .setAmount(1)
                            .setDisplayName(new ColoredText("&cErreur de configuration de l'item !").buildComponent())
                            .getItemStack()
            );
            assert itemStack != null;

            final String message = messageSection.getString("message");

            if (message == null) {
                pagination.addItem(IntelligentItem.empty(itemStack));
            } else {
                pagination.addItem(IntelligentItem.of(itemStack, inventoryClickEvent -> {
                    final Player whoClicked = (Player) inventoryClickEvent.getWhoClicked();
                    whoClicked.chat(ChatColor.translateAlternateColorCodes('&', message));
                    whoClicked.closeInventory();
                }));
            }
        }
    }

    private SlotIterator slotIterator() {
        List<Integer> blacklist = new ArrayList<>(Arrays.asList(
                0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 17, 18, 26, 27, 35, 36, 44, 45, 46, 47, 48, 49, 50, 51, 52, 53
        ));

        return SlotIterator
                .builder()
                .startPosition(10)
                .endPosition(43)
                .blackList(blacklist)
                .type(SlotIterator.SlotIteratorType.HORIZONTAL)
                .build();
    }

    public RyseInventory getRyseInventory() {
        return ryseInventory;
    }
}

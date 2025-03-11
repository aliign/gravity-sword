package aliign.gravitysword.items;

import aliign.gravitysword.GravitySword;
import aliign.gravitysword.items.custom.GravitySwordItem;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroupEntries;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModItems {

    public static final Item BINARY_FLARE = registerItem("binary_flare", new GravitySwordItem(new FabricItemSettings()));

    private static void addItemsToIngredientItemGroup(FabricItemGroupEntries entries) {
        entries.add(BINARY_FLARE);
    }

    private static Item registerItem(String name, Item item) {
        return Registry.register(Registries.ITEM, new Identifier(GravitySword.MOD_ID, name), item);
    }

    public static void registerModItems() {
        GravitySword.LOGGER.info("Registering Mod Items for" + GravitySword.MOD_ID);
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.COMBAT).register(ModItems::addItemsToIngredientItemGroup);
    }
}

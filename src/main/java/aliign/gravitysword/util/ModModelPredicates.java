package aliign.gravitysword.util;

import aliign.gravitysword.GravitySword;
import aliign.gravitysword.items.ModItems;
import aliign.gravitysword.items.custom.GravitySwordItem;
import net.minecraft.client.item.ModelPredicateProviderRegistry;
import net.minecraft.util.Identifier;

public class ModModelPredicates {
    public static void registerModelPredicates() {
        ModelPredicateProviderRegistry.register(ModItems.BINARY_FLARE, new Identifier(GravitySword.MOD_ID, "charges"), (stack, world, entity, seed) -> {
            if (GravitySwordItem.GetCharges(stack) == 0) {
                return 1.0F;
            }
            return (float) (GravitySwordItem.GetCharges(stack) - GravitySwordItem.min_charges) / (GravitySwordItem.max_charges - GravitySwordItem.min_charges);
        });
    }
}

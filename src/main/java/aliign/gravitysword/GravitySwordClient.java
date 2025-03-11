package aliign.gravitysword;

import aliign.gravitysword.util.ModModelPredicates;
import net.fabricmc.api.ClientModInitializer;

public class GravitySwordClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ModModelPredicates.registerModelPredicates();
        GravitySword.LOGGER.info("Registering Mod Predicates");
    }
}

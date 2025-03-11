package aliign.gravitysword;

import aliign.gravitysword.items.ModItems;
import aliign.gravitysword.sound.ModSounds;
import aliign.gravitysword.util.ActiveComponent;
import aliign.gravitysword.util.GravityActiveComponent;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GravitySword implements ModInitializer {
	public static final String MOD_ID = "gravitysword";

	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		ModItems.registerModItems();
		GravitySwordComponents.ACTIVE_COMPONENT.hashCode();
		ModSounds.registerSounds();
		ServerPlayerEvents.COPY_FROM.register((oldPlayer, newPlayer, alive) -> {
			ActiveComponent oldComponent = GravitySwordComponents.ACTIVE_COMPONENT.maybeGet(oldPlayer).orElse(new GravityActiveComponent(oldPlayer));
			ActiveComponent newComponent = GravitySwordComponents.ACTIVE_COMPONENT.maybeGet(newPlayer).orElse(new GravityActiveComponent(newPlayer));
			newComponent.setValue(oldComponent.getValue());
		});
	}
}
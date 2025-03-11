package aliign.gravitysword;

import aliign.gravitysword.util.ActiveComponent;
import aliign.gravitysword.util.GravityActiveComponent;
import dev.onyxstudios.cca.api.v3.component.ComponentKey;
import dev.onyxstudios.cca.api.v3.component.ComponentRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentInitializer;
import dev.onyxstudios.cca.api.v3.entity.RespawnCopyStrategy;
import net.minecraft.util.Identifier;

public class GravitySwordComponents implements EntityComponentInitializer {
    public static final ComponentKey<ActiveComponent> ACTIVE_COMPONENT = ComponentRegistry.getOrCreate(Identifier.of(GravitySword.MOD_ID, "active"), ActiveComponent.class);
    @Override
    public void registerEntityComponentFactories(EntityComponentFactoryRegistry registry) {
        registry.registerForPlayers(ACTIVE_COMPONENT, GravityActiveComponent::new, RespawnCopyStrategy.ALWAYS_COPY);
    }
}

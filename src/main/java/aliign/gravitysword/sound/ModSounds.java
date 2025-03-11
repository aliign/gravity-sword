package aliign.gravitysword.sound;

import aliign.gravitysword.GravitySword;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

public class ModSounds {
    public static final SoundEvent BINARY_FLARE_CHARGE = registerSoundEvent("binary_flare_charge");
    public static final SoundEvent BINARY_FLARE_ACTIVATE = registerSoundEvent("binary_flare_activate");
    public static final SoundEvent BINARY_FLARE_WASTE = registerSoundEvent("binary_flare_waste");

    public static SoundEvent registerSoundEvent(String name) {
        Identifier id = new Identifier(GravitySword.MOD_ID, name);
        return Registry.register(Registries.SOUND_EVENT, id, SoundEvent.of(id));
    }
    public static void registerSounds() {

    }
}

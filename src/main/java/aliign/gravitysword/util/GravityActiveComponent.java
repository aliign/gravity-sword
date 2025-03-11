package aliign.gravitysword.util;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;

public class GravityActiveComponent implements ActiveComponent {
    private final PlayerEntity player;
    private boolean active = false;

    public GravityActiveComponent(PlayerEntity player) {
        this.player = player;
    }

    @Override
    public boolean getValue() {
        return active;
    }

    @Override
    public void setValue(boolean newActive) {
        active = newActive;
    }

    @Override
    public void readFromNbt(NbtCompound tag) {
        this.active = tag.getBoolean("Active");
    }

    @Override
    public void writeToNbt(NbtCompound tag) {
        tag.putBoolean("Active", this.active);
    }
}
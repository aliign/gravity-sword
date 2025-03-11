package aliign.gravitysword.util;

import dev.onyxstudios.cca.api.v3.component.Component;
import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import net.minecraft.nbt.NbtCompound;

public interface ActiveComponent extends Component, AutoSyncedComponent {
    boolean getValue();
    void setValue(boolean newActive);

    void readFromNbt(NbtCompound tag);
    void writeToNbt(NbtCompound tag);
}
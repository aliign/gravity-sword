package aliign.gravitysword.items.custom;

import aliign.gravitysword.GravitySword;
import aliign.gravitysword.GravitySwordComponents;
import aliign.gravitysword.sound.ModSounds;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import gravity_changer.GravityComponent;
import gravity_changer.api.GravityChangerAPI;
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.annotation.Nullable;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.client.item.TooltipData;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolMaterials;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.text.LiteralTextContent;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class GravitySwordItem extends SwordItem {
    public GravitySwordItem(SwordItem.Settings settings) {
        super(ToolMaterials.NETHERITE, 0, -2.6F, settings);
    }

    protected static final UUID ATTACK_MODIFIER_ID = UUID.fromString("4782863a-ebb6-402b-845a-e6aa7c49b4f6");
    public static int max_charges = 5;
    public static int min_charges = 1;
    public boolean test_active = false;

    public static int GetCharges(ItemStack stack) {
        NbtCompound nbt = stack.getNbt();
        return (nbt != null) ? nbt.getInt("Charges") : 0;
    }

    public static void SetCharges(ItemStack stack, int state) {
        stack.getOrCreateNbt().putInt("Charges", state);
    }

    public static boolean GetPlayerActive(PlayerEntity player) {
        return player.getComponent(GravitySwordComponents.ACTIVE_COMPONENT).getValue();
    }

    public static void SetPlayerActive(PlayerEntity player, boolean newActive) {
        player.getComponent(GravitySwordComponents.ACTIVE_COMPONENT).setValue(newActive);
        player.syncComponent(GravitySwordComponents.ACTIVE_COMPONENT);
        GravitySword.LOGGER.info(String.valueOf(newActive));
    }

    private void IncreaseCharges(ItemStack stack, PlayerEntity player, int i) {
        int charges = i + GetCharges(stack);
        if (charges > max_charges) {
            charges = max_charges;
            player.playSound(ModSounds.BINARY_FLARE_WASTE, SoundCategory.PLAYERS, 1.5F, (float) (0.9 + Math.random() * (0.2)));
        } else {
            player.playSound(ModSounds.BINARY_FLARE_CHARGE, SoundCategory.PLAYERS, 2.0F, (float) (0.9 + Math.random() * (0.2)));
        }
        SetCharges(stack, charges);
    }

    private void DecreaseCharges(ItemStack stack, PlayerEntity player, int i) {
        int charges = GetCharges(stack) - i;
        if (charges < min_charges) {
            charges = min_charges;
        }
        player.playSound(ModSounds.BINARY_FLARE_ACTIVATE, SoundCategory.PLAYERS, 0.3F, (float) (0.9 + Math.random() * (0.2)));
        SetCharges(stack, charges);
    }

    private static void applyAttributeModifiers(ItemStack stack, LivingEntity entity, @Nullable Hand hand) {
        float damageModifier = getDamageFromCharges(GetCharges(stack));
        NbtCompound nbt = stack.getNbt();
        if(nbt != null && nbt.contains("AttributeModifiers", 9)) {
            NbtList modifiers = nbt.getList("AttributeModifiers", 10);
            for (int i = 0; i < modifiers.size(); i++) {
                NbtCompound modifier = modifiers.getCompound(i);
                if (modifier.contains("UUID") && modifier.getUuid("UUID").equals(ATTACK_MODIFIER_ID)) {
                    modifiers.remove(i);
                    break;
                }
            }
            nbt.put("AttributeModifiers", modifiers);
            stack.setNbt(nbt);
        }
        stack.addAttributeModifier(EntityAttributes.GENERIC_ATTACK_DAMAGE, new EntityAttributeModifier(ATTACK_MODIFIER_ID, "Dynamic Attack Damage", damageModifier, EntityAttributeModifier.Operation.ADDITION), EquipmentSlot.MAINHAND);
        if (hand != null) {entity.setStackInHand(hand, stack);}
    }

    @Override
    public boolean postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        if (target.isDead() && attacker instanceof PlayerEntity player) {
            if (target instanceof PlayerEntity) {
                IncreaseCharges(stack, player, max_charges - min_charges);
            } else {
                IncreaseCharges(stack, player, 1);
            }
            applyAttributeModifiers(stack, attacker, Hand.MAIN_HAND);
        }
        return true;
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        if(!world.isClient()) {
            if(isUnderCeiling(user)) {
                ItemStack stack = user.getStackInHand(hand);
                if (GravityChangerAPI.getGravityDirection(user) != Direction.DOWN) {
                    SetPlayerActive(user, false);
                    DecreaseCharges(user.getStackInHand(hand), user, 0);
                    return TypedActionResult.success(user.getStackInHand(hand));
                }
                if (GetCharges(user.getStackInHand(hand)) > 0) {
                    SetPlayerActive(user, true);
                    DecreaseCharges(user.getStackInHand(hand), user, 1);
                    applyAttributeModifiers(stack, user, hand);
                    return TypedActionResult.success(user.getStackInHand(hand));
                }
            }
        }
        return TypedActionResult.fail(user.getStackInHand(hand));
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        if (!(entity instanceof PlayerEntity player)) {
            return;
        }

        if (GetPlayerActive(player)) {
            GravityComponent gravityComponent = GravityChangerAPI.getGravityComponent(player);
            if (gravityComponent != null) {
                gravityComponent.applyGravityDirectionEffect(Direction.UP, null, 100000);
            }
        }
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        tooltip.add(Text.of(""));
        tooltip.add(Text.translatable("item.modifiers.mainhand").formatted(Formatting.GRAY));
        if (GetCharges(stack) < min_charges) {
            tooltip.add(Text.translatable("attribute.modifier.equals.0",
                            " " + ItemStack.MODIFIER_FORMAT.format(10),
                            Text.translatable("attribute.name.generic.attack_damage"))
                    .formatted(Formatting.DARK_GREEN));
        } else {
            tooltip.add(Text.translatable("attribute.modifier.equals.0",
                            " " + ItemStack.MODIFIER_FORMAT.format(getDamageFromCharges(GetCharges(stack))),
                            Text.translatable("attribute.name.generic.attack_damage"))
                    .formatted(Formatting.DARK_GREEN));
        }
        tooltip.add(Text.translatable("attribute.modifier.equals.0",
                        " " + ItemStack.MODIFIER_FORMAT.format(1.4),
                        Text.translatable("attribute.name.generic.attack_speed"))
                .formatted(Formatting.DARK_GREEN));
    }

    @Override
    public void onCraft(ItemStack stack, World world, PlayerEntity player) {
        SetCharges(stack, min_charges);
        stack.addAttributeModifier(EntityAttributes.GENERIC_ATTACK_DAMAGE, new EntityAttributeModifier(ATTACK_MODIFIER_ID, "Dynamic Attack Damage", 4, EntityAttributeModifier.Operation.ADDITION), EquipmentSlot.MAINHAND);
    }

    public boolean isUnderCeiling(Entity entity) {
        ServerWorld world = (ServerWorld) entity.getWorld();
        boolean has_ceiling = world.getDimension().hasCeiling();
        int height = world.getDimension().logicalHeight() + world.getDimension().minY();
        if(has_ceiling && entity.getY() < height) {
            return true;
        }
        return false;
    }

    public static float getDamageFromCharges(int charges) {
        switch (charges) {
            case 2 -> {
                return 5.5F;
            }
            case 3 -> {
                return 7;
            }
            case 4 -> {
                return 8.5F;
            }
            case 5 -> {
                return 10;
            }
            default -> {
                return 4;
            }
        }
    }
}

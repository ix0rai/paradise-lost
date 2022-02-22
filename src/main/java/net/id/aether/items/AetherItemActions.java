package net.id.aether.items;

import net.fabricmc.fabric.api.registry.CompostingChanceRegistry;
import net.fabricmc.fabric.api.registry.FuelRegistry;
import net.id.aether.items.utils.AetherDispenserBehaviors;
import net.id.aether.items.utils.StackableVariantColorizer;
import net.minecraft.block.DispenserBlock;
import net.minecraft.item.ItemConvertible;

import static net.id.incubus_core.util.RegistryQueue.Action;
import static net.id.incubus_core.util.RegistryQueue.onClient;

class AetherItemActions {
    protected static final Action<ItemConvertible> compostable30 = compostable(0.3f);
    protected static final Action<ItemConvertible> compostable50 = compostable(0.5f);
    protected static final Action<ItemConvertible> compostable65 = compostable(0.65f);
    protected static final Action<ItemConvertible> compostable85 = compostable(0.85f);
    protected static final Action<ItemConvertible> compostable100 = compostable(1f);

    protected static final Action<ItemConvertible> emptiableBucketBehavior = (id, item) -> DispenserBlock.registerBehavior(item, AetherDispenserBehaviors.emptiableBucket);
    protected static final Action<ItemConvertible> emptyBucketBehavior = (id, item) -> DispenserBlock.registerBehavior(item, AetherDispenserBehaviors.emptyBucket);
    protected static final Action<ItemConvertible> spawnEggBehavior = (id, item) -> DispenserBlock.registerBehavior(item, AetherDispenserBehaviors.spawnEgg);

    protected static Action<ItemConvertible> fuel(int ticks) { return (id, item) -> FuelRegistry.INSTANCE.add(item, ticks);}
    protected static Action<ItemConvertible> compostable(float chance) { return (id, item) -> CompostingChanceRegistry.INSTANCE.add(item, chance); }

    protected static final Action<ItemConvertible> swetColor = onClient(new StackableVariantColorizer(0xDADADA, 0x939393, 0x4F4F4F));

}

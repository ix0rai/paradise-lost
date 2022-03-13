package net.id.aether.mixin.entity;

import dev.emi.trinkets.api.TrinketComponent;
import dev.emi.trinkets.api.TrinketsApi;
import net.id.aether.entities.AetherEntityExtensions;
import net.id.aether.entities.AetherEntityTypes;
import net.id.aether.items.tools.AetherToolMaterials;
import net.id.aether.tag.AetherItemTags;
import net.id.aether.world.dimension.AetherDimension;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.Item;
import net.minecraft.item.ToolItem;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.border.WorldBorder;
import net.minecraft.world.dimension.DimensionType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity implements AetherEntityExtensions {
    @Unique
    private int gravitatedTicks;

    @Unique
    public boolean aerbunnyFallen = false;

    @Unique
    private boolean aetherFallen = false;

    public LivingEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @SuppressWarnings("ConstantConditions")
    @ModifyVariable(
        method = "travel",
        at = @At(
            value = "INVOKE_ASSIGN",
            ordinal = 0,
            target = "Lnet/minecraft/world/World;getFluidState(Lnet/minecraft/util/math/BlockPos;)Lnet/minecraft/fluid/FluidState;"
        )
    )
    private double changeGravity(double gravity) {
        LivingEntity thiz = (LivingEntity) (Object) this;

        boolean isFalling = this.getVelocity().y <= 0.0D;
        if ((isFalling || this.isBeingGravitated()) && !thiz.hasStatusEffect(StatusEffects.SLOW_FALLING) && !isTouchingWater() && !isSneaking()) {
            // Get parachutes from trinket slots
            Optional<TrinketComponent> componentOptional = TrinketsApi.getTrinketComponent(thiz);
            boolean isWearingParachute = componentOptional.isPresent()
                    && componentOptional.get().isEquipped(e -> e.isIn(AetherItemTags.PARACHUTES));

            if (isWearingParachute) {
                gravity -= 0.07;
                this.fallDistance = 0;
            } else if (thiz.hasPassengers() && thiz.getPassengerList().stream().anyMatch(passenger ->
                    passenger.getType().equals(AetherEntityTypes.AERBUNNY))) {
                gravity *= 0.625;
                this.fallDistance = 0; // alternatively, remove & replace with fall damage dampener
            }
        }

        if (gravitatedTicks > 0) {
            gravitatedTicks--;
            this.fallDistance = 0;
            if (!this.hasNoGravity()) {
                gravity = -gravity;
            }
        }

        return gravity;
    }

    // We don't have to worry about this running twice for PlayerEntities, because it will have already returned in PlayerEntityMixin.
    // TODO let this be configurable
    @SuppressWarnings("ConstantConditions")
    @Inject(method = "damage", at = @At("HEAD"), cancellable = true)
    private void damage(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir){
        if (source.isOutOfWorld() && getY() < world.getBottomY() - 1 && world.getRegistryKey() == AetherDimension.AETHER_WORLD_KEY) {
            if (!world.isClient()) {
                this.setAetherFallen(true);
                ServerWorld overworld = getServer().getWorld(World.OVERWORLD);
                WorldBorder worldBorder = overworld.getWorldBorder();
                double xMin = Math.max(-2.9999872E7D, worldBorder.getBoundWest() + 16.0D);
                double zMin = Math.max(-2.9999872E7D, worldBorder.getBoundNorth() + 16.0D);
                double xMax = Math.min(2.9999872E7D, worldBorder.getBoundEast() - 16.0D);
                double zMax = Math.min(2.9999872E7D, worldBorder.getBoundSouth() - 16.0D);
                double scaleFactor = DimensionType.getCoordinateScaleFactor(world.getDimension(), overworld.getDimension());
                BlockPos blockPos3 = new BlockPos(MathHelper.clamp(getX() * scaleFactor, xMin, xMax), world.getTopY() + 128, MathHelper.clamp(getZ() * scaleFactor, zMin, zMax));

                this.moveToWorld(overworld);
                this.teleport(blockPos3.getX(), blockPos3.getY(), blockPos3.getZ());
                StatusEffectInstance ef = new StatusEffectInstance(StatusEffects.NAUSEA, 160, 2, false, false, true);
                ((LivingEntity) (Object) this).addStatusEffect(ef);
            }
            cir.setReturnValue(false);
            cir.cancel();
        }
    }

    @Inject(method = "damage", at = @At("RETURN"))
    private void onDamage(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        Entity attacker = source.getAttacker();
        if (cir.getReturnValue() && attacker instanceof LivingEntity) {
            Item item = ((LivingEntity) attacker).getMainHandStack().getItem();
            if (item instanceof ToolItem tool && tool.getMaterial() == AetherToolMaterials.GRAVITITE) {
                this.addVelocity(0, amount / 20 + 0.1, 0);
            }
        }
    }

    @Override
    public boolean isAetherFallen() {
        return aetherFallen;
    }

    @Override
    public void setAetherFallen(boolean aetherFallen) {
        this.aetherFallen = aetherFallen;
    }

    @Override
    public void gravitate() {
        gravitatedTicks = 20;
    }

    @Override
    public boolean isBeingGravitated() {
        return gravitatedTicks > 0;
    }

    @Override
    public boolean isAerbunnyFallen() {
        return aerbunnyFallen;
    }

    @Override
    public void setAerbunnyFallen(boolean aerbunnyFallen) {
        this.aerbunnyFallen = aerbunnyFallen;
    }
}

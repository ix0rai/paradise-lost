package net.id.aether.mixin.entity;

//import dev.emi.trinkets.api.TrinketComponent;
//import dev.emi.trinkets.api.TrinketsApi;
import net.id.aether.entities.AetherEntityExtensions;
import net.id.aether.entities.AetherEntityTypes;
import net.id.aether.items.tools.AetherToolMaterials;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.Item;
import net.minecraft.item.ToolItem;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity implements AetherEntityExtensions {
    @Unique
    private int gravitatedTicks;

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
            Optional<TrinketComponent> componentOptional = TrinketsApi.getTrinketComponent(entity);
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
    public void gravitate() {
        gravitatedTicks = 20;
    }

    @Override
    public boolean isBeingGravitated() {
        return gravitatedTicks > 0;
    }
}

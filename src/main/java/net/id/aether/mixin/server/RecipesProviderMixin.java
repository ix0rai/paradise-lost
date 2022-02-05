package net.id.aether.mixin.server;

import net.id.aether.blocks.AetherBlocks;
import net.id.aether.items.AetherItems;
import net.id.aether.tag.AetherItemTags;
import net.minecraft.data.server.RecipesProvider;
import net.minecraft.data.server.recipe.RecipeJsonProvider;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Consumer;

// TODO: Complete Recipe book implementation for PL-1.7
@Mixin(RecipesProvider.class)
public class RecipesProviderMixin {
    @Inject(method = "generate", at = @At("TAIL"))
    private static void extendRecipes(Consumer<RecipeJsonProvider> exporter, CallbackInfo ci) {
        RecipesProvider.offerPlanksRecipe2(exporter, AetherBlocks.SKYROOT_PLANKS, AetherItemTags.SKYROOT_LOGS);
        RecipesProvider.offerPlanksRecipe2(exporter, AetherBlocks.GOLDEN_OAK_PLANKS, AetherItemTags.GOLDEN_OAK_LOGS);
        RecipesProvider.offerPlanksRecipe2(exporter, AetherBlocks.CRYSTAL_PLANKS, AetherItemTags.CRYSTAL_LOGS);
        RecipesProvider.offerPlanksRecipe2(exporter, AetherBlocks.ORANGE_PLANKS, AetherItemTags.ORANGE_LOGS);
        RecipesProvider.offerPlanksRecipe2(exporter, AetherBlocks.WISTERIA_PLANKS, AetherItemTags.WISTERIA_LOGS);

        RecipesProvider.offerBarkBlockRecipe(exporter, AetherBlocks.SKYROOT_WOOD, AetherBlocks.SKYROOT_LOG);
        RecipesProvider.offerBarkBlockRecipe(exporter, AetherBlocks.GOLDEN_OAK_WOOD, AetherBlocks.GOLDEN_OAK_LOG);
        RecipesProvider.offerBarkBlockRecipe(exporter, AetherBlocks.CRYSTAL_WOOD, AetherBlocks.CRYSTAL_LOG);
        RecipesProvider.offerBarkBlockRecipe(exporter, AetherBlocks.ORANGE_WOOD, AetherBlocks.ORANGE_LOG);
        RecipesProvider.offerBarkBlockRecipe(exporter, AetherBlocks.WISTERIA_WOOD, AetherBlocks.WISTERIA_LOG);

        RecipesProvider.offerBoatRecipe(exporter, AetherItems.SKYROOT_BOAT, AetherBlocks.SKYROOT_PLANKS);
        RecipesProvider.offerBoatRecipe(exporter, AetherItems.GOLDEN_OAK_BOAT, AetherBlocks.GOLDEN_OAK_PLANKS);
        RecipesProvider.offerBoatRecipe(exporter, AetherItems.CRYSTAL_BOAT, AetherBlocks.CRYSTAL_PLANKS);
        RecipesProvider.offerBoatRecipe(exporter, AetherItems.ORANGE_BOAT, AetherBlocks.ORANGE_PLANKS);
        RecipesProvider.offerBoatRecipe(exporter, AetherItems.WISTERIA_BOAT, AetherBlocks.WISTERIA_PLANKS);

        RecipesProvider.offerSlabRecipe(exporter, AetherBlocks.SKYROOT_SLAB, AetherBlocks.SKYROOT_PLANKS);
        RecipesProvider.offerSlabRecipe(exporter, AetherBlocks.GOLDEN_OAK_SLAB, AetherBlocks.GOLDEN_OAK_PLANKS);
        RecipesProvider.offerSlabRecipe(exporter, AetherBlocks.CRYSTAL_SLAB, AetherBlocks.CRYSTAL_PLANKS);
        RecipesProvider.offerSlabRecipe(exporter, AetherBlocks.ORANGE_SLAB, AetherBlocks.ORANGE_PLANKS);
        RecipesProvider.offerSlabRecipe(exporter, AetherBlocks.WISTERIA_SLAB, AetherBlocks.WISTERIA_PLANKS);
    }
}

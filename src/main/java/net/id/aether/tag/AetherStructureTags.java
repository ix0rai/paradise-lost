package net.id.aether.tag;

import net.fabricmc.fabric.api.tag.TagFactory;
import net.id.aether.Aether;
import net.minecraft.tag.Tag;
import net.minecraft.world.biome.Biome;

public class AetherStructureTags {

    public static final Tag<Biome> ORANGE_RUIN_HAS_STRUCTURE = register("has_structure/orange_ruin");
    public static final Tag<Biome> SKYROOT_TOWER_HAS_STRUCTURE = register("has_structure/skyroot_tower");
    public static final Tag<Biome> WELL_HAS_STRUCTURE = register("has_structure/well");

    private static Tag<Biome> register(String id) {
        return TagFactory.BIOME.create(Aether.locate(id));
    }
}

package net.id.aether.entities;

public interface AetherEntityExtensions {

    boolean isBeingGravitated();

    boolean isAetherFallen();

    void setAetherFallen(boolean aetherFallen);

    boolean isAerbunnyFallen();

    void setAerbunnyFallen(boolean aerbunnyFallen);

    void gravitate();
}

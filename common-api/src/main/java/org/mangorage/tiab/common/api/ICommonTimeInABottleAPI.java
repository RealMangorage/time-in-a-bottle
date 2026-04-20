package org.mangorage.tiab.common.api;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.NotNull;
import org.mangorage.tiab.common.api.annotations.API;
import org.mangorage.tiab.common.api.impl.IStoredTimeComponent;
import org.mangorage.tiab.common.api.impl.ITimeAcceleratorEntity;

import java.util.List;
import java.util.function.Supplier;

public interface ICommonTimeInABottleAPI {

    /**
     * This is where you grab a reference to the API
     */
    @API(since = "7.0.0")
    Supplier<ICommonTimeInABottleAPI> COMMON_API = Getter.GETTER.get();

    @API(since = "7.0.0")
    LoaderSide getLoaderSide();

    @API(since = "7.0.0")
    boolean isModLoaded(String modId);

    @API(since = "7.0.0")
    ITiabRegistration getRegistration();

    /**
     * Register an {@link ITiabItemSearch} to
     * find an Time In A Bottle ItemStack
     * Only finds the first one
     */
    @API(since = "7.0.0")
    void registerItemSearch(ITiabItemSearch search);

    /**
     * Find the first Time in a bottle that this player has
     * Mods need to register an {@link ITiabItemSearch}
     * if they have a custom system for allowing
     * players to hold additional items (E.G Curios)
     *
     * @return ItemStack of the bottle. Or {@link ItemStack#EMPTY}
     */
    @API(since = "7.0.0")
    @NotNull ItemStack findTiabItem(Player player);

    @API(since = "7.0.0")
    TagKey<Block> getTagKey(); // get the un-acceleratable tag key

    @API(since = "7.0.0")
    ITiabConfig getConfig();

    @API(since = "7.0.0")
    IStoredTimeComponent createStoredTimeComponent(int stored, int total);

    @API(since = "7.0.0")
    ITimeAcceleratorEntity createEntity(ServerLevel level);

    /**
     * @return A list of {@link ITiabItemSearch}
     */
    @API(since = "7.0.0")
    List<ITiabItemSearch> getSearchHandlers();

    @API(since = "7.0.0")
    List<? extends ITimeAcceleratorEntity> getEntities(Level level, AABB aabb);

    @API(since = "7.0.0")
    String getModId();
}

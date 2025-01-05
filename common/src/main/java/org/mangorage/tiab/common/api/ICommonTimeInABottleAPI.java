package org.mangorage.tiab.common.api;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import org.mangorage.tiab.common.api.impl.IStoredTimeComponent;
import org.mangorage.tiab.common.api.impl.ITimeAcceleratorEntity;

import java.util.List;
import java.util.function.Supplier;

public interface ICommonTimeInABottleAPI {

    /**
     * This is where you grab a reference to the API
     */
    Supplier<ICommonTimeInABottleAPI> COMMON_API = Getter.GETTER.get();
    LoaderSide getLoaderSide();

    boolean isModLoaded(String modId);
    ITiabRegistration getRegistration();

    /**
     * Register an {@link ITiabItemSearch} to
     * find an Time In A Bottle ItemStack
     * Only finds the first one
     */
    void registerItemSearch(ITiabItemSearch search);

    /**
     * Find the first Time in a bottle that this player has
     * Mods need to register an {@link ITiabItemSearch}
     * if they have a custom system for allowing
     * players to hold additional items (E.G Curios)
     */
    ItemStack findTiabItem(Player player);

    TagKey<Block> getTagKey(); // get the un-acceleratable tag key
    ITiabConfig getConfig();

    IStoredTimeComponent createStoredTimeComponent(int stored, int total);
    ITimeAcceleratorEntity createEntity(ServerLevel level);

    /**
     * @return A list of {@link ITiabItemSearch}
     */
    List<ITiabItemSearch> getSearchHandlers();
    String getModId();
}

package org.mangorage.tiab.common.api;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import org.mangorage.tiab.common.TiabMod;
import org.mangorage.tiab.common.api.impl.IStoredTimeComponent;
import org.mangorage.tiab.common.api.impl.ITimeAcceleratorEntity;
import org.mangorage.tiab.common.core.LoaderSide;

import java.util.function.Supplier;

public interface ICommonTimeInABottleAPI {
    Supplier<ICommonTimeInABottleAPI> COMMON_API = TiabMod.getAPIHolder();
    LoaderSide getLoaderSide();

    boolean isModLoaded(String modId);
    ITiabRegistration getRegistration();

    void registerItemSearch(ITiabItemSearch search);
    ItemStack findTiabItem(Player player);

    TagKey<Block> getTagKey(); // get the un-acceleratable tag key
    ITiabConfig getConfig();

    IStoredTimeComponent createStoredTimeComponent(int stored, int total);
    ITimeAcceleratorEntity createEntity(ServerLevel level, EntitySpawnReason reason);
}

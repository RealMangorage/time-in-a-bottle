package org.mangorage.tiab.common;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import org.mangorage.tiab.common.api.ICommonTimeInABottleAPI;
import org.mangorage.tiab.common.api.ITiabConfig;
import org.mangorage.tiab.common.api.ITiabItemSearch;
import org.mangorage.tiab.common.commands.TiabCommands;
import org.mangorage.tiab.common.core.LoaderSide;
import org.mangorage.tiab.common.items.TiabItem;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import static org.mangorage.tiab.common.CommonConstants.MODID;

public abstract class TiabMod implements ICommonTimeInABottleAPI {
    private static final TagKey<Block> TIAB_UN_ACCELERATABLE = TagKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath(MODID, "un_acceleratable"));

    private final List<ITiabItemSearch> itemSearchList = new CopyOnWriteArrayList<>(); // To handle Concurrency
    private final LoaderSide loaderSide;
    private final ITiabConfig defaultConfig = new ITiabConfig() {};

    public TiabMod(LoaderSide loaderSide) {
        this.loaderSide = loaderSide;
        ICommonTimeInABottleAPI.COMMON_API.setValue(this);

        // Default Search
        registerItemSearch(p -> {
            for (ItemStack item : p.getInventory().items) {
                if (item.getItem() instanceof TiabItem tiabItem) {
                    return item;
                }
            }
            return null;
        });
    }

    public LoaderSide getLoaderSide() {
        return loaderSide;
    }

    protected void registerCommand(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal(MODID).then(TiabCommands.addTimeCommand).then(TiabCommands.removeTimeCommand));
    }

    @Override
    public void registerItemSearch(ITiabItemSearch search) {
        itemSearchList.add(search);
    }

    @Override
    public ItemStack findTiabItem(Player player) {
        for (ITiabItemSearch search : itemSearchList) {
            var item = search.findItem(player);
            if (item != null) {
                return item;
            }
        }
        return null;
    }

    @Override
    public TagKey<Block> getTagKey() {
        return TIAB_UN_ACCELERATABLE;
    }

    @Override
    public ITiabConfig getConfig() {
        return defaultConfig;
    }
}

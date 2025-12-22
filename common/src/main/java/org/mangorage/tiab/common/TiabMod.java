package org.mangorage.tiab.common;

import com.mojang.brigadier.CommandDispatcher;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.NotNull;
import org.mangorage.tiab.common.api.ICommonTimeInABottleAPI;
import org.mangorage.tiab.common.api.ITiabConfig;
import org.mangorage.tiab.common.api.ITiabItemSearch;
import org.mangorage.tiab.common.api.LoaderSide;
import org.mangorage.tiab.common.api.impl.IStoredTimeComponent;
import org.mangorage.tiab.common.api.impl.ITiabItem;
import org.mangorage.tiab.common.api.impl.ITimeAcceleratorEntity;
import org.mangorage.tiab.common.commands.TiabCommand;
import org.mangorage.tiab.common.core.StoredTimeComponent;
import org.mangorage.tiab.common.entities.TimeAcceleratorEntity;
import java.util.List;
import java.util.function.Supplier;

import static org.mangorage.tiab.common.CommonConstants.MODID;

public abstract class TiabMod implements ICommonTimeInABottleAPI {
    private static ICommonTimeInABottleAPI API;

    public static Supplier<ICommonTimeInABottleAPI> getAPIHolder() {
        return () -> API;
    }

    private static final TagKey<Block> TIAB_UN_ACCELERATABLE = TagKey.create(Registries.BLOCK, Identifier.fromNamespaceAndPath(MODID, "un_acceleratable"));

    private final List<ITiabItemSearch> itemSearchList = new ObjectArrayList<>();
    private final LoaderSide loaderSide;
    private final ITiabConfig defaultConfig = new ITiabConfig() {};

    public TiabMod(LoaderSide loaderSide) {
        this.loaderSide = loaderSide;
        TiabMod.API = this;

        // Default Search
        registerItemSearch(p -> {
            for (int i = 0; i < p.getInventory().getContainerSize(); i++) {
                if (p.getInventory().getItem(i).getItem() instanceof ITiabItem) {
                    return p.getInventory().getItem(i);
                }
            }
            return null;
        });
    }

    @Override
    public List<ITiabItemSearch> getSearchHandlers() {
        return List.copyOf(itemSearchList); // Copy it!
    }

    @Override
    public String getModId() {
        return MODID;
    }

    public LoaderSide getLoaderSide() {
        return loaderSide;
    }

    protected void registerCommand(CommandDispatcher<CommandSourceStack> dispatcher) {
        TiabCommand.register(dispatcher);
    }

    @Override
    public void registerItemSearch(ITiabItemSearch search) {
        itemSearchList.add(search);
    }

    @Override
    @SuppressWarnings("all")
    public @NotNull ItemStack findTiabItem(Player player) {
        for (ITiabItemSearch search : itemSearchList) {
            var item = search.findItem(player);
            if (item == null) continue; // TODO: Remove in next major version. 26.1
            if (!item.isEmpty()) return item;
        }
        return ItemStack.EMPTY;
    }

    @Override
    public TagKey<Block> getTagKey() {
        return TIAB_UN_ACCELERATABLE;
    }

    @Override
    public ITiabConfig getConfig() {
        return defaultConfig;
    }

    @Override
    public IStoredTimeComponent createStoredTimeComponent(int stored, int total) {
        return new StoredTimeComponent(stored, total);
    }

    @Override
    public ITimeAcceleratorEntity createEntity(ServerLevel level) {
        return (ITimeAcceleratorEntity) getRegistration().getAcceleratorEntityType().create(level, EntitySpawnReason.NATURAL);
    }

    @Override
    public List<? extends ITimeAcceleratorEntity> getEntities(Level level, AABB aabb) {
        return level.getEntitiesOfClass(TimeAcceleratorEntity.class, aabb);
    }

    protected void tickPlayer(Player player) {
        if (player instanceof ServerPlayer serverPlayer) {
            var server = serverPlayer.level().getServer();
            if (server != null && server.getTickCount() % 20 == 0) {
                ICommonTimeInABottleAPI.COMMON_API.get().getRegistration().getTiabItem().tickPlayer(player, 20);
            }
        }
    }
}

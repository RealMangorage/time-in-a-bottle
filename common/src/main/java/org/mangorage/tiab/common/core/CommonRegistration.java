package org.mangorage.tiab.common.core;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import org.mangorage.tiab.common.commands.TiabCommands;
import org.mangorage.tiab.common.config.ConfigHolder;
import static org.mangorage.tiab.common.CommonConstants.MODID;


// Where we register everything....
public final class CommonRegistration {
    public static final ConfigHolder SERVER_CONFIG = ConfigHolder.create();
    public static final TagKey<Block> TIAB_UN_ACCELERATABLE = TagKey.create(Registries.BLOCK, create("un_acceleratable"));

    private static ResourceLocation create(String id) {
        return ResourceLocation.fromNamespaceAndPath(MODID, id);
    }

    public static void initServer(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal(MODID).then(TiabCommands.addTimeCommand).then(TiabCommands.removeTimeCommand));
    }
}

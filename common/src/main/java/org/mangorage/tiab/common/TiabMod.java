package org.mangorage.tiab.common;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import org.mangorage.tiab.common.api.ICommonTimeInABottleAPI;
import org.mangorage.tiab.common.commands.TiabCommands;
import org.mangorage.tiab.common.core.LoaderSide;

import static org.mangorage.tiab.common.CommonConstants.MODID;

public abstract class TiabMod implements ICommonTimeInABottleAPI {

    private final LoaderSide loaderSide;

    public TiabMod(LoaderSide loaderSide) {
        this.loaderSide = loaderSide;
        ICommonTimeInABottleAPI.COMMON_API.setValue(this);
    }

    public LoaderSide getLoaderSide() {
        return loaderSide;
    }

    protected void registerCommand(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal(MODID).then(TiabCommands.addTimeCommand).then(TiabCommands.removeTimeCommand));
    }
}

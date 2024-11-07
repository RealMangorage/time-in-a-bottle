package org.mangorage.tiab.common.commands;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.MessageArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.mangorage.tiab.common.api.ICommonTimeInABottleAPI;
import org.mangorage.tiab.common.core.CommonRegistration;
import org.mangorage.tiab.common.core.StoredTimeComponent;
import org.mangorage.tiab.common.items.TiabItem;
import org.mangorage.tiab.common.misc.CommonHelper;

public class TiabCommands {
    private static final String ADD_TIME_COMMAND = "addTime";
    private static final String REMOVE_TIME_COMMAND = "removeTime";
    private static final String TIME_PARAM = "seconds";

    public static LiteralArgumentBuilder<CommandSourceStack> addTimeCommand = Commands.literal(ADD_TIME_COMMAND).requires(commandSource -> commandSource.hasPermission(2)).then(Commands.argument(TIME_PARAM, MessageArgument.message()).executes((ctx) -> {
        try {
            return processTimeCommand(ctx, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }));
    public static LiteralArgumentBuilder<CommandSourceStack> removeTimeCommand = Commands.literal(REMOVE_TIME_COMMAND).requires(commandSource -> commandSource.hasPermission(2)).then(Commands.argument(TIME_PARAM, MessageArgument.message()).executes((ctx) -> {
        try {
            return processTimeCommand(ctx, false);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return 0;
    }));

    private static int processTimeCommand(CommandContext<CommandSourceStack> ctx, boolean isAdd) throws CommandSyntaxException {
        Component messageValue = MessageArgument.getMessage(ctx, TIME_PARAM);
        CommandSourceStack source = ctx.getSource();
        ServerPlayer player = source.getPlayerOrException();

        if (!messageValue.getString().isEmpty()) {
            try {
                int timeToAdd = Integer.parseInt(messageValue.getString());

                if (timeToAdd < 0) {
                    throw new NumberFormatException();
                }
                if (timeToAdd > CommonRegistration.SERVER_CONFIG.get().MAX_STORED_TIME() / CommonRegistration.SERVER_CONFIG.get().TICKS_CONST()) {
                    timeToAdd = CommonRegistration.SERVER_CONFIG.get().MAX_STORED_TIME() / CommonRegistration.SERVER_CONFIG.get().TICKS_CONST();
                }

                boolean success = false;

                for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
                    ItemStack invStack = player.getInventory().getItem(i);
                    Item item = invStack.getItem();
                    if (item instanceof TiabItem itemTiab) {
                        int currentStoredEnergy = itemTiab.getStoredComponent(invStack).stored();

                        if (!isAdd) {
                            if (currentStoredEnergy / CommonRegistration.SERVER_CONFIG.get().TICKS_CONST() < timeToAdd) {
                                timeToAdd = currentStoredEnergy / CommonRegistration.SERVER_CONFIG.get().TICKS_CONST();
                            }
                            timeToAdd = -timeToAdd;
                        }

                        final int timeToAddFinal = timeToAdd;

                        // Check if the number becomes negative
                        if (CommonHelper.isPositive(currentStoredEnergy + timeToAddFinal * CommonRegistration.SERVER_CONFIG.get().TICKS_CONST())) {
                            CommonHelper.modify(invStack, ICommonTimeInABottleAPI.COMMON_API.getDirect().getRegistration().getStoredTime(), () -> new StoredTimeComponent(0, 0), old -> {
                                return new StoredTimeComponent(currentStoredEnergy + timeToAddFinal * CommonRegistration.SERVER_CONFIG.get().TICKS_CONST(), old.total());
                            });

                            CommonHelper.sendStatusMessage(player, String.format("%s %d seconds", isAdd ? "Added" : "Removed ", timeToAdd));
                        }

                        success = true;
                    }
                }

                if (!success) {
                    CommonHelper.sendStatusMessage(player, "No Time in a bottle item in inventory");
                }

                return 1;
            } catch (NumberFormatException ex) {
                CommonHelper.sendStatusMessage(player, "Invalid time parameter! (is the number too big?)");
            }
        } else {
            CommonHelper.sendStatusMessage(player, "Empty time parameter!");
        }
        return 0;
    }

}

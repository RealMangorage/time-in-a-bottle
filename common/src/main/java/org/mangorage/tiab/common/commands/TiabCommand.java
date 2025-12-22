package org.mangorage.tiab.common.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.permissions.PermissionSet;
import net.minecraft.server.permissions.Permissions;
import net.minecraft.world.item.Item;
import org.mangorage.tiab.common.api.ICommonTimeInABottleAPI;
import org.mangorage.tiab.common.api.impl.ITiabItem;
import org.mangorage.tiab.common.core.StoredTimeComponent;
import org.mangorage.tiab.common.misc.CommonHelper;

public final class TiabCommand {

    public static LiteralArgumentBuilder<CommandSourceStack> COMMAND = Commands.literal("tiab")
            .then(
                    Commands.literal("time")
                            .requires(commandSourceStack -> commandSourceStack.permissions().hasPermission(Permissions.COMMANDS_ADMIN))
                            .then(
                                    Commands.literal("add")
                                            .then(
                                                    Commands.argument("player", EntityArgument.player())
                                                            .then(
                                                                    Commands.argument("seconds", IntegerArgumentType.integer())
                                                                            .executes(stack -> {
                                                                                return processTime(
                                                                                        stack.getSource(),
                                                                                        EntityArgument.getPlayer(stack, "player"),
                                                                                        stack.getArgument("seconds", Integer.class),
                                                                                        true
                                                                                );
                                                                            })
                                                            )
                                            )
                            )
                            .then(
                                    Commands.literal("remove")
                                            .then(
                                                    Commands.argument("player", EntityArgument.player())
                                                            .then(
                                                                    Commands.argument("seconds", IntegerArgumentType.integer())
                                                                            .executes(stack -> {
                                                                                return processTime(
                                                                                        stack.getSource(),
                                                                                        EntityArgument.getPlayer(stack, "player"),
                                                                                        stack.getArgument("seconds", Integer.class),
                                                                                        false
                                                                                );
                                                                            })
                                                            )
                                            )
                            )

            );

    public static int processTime(CommandSourceStack stack, ServerPlayer player, int time, boolean add) {
        if (time < 0) {
            return 0; // invalid input
        }

        var cfg = ICommonTimeInABottleAPI.COMMON_API.get().getConfig();

        if (time > cfg.MAX_STORED_TIME() / cfg.TICKS_CONST()) {
            time = cfg.MAX_STORED_TIME() / cfg.TICKS_CONST();
        }

        final var itemStack = ICommonTimeInABottleAPI.COMMON_API.get().findTiabItem(player);
        if (itemStack.isEmpty()) {
            CommonHelper.sendStatusMessage(player, "No Time in a bottle item in inventory");
            stack.sendFailure(
                    Component.literal("No Time in a bottle item in inventory!")
            );
            return 0;
        }

        Item item = itemStack.getItem();

        if (item instanceof ITiabItem itemTiab) {
            int currentStoredEnergy = itemTiab.getStoredComponent(itemStack).stored();

            int timeToAdd = add ? time : -Math.min(time, currentStoredEnergy / cfg.TICKS_CONST());

            if (CommonHelper.isPositive(currentStoredEnergy + timeToAdd * cfg.TICKS_CONST())) {
                CommonHelper.modify(itemStack,
                        ICommonTimeInABottleAPI.COMMON_API.get().getRegistration().getStoredTime(),
                        new StoredTimeComponent(0, 0),
                        old -> new StoredTimeComponent(currentStoredEnergy + timeToAdd * cfg.TICKS_CONST(), old.total())
                );

                CommonHelper.sendStatusMessage(player, String.format("%s %d seconds", add ? "Added" : "Removed", Math.abs(timeToAdd)));
            }
        }

        stack.sendSuccess(
                () -> Component.literal("Time added to bottle!"), false
        );

        return 1;
    }

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(
                COMMAND
        );
    }

    TiabCommand() {}
}

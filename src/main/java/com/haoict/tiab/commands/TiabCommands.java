package com.haoict.tiab.commands;

import com.haoict.tiab.common.config.Constants;
import com.haoict.tiab.common.config.TiabConfig;
import com.haoict.tiab.common.core.api.ApiRegistry;
import com.haoict.tiab.common.core.api.interfaces.ITimeInABottleCommandAPI;
import com.haoict.tiab.common.core.api.interfaces.ITimeInABottleCommandEventAPI;
import com.haoict.tiab.common.items.TimeInABottleItem;
import com.haoict.tiab.common.utils.SendMessage;
import com.magorage.tiab.api.ITimeInABottleAPI;
import com.magorage.tiab.api.events.CommandEventFactory;
import com.magorage.tiab.api.events.TimeCommandEvent;
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
import net.minecraftforge.common.MinecraftForge;
import java.util.function.Function;

public class TiabCommands {
    private static final String ADD_TIME_COMMAND = "addTime";
    private static final String REMOVE_TIME_COMMAND = "removeTime";
    private static final String TIME_PARAM = "seconds";
    public static final Function<ServerPlayer, ItemStack> TIAB_GETTER = (plr) -> {
        ItemStack found = null;
        check: for (int i = 0; i < plr.getInventory().getContainerSize(); i++) {
            ItemStack invStack = plr.getInventory().getItem(i);
            Item item = invStack.getItem();
            if (item instanceof TimeInABottleItem) {
                found = invStack;
                break check;
            }
        }
        return found;
    };
    private static ITimeInABottleAPI API;
    private static boolean CONFIGURED_API = false;
    private static boolean registered = false;

    public static void setAPI(final ITimeInABottleAPI api) {
        if (CONFIGURED_API) return;
        CONFIGURED_API = true;
        API = api;
    }

    public static void register() {
        if (registered) return;
        registered = true;
        class Provider implements ITimeInABottleCommandAPI {
            @Override
            public int processCommand(Function<ServerPlayer, ItemStack> itemStackFunction, ServerPlayer player, String messageValue, boolean isAdd) {
                return handleCommand(itemStackFunction, player, messageValue, isAdd);
            }
        }
        var eventFactory = TimeCommandEvent.registerAPIProvider();
        class EventProvider implements ITimeInABottleCommandEventAPI {
            private final CommandEventFactory factory;
            protected EventProvider(CommandEventFactory factory) {
                this.factory = factory;
            }

            @Override
            public TimeCommandEvent createEvent(ItemStack stack, ServerPlayer player, int time, boolean isAdd) {
                return factory.createEvent(stack, player, time, isAdd);
            }
        }
        ApiRegistry.registerAccess(ITimeInABottleCommandAPI.class, new Provider());
        ApiRegistry.registerAccess(ITimeInABottleCommandEventAPI.class, new EventProvider(eventFactory));
    }

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

        return handleCommand(TIAB_GETTER, player, messageValue.getString(), isAdd);
    }

    private static int handleCommand(Function<ServerPlayer, ItemStack> itemStackFunction, ServerPlayer player, String messageValue, boolean isAdd) {
        if (!messageValue.isEmpty()) {
            try {
                ItemStack invStack = itemStackFunction.apply(player);
                if (invStack != null) {
                    int currentStoredEnergy = API.getStoredTime(invStack);

                    int timeToAdd = Integer.parseInt(messageValue);

                    if (timeToAdd < 0) {
                        throw new NumberFormatException();
                    }
                    if (timeToAdd > TiabConfig.COMMON.maxStoredTime.get() / Constants.TICK_CONST) {
                        timeToAdd = TiabConfig.COMMON.maxStoredTime.get() / Constants.TICK_CONST;
                    }

                    if (!isAdd) {
                        if (currentStoredEnergy / Constants.TICK_CONST < timeToAdd) {
                            timeToAdd = currentStoredEnergy / Constants.TICK_CONST;
                        }
                        timeToAdd = -timeToAdd;
                    }

                    if (!API.canUse()) {
                        var event = API.createEvent(invStack, player, timeToAdd, isAdd);
                        if (event != null) {
                            MinecraftForge.EVENT_BUS.post(event);
                            if (!event.isHandled())
                                SendMessage.sendStatusMessage(player, "Tiab has had its API access revoked in the config.");
                            return 1;
                        } else {
                            SendMessage.sendStatusMessage(player, "Tiab has had its API access revoked in the config. NULL event");
                            return 1;
                        }
                    } else {
                        SendMessage.sendStatusMessage(player, String.format("%s %d seconds", isAdd ? "Added" : "Removed ", timeToAdd));
                        API.setStoredTime(invStack, currentStoredEnergy + timeToAdd * Constants.TICK_CONST);
                    }
                } else {
                    SendMessage.sendStatusMessage(player, "No Time in a bottle item in inventory");
                }
                return 1;
            } catch (NumberFormatException ex) {
                SendMessage.sendStatusMessage(player, "Invalid time parameter! (is the number too big?)");
            }
        } else {
            SendMessage.sendStatusMessage(player, "Empty time parameter!");
        }
        return 0;
    }
}

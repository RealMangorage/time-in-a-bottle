package org.mangorage.tiab.common.core;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.TooltipFlag;
import org.mangorage.tiab.common.api.ICommonTimeInABottleAPI;
import org.mangorage.tiab.common.api.impl.IStoredTimeComponent;
import org.mangorage.tiab.common.misc.CommonHelper;

import java.awt.*;
import java.util.function.Consumer;

public record StoredTimeComponent(int stored, int total) implements IStoredTimeComponent {
    public static final Codec<IStoredTimeComponent> DIRECT_CODEC = RecordCodecBuilder.create(
            builder -> builder.group(
                            ExtraCodecs.NON_NEGATIVE_INT.fieldOf("stored").forGetter(IStoredTimeComponent::stored),
                            ExtraCodecs.NON_NEGATIVE_INT.fieldOf("total").forGetter(IStoredTimeComponent::total)
                    ).apply(builder, StoredTimeComponent::new)
    );

    public static final StreamCodec<RegistryFriendlyByteBuf, IStoredTimeComponent> DIRECT_STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.VAR_INT,
            IStoredTimeComponent::stored,
            ByteBufCodecs.VAR_INT,
            IStoredTimeComponent::total,
            StoredTimeComponent::new
    );

    @Override
    public void addToTooltip(Item.TooltipContext context, Consumer<Component> componentList, TooltipFlag flag, DataComponentGetter components) {
        if (components.getOrDefault(ICommonTimeInABottleAPI.COMMON_API.get().getRegistration().getCreativeComponent(), false)) {
            componentList.accept(
                    Component.translatable("item.tiab.time_in_a_bottle.tooltip.creative")
                            .withColor(Color.MAGENTA.getRGB())
            );
        } else {
            componentList.accept(
                    CommonHelper.getStoredTimeTranslated(stored)
            );
            componentList.accept(
                    CommonHelper.getTotalTimeTranslated(total)
            );
        }
    }
}

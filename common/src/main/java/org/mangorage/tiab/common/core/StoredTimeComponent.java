package org.mangorage.tiab.common.core;


import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ExtraCodecs;
import org.mangorage.tiab.common.api.impl.IStoredTimeComponent;

public record StoredTimeComponent(int stored, int total) implements IStoredTimeComponent {
    public static final Codec<IStoredTimeComponent> DIRECT_CODEC = RecordCodecBuilder.create(
            buiilder -> buiilder.group(
                            ExtraCodecs.NON_NEGATIVE_INT.fieldOf("stored").forGetter(IStoredTimeComponent::stored),
                            ExtraCodecs.NON_NEGATIVE_INT.fieldOf("total").forGetter(IStoredTimeComponent::total)
                    ).apply(buiilder, StoredTimeComponent::new)
    );
    public static final StreamCodec<RegistryFriendlyByteBuf, IStoredTimeComponent> DIRECT_STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.VAR_INT,
            IStoredTimeComponent::stored,
            ByteBufCodecs.VAR_INT,
            IStoredTimeComponent::total,
            StoredTimeComponent::new
    );
}

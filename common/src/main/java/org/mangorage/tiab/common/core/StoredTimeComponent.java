package org.mangorage.tiab.common.core;


import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ExtraCodecs;
import org.mangorage.tiab.api.common.components.IStoredTimeComponent;

public record StoredTimeComponent(int stored, int total) implements IStoredTimeComponent {
    public static final Codec<StoredTimeComponent> DIRECT_CODEC = RecordCodecBuilder.create(
            buiilder -> buiilder.group(
                            ExtraCodecs.NON_NEGATIVE_INT.fieldOf("stored").forGetter(StoredTimeComponent::stored),
                            ExtraCodecs.NON_NEGATIVE_INT.fieldOf("total").forGetter(StoredTimeComponent::total)
                    ).apply(buiilder, StoredTimeComponent::new)
    );
    public static final StreamCodec<RegistryFriendlyByteBuf, StoredTimeComponent> DIRECT_STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.VAR_INT,
            StoredTimeComponent::stored,
            ByteBufCodecs.VAR_INT,
            StoredTimeComponent::total,
            StoredTimeComponent::new
    );

    public static StoredTimeComponent create(IStoredTimeComponent iStoredTimeComponent) {
        return new StoredTimeComponent(iStoredTimeComponent.stored(), iStoredTimeComponent.total());
    }
}

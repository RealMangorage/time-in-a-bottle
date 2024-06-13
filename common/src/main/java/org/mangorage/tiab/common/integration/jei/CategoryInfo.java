package org.mangorage.tiab.common.integration.jei;

import net.minecraft.tags.TagKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.Block;

public class CategoryInfo {
    private final TagKey<Block> blockTagKey;

    public CategoryInfo(TagKey<Block> blockTagKey) {
        this.blockTagKey = blockTagKey;
    }

    public boolean is(BlockItem item) {
        return item.getBlock().defaultBlockState().is(blockTagKey);
    }

}

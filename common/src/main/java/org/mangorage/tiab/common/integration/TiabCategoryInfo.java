package org.mangorage.tiab.common.integration;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import org.mangorage.tiab.common.api.ICommonTimeInABottleAPI;

import java.util.ArrayList;
import java.util.List;

public record TiabCategoryInfo(List<Item> items, int id) {

    public static List<TiabCategoryInfo> create(final List<Item> items) {
        final List<BlockItem> blackListedItems = items.stream()
                .filter(item -> item instanceof BlockItem)
                .map(item -> (BlockItem) item)
                .filter(blockItem -> blockItem.getBlock().defaultBlockState().is(ICommonTimeInABottleAPI.COMMON_API.get().getTagKey()))
                .toList();

        ArrayList<TiabCategoryInfo> infos = new ArrayList<>();
        ArrayList<Item> currentList = new ArrayList<>();
        int id = 0;

        for (Item item : blackListedItems) {
            if (currentList.size() == 54) {
                infos.add(new TiabCategoryInfo(List.copyOf(currentList), id++));
                currentList.clear();
            }
            currentList.add(item);
        }

        if (!currentList.isEmpty())
            infos.add(new TiabCategoryInfo(List.copyOf(currentList), id));

        return infos;
    }

    public List<Item> getItems() {
        return items;
    }
}

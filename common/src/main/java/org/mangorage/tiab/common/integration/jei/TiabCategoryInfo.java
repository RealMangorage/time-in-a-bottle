package org.mangorage.tiab.common.integration.jei;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import org.mangorage.tiab.common.core.CommonRegistration;

import java.util.ArrayList;
import java.util.List;

public class TiabCategoryInfo {

    public static List<TiabCategoryInfo> create(final List<Item> items) {
        final List<BlockItem> blackListedItems = items.stream()
                .filter(item -> item instanceof BlockItem)
                .map(item -> (BlockItem) item)
                .filter(blockItem -> blockItem.getBlock().defaultBlockState().is(CommonRegistration.TIAB_UN_ACCELERATABLE))
                .toList();

        ArrayList<TiabCategoryInfo> infos = new ArrayList<>();
        ArrayList<Item> currentList = new ArrayList<>();

        for (Item item : blackListedItems) {
            if (currentList.size() == 54) {
                infos.add(new TiabCategoryInfo(List.copyOf(currentList)));
                currentList.clear();
            }
            currentList.add(item);
        }

        if (!currentList.isEmpty())
            infos.add(new TiabCategoryInfo(List.copyOf(currentList)));

        return infos;
    }

    private final List<Item> items;

    public TiabCategoryInfo(List<Item> items) {
        this.items = items;
    }

    public List<Item> getItems() {
        return items;
    }
}

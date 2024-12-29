package org.mangorage.tiab.common.integration.emi;

import dev.emi.emi.api.recipe.BasicEmiRecipe;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.widget.WidgetHolder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import org.mangorage.tiab.common.api.ICommonTimeInABottleAPI;
import org.mangorage.tiab.common.integration.TiabCategoryInfo;

public class TiabEmiRecipe extends BasicEmiRecipe {
    private final TiabCategoryInfo info;

    public TiabEmiRecipe(TiabCategoryInfo info) {
        super(TiabEmiPlugin.CATEGORY, ResourceLocation.fromNamespaceAndPath("tiab", "page" + info.getId()), 160, 130);
        this.catalysts.add(EmiIngredient.of(Ingredient.of(ICommonTimeInABottleAPI.COMMON_API.get().getRegistration().getTiabItem().asItem())));
        this.info = info;
    }

    @Override
    public void addWidgets(WidgetHolder widgets) {
        int xPos = 72;
        int startPosWidth = 0;
        int startPosHeight = 20;

        widgets.addSlot(EmiIngredient.of(Ingredient.of(ICommonTimeInABottleAPI.COMMON_API.get().getRegistration().getTiabItem().asItem())), xPos - 1, 1);

        for (Item item : info.getItems()) {
            widgets.addSlot(EmiIngredient.of(Ingredient.of(item)), startPosWidth - 1, startPosHeight);
            startPosWidth += 18;
            if (startPosWidth > 158) {
                startPosHeight += 18;
                startPosWidth = 0;
            }
        }
    }
}

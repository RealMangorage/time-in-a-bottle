package org.mangorage.tiab.common.integration.jei;

import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IJeiHelpers;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.mangorage.tiab.common.CommonConstants;
import org.mangorage.tiab.common.core.CommonRegistration;
import java.util.List;

public class Category implements IRecipeCategory<CategoryInfo> {
    public static final RecipeType<CategoryInfo> RECIPE_TYPE = RecipeType.create(CommonConstants.MODID, "resource_generator", CategoryInfo.class);

    private final IDrawable backrgound;
    private final IDrawable icon;
    private final List<BlockItem> blockItems;

    public Category(IJeiHelpers helper) {
        var gui = helper.getGuiHelper();
        this.backrgound = gui.createBlankDrawable(100, 100);
        this.icon = gui.createDrawableItemStack(CommonRegistration.TIAB_ITEM.get().getDefaultInstance());
        this.blockItems = helper.getIngredientManager()
                .getAllItemStacks()
                .stream()
                .map(ItemStack::getItem)
                .filter(item -> item instanceof BlockItem)
                .map(item -> (BlockItem) item)
                .toList();
    }


    @Override
    public RecipeType<CategoryInfo> getRecipeType() {
        return RECIPE_TYPE;
    }

    @Override
    public Component getTitle() {
        return Component.literal("Time in a bottle Blacklisted Blocks");
    }

    @Override
    public IDrawable getBackground() {
        return backrgound;
    }

    @Override
    public IDrawable getIcon() {
        return icon;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, CategoryInfo recipe, IFocusGroup focuses) {
        var stacks = blockItems.stream()
                .filter(recipe::is)
                .map(Item::getDefaultInstance)
                .toList();

        builder.addSlot(RecipeIngredientRole.INPUT, 5, 5)
                .addItemStack(CommonRegistration.TIAB_ITEM.get().getDefaultInstance());
        builder.addSlot(RecipeIngredientRole.OUTPUT, 10, 10)
                .addItemStacks(stacks);
    }
}

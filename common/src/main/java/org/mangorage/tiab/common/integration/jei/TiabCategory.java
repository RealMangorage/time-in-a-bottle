package org.mangorage.tiab.common.integration.jei;

import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IIngredientAcceptor;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.builder.IRecipeSlotBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IJeiHelpers;
import mezz.jei.api.ingredients.IIngredientType;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.category.IRecipeCategory;
import mezz.jei.api.recipe.types.IRecipeType;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.mangorage.tiab.common.api.ICommonTimeInABottleAPI;
import org.mangorage.tiab.common.CommonConstants;
import org.mangorage.tiab.common.integration.TiabCategoryInfo;

public record TiabCategory(IDrawable background, IDrawable icon, IDrawable slotBackground) implements IRecipeCategory<TiabCategoryInfo> {
    public static final IRecipeType<TiabCategoryInfo> RECIPE_TYPE = IRecipeType.create(CommonConstants.MODID, "tiab_category", TiabCategoryInfo.class);

    public static TiabCategory create(IJeiHelpers helper) {
        var gui = helper.getGuiHelper();
        return new TiabCategory(
                gui.createBlankDrawable(160, 125),
                gui.createDrawableItemStack(ICommonTimeInABottleAPI.COMMON_API.get().getRegistration().getTiabItem().asItem().getDefaultInstance()),
                gui.getSlotDrawable()
        );
    }

    @Override
    public IRecipeType<TiabCategoryInfo> getRecipeType() {
        return RECIPE_TYPE;
    }

    @Override
    public Component getTitle() {
        return Component.translatable("emi.category.tiab.test");
    }

    @Override
    public int getWidth() {
        return background.getWidth();
    }

    @Override
    public int getHeight() {
        return background.getHeight();
    }

    @Override
    public IDrawable getIcon() {
        return icon;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, TiabCategoryInfo recipe, IFocusGroup focuses) {
        int xPos = 72;
        int startPosWidth = 0;
        int startPosHeight = 20;

        IRecipeSlotBuilder inputSlotBuilder = builder.addSlot(RecipeIngredientRole.RENDER_ONLY, xPos, 1).setBackground(slotBackground, -1, -1);
        addIngredient(VanillaTypes.ITEM_STACK, ICommonTimeInABottleAPI.COMMON_API.get().getRegistration().getTiabItem().asItem().getDefaultInstance(), inputSlotBuilder);

        for (Item item : recipe.getItems()) {
            builder.addSlot(RecipeIngredientRole.OUTPUT, startPosWidth, startPosHeight).setBackground(slotBackground, -1, -1).add(VanillaTypes.ITEM_STACK, new ItemStack(item));
            startPosWidth += 18;
            if (startPosWidth > 161) {
                startPosHeight += 18;
                startPosWidth = 0;
            }
        }
    }

    private static <T> void addIngredient(IIngredientType<T> type, T ingredient, IIngredientAcceptor<?> slotBuilder) {
        slotBuilder.add(type, ingredient);
    }
}

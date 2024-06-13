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
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.mangorage.tiab.common.CommonConstants;
import org.mangorage.tiab.common.core.CommonRegistration;

public class TiabCategory implements IRecipeCategory<TiabCategoryInfo> {
    public static final RecipeType<TiabCategoryInfo> RECIPE_TYPE = RecipeType.create(CommonConstants.MODID, "resource_generator", TiabCategoryInfo.class);

    private final IDrawable backrgound;
    private final IDrawable icon;
    private final IDrawable slotBackground;

    public TiabCategory(IJeiHelpers helper) {
        var gui = helper.getGuiHelper();
        this.backrgound = gui.createBlankDrawable(160, 125);
        this.icon = gui.createDrawableItemStack(CommonRegistration.TIAB_ITEM.get().getDefaultInstance());
        this.slotBackground = gui.getSlotDrawable();
    }


    @Override
    public RecipeType<TiabCategoryInfo> getRecipeType() {
        return RECIPE_TYPE;
    }

    @Override
    public Component getTitle() {
        return Component.literal("TIAB Blacklisted Blocks");
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
    public void setRecipe(IRecipeLayoutBuilder builder, TiabCategoryInfo recipe, IFocusGroup focuses) {
        int xPos = 72;
        int startPosWidth = 0;
        int startPosHeight = 20;

        IRecipeSlotBuilder inputSlotBuilder = builder.addSlot(RecipeIngredientRole.CATALYST, xPos, 1).setBackground(slotBackground, -1, -1);
        addIngredient(VanillaTypes.ITEM_STACK, CommonRegistration.TIAB_ITEM.get().getDefaultInstance(), inputSlotBuilder);

        for (Item item : recipe.getItems()) {
            builder.addSlot(RecipeIngredientRole.OUTPUT, startPosWidth, startPosHeight).setBackground(slotBackground, -1, -1).addIngredient(VanillaTypes.ITEM_STACK, new ItemStack(item));
            startPosWidth += 18;
            if (startPosWidth > 161) {
                startPosHeight += 18;
                startPosWidth = 0;
            }
        }
    }

    private static <T> void addIngredient(IIngredientType<T> type, T ingredient, IIngredientAcceptor<?> slotBuilder) {
        slotBuilder.addIngredient(type, ingredient);
    }
}

package org.mangorage.tiab.common.integration.jei;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.ingredients.subtypes.IIngredientSubtypeInterpreter;
import mezz.jei.api.ingredients.subtypes.UidContext;
import mezz.jei.api.registration.IAdvancedRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import mezz.jei.api.registration.ISubtypeRegistration;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.mangorage.tiab.common.CommonConstants;
import org.mangorage.tiab.common.core.CommonRegistration;

import java.util.List;

@JeiPlugin
public class TiabJeiPlugin implements IModPlugin {
    private Category category;

    @Override
    public ResourceLocation getPluginUid() {
;        return new ResourceLocation(CommonConstants.MODID, "information");
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration reg) {
        category = new Category(reg.getJeiHelpers());
        reg.addRecipeCategories(category);
    }

    @Override
    public void registerAdvanced(IAdvancedRegistration reg) {
    }

    @Override
    public void registerItemSubtypes(ISubtypeRegistration reg) {
    }

    @Override
    public void registerRecipes(IRecipeRegistration reg) {
        reg.addRecipes(category.getRecipeType(), List.of(
                new CategoryInfo(CommonRegistration.TIAB_UN_ACCELERATABLE)
        ));
    }


}

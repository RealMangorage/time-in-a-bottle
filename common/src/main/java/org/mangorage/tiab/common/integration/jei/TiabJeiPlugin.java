package org.mangorage.tiab.common.integration.jei;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IAdvancedRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import mezz.jei.api.registration.ISubtypeRegistration;
import mezz.jei.api.runtime.IJeiRuntime;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.mangorage.tiab.common.CommonConstants;
import org.mangorage.tiab.common.integration.TiabCategoryInfo;

@JeiPlugin
public final class TiabJeiPlugin implements IModPlugin {
    private TiabCategory tiabCategory;

    @Override
    public ResourceLocation getPluginUid() {
        return ResourceLocation.fromNamespaceAndPath(CommonConstants.MODID, "information");
    }


    @Override
    public void registerCategories(IRecipeCategoryRegistration reg) {
        tiabCategory = TiabCategory.create(reg.getJeiHelpers());
        reg.addRecipeCategories(tiabCategory);
    }

    @Override
    public void registerRecipes(IRecipeRegistration reg) {
        tiabCategory = TiabCategory.create(reg.getJeiHelpers());
        reg.addRecipes(tiabCategory.getRecipeType(), TiabCategoryInfo.create(reg.getJeiHelpers().getIngredientManager().getAllItemStacks().stream().map(ItemStack::getItem).toList()));
    }


}

package integration.jei;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.registration.IAdvancedRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import mezz.jei.api.registration.ISubtypeRegistration;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.mangorage.tiab.common.CommonConstants;
import org.mangorage.tiab.common.integration.TiabCategoryInfo;

@JeiPlugin
public class TiabJeiPlugin implements IModPlugin {
    private TiabCategory tiabCategory;

    @Override
    public ResourceLocation getPluginUid() {
        return ResourceLocation.fromNamespaceAndPath(CommonConstants.MODID, "information");
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration reg) {
        tiabCategory = new TiabCategory(reg.getJeiHelpers());
        reg.addRecipeCategories(tiabCategory);
    }

    @Override
    public void registerAdvanced(IAdvancedRegistration reg) {
    }

    @Override
    public void registerItemSubtypes(ISubtypeRegistration reg) {
    }

    @Override
    public void registerRecipes(IRecipeRegistration reg) {
        reg.addRecipes(tiabCategory.getRecipeType(), TiabCategoryInfo.create(reg.getJeiHelpers().getIngredientManager().getAllItemStacks().stream().map(ItemStack::getItem).toList()));
    }


}

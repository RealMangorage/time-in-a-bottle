package org.mangorage.tiab.common.integration.emi;


import dev.emi.emi.api.EmiApi;
import dev.emi.emi.api.EmiEntrypoint;
import dev.emi.emi.api.EmiInitRegistry;
import dev.emi.emi.api.EmiPlugin;
import dev.emi.emi.api.EmiRegistry;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.stack.EmiStack;
import net.minecraft.resources.ResourceLocation;
import org.mangorage.tiab.common.core.CommonRegistration;
import org.mangorage.tiab.common.integration.TiabCategoryInfo;

@EmiEntrypoint
public class TiabEmiPlugin implements EmiPlugin {
    public static EmiRecipeCategory CATEGORY = new EmiRecipeCategory(new ResourceLocation("tiab", "test"), EmiStack.of(CommonRegistration.TIAB_ITEM.get()));

    @Override
    public void register(EmiRegistry emiRegistry) {
        var list = EmiApi.getIndexStacks().stream()
                .map(es -> es.getItemStack().getItem())
                .toList();
        var listInfo = TiabCategoryInfo.create(list);

        emiRegistry.addCategory(CATEGORY);

        listInfo.forEach(info -> {
            emiRegistry.addRecipe(new TiabEmiRecipe(info));
        });
    }

    @Override
    public void initialize(EmiInitRegistry registry) {
    }
}

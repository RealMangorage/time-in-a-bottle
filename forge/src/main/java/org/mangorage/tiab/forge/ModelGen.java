package org.mangorage.tiab.forge;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.mangorage.tiab.common.CommonConstants;
import org.mangorage.tiab.common.core.CommonRegistration;

import java.util.function.Supplier;

public class ModelGen extends ItemModelProvider {
    public ModelGen(PackOutput output, ExistingFileHelper helper) {
        super(output, CommonConstants.MODID, helper);
    }

    @Override
    protected void registerModels() {
        basic(CommonRegistration.TIAB_ITEM).texture("layer0", modLoc("item/time"));
    }

    private ItemModelBuilder basic(ResourceLocation name) {
        return withExistingParent(name.getPath(), mcLoc("item/generated"));
    }

    private ItemModelBuilder basic(Supplier<? extends Item> supplier) {
        Item i = supplier.get();
        return basic(BuiltInRegistries.ITEM.getKey(i));
    }
}

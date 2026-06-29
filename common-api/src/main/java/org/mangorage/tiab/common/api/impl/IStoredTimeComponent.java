package org.mangorage.tiab.common.api.impl;

import net.minecraft.world.item.component.TooltipProvider;

public interface IStoredTimeComponent extends TooltipProvider {
    int stored();
    int total();
}

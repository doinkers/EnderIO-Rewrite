package com.enderio.base.common.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.WeightedPressurePlateBlock;
import net.minecraft.world.level.block.state.properties.BlockSetType;

public class SilentWeightedPressurePlateBlock extends WeightedPressurePlateBlock {

    public SilentWeightedPressurePlateBlock(WeightedPressurePlateBlock from) {
        super(from.maxWeight, Properties.copy(from), EIOBlockSetType.SILENT);
    }

}

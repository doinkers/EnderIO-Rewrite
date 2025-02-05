package com.enderio.machines.common.blockentity.solar;

import com.enderio.api.capacitor.FixedScalable;
import com.enderio.api.io.IIOConfig;
import com.enderio.api.io.IOMode;
import com.enderio.api.io.energy.EnergyIOMode;
import com.enderio.machines.common.blockentity.base.PowerGeneratingMachineEntity;
import com.enderio.machines.common.io.SidedFixedIOConfig;
import com.enderio.machines.common.io.energy.MachineEnergyStorage;
import dev.gigaherz.graph3.Graph;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class SolarPanelBlockEntity extends PowerGeneratingMachineEntity {

    private final ISolarPanelTier tier;

    private final SolarPanelNode node;

    public SolarPanelBlockEntity(BlockEntityType<?> type, BlockPos worldPosition, BlockState blockState, ISolarPanelTier tier) {
        super(new FixedScalable(() -> (float)tier.getStorageCapacity()), new FixedScalable(() -> (float)tier.getStorageCapacity()), type, worldPosition, blockState);
        this.tier = tier;
        this.node = new SolarPanelNode(() -> energyStorage, () -> (SolarPanelEnergyStorageWrapper) getExposedEnergyStorage());
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int pContainerId, Inventory pPlayerInventory, Player pPlayer) {
        return null;
    }

    @Override
    public @Nullable MachineEnergyStorage createExposedEnergyStorage() {
        return new SolarPanelEnergyStorageWrapper(createIOConfig(), EnergyIOMode.Output, () -> tier);
    }

    @Override
    public void serverTick() {
        super.serverTick();
    }

    @Override
    public boolean isGenerating() {
        if (level == null || level.getHeight(Heightmap.Types.WORLD_SURFACE, worldPosition.getX(), worldPosition.getZ()) != worldPosition.getY() + 1)
            return false;

        return getGenerationRate() > 0;
    }

    @Override
    public int getGenerationRate() {
        int minuteInTicks = 20 * 60;
        if (level == null)
            return 0;
        int dayTime = (int) (level.getDayTime() % (minuteInTicks * 20));
        if (dayTime > minuteInTicks * 9)
            return 0;
        if (dayTime < minuteInTicks)
            return 0;
        float progress = dayTime > minuteInTicks * 5 ? 10 * minuteInTicks - dayTime : dayTime;
        progress = (progress - minuteInTicks) / (4 * minuteInTicks);
        double easing = easing(progress);
        if (level.isRaining() && !level.isThundering())
            easing -= 0.3f;
        if (level.isThundering())
            easing -= 0.7f;
        if (easing < 0)
            return 0;
        return (int) (easing * tier.getProductionRate());
    }

    @Override
    public void setRemoved() {
        if (node.getGraph() != null)
            node.getGraph().remove(node);
        super.setRemoved();
    }

    @Override
    public void onLoad() {
        super.onLoad();
        if (node.getGraph() == null)
            Graph.integrate(node, List.of());
        for (Direction direction: new Direction[] {Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST}) {
            if (level.getBlockEntity(worldPosition.relative(direction)) instanceof SolarPanelBlockEntity panel && panel.tier == tier) {
                Graph.connect(node, panel.node);
            }
        }
    }

    //Reference: EaseInOutQuad Function
    private static double easing(float progress) {
        if (progress > 0.5f)
            return 1 - Math.pow(-2*progress + 2, 2)/2;
        return 2 * progress * progress;
    }

    @Override
    protected IIOConfig createIOConfig() {
        return new SidedFixedIOConfig(dir -> dir == Direction.UP ? IOMode.NONE : IOMode.PUSH);
    }

    @Override
    public boolean canOpenMenu() {
        return false;
    }
}

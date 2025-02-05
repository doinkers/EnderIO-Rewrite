package com.enderio.machines.common.blockentity.sync;

import com.enderio.core.common.sync.EnderDataSlot;
import com.enderio.core.common.sync.SyncMode;
import com.enderio.machines.common.io.energy.IMachineEnergyStorage;
import com.enderio.machines.common.io.energy.ImmutableMachineEnergyStorage;
import net.minecraft.nbt.CompoundTag;

import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * Data slot for syncing an instance of {@link IMachineEnergyStorage} using a data slot.
 * @apiNote Sends a {@link ImmutableMachineEnergyStorage} to the receiver.
 */
public class MachineEnergyDataSlot extends EnderDataSlot<IMachineEnergyStorage> {
    public MachineEnergyDataSlot(Supplier<IMachineEnergyStorage> getter, Consumer<IMachineEnergyStorage> setter, SyncMode mode) {
        super(getter, setter, mode);
    }

    @Override
    public CompoundTag toFullNBT() {
        IMachineEnergyStorage storage = getter().get();
        CompoundTag tag = new CompoundTag();
        tag.putInt("Energy", storage.getEnergyStored());
        tag.putInt("MaxStored", storage.getMaxEnergyStored());
        tag.putInt("MaxUse", storage.getMaxEnergyUse());
        return tag;
    }

    @Override
    protected IMachineEnergyStorage fromNBT(CompoundTag nbt) {
        int energy = nbt.getInt("Energy");
        int maxStored = nbt.getInt("MaxStored");
        int maxUse = nbt.getInt("MaxUse");
        return new ImmutableMachineEnergyStorage(energy, maxStored, maxUse);
    }
}

package crazypants.enderio.base.machine.base.te;

import javax.annotation.Nonnull;

import net.minecraft.item.ItemStack;

import crazypants.enderio.api.capacitor.ICapacitorData;
import crazypants.enderio.base.power.IEnergyTank;

public interface IEnergyLogic {

    void serverTick();

    void processTasks(boolean redstoneCheck);

    int getScaledPower();

    boolean displayPower();

    boolean hasPower();

    @Nonnull
    ICapacitorData getCapacitorData();

    @Nonnull
    IEnergyTank getEnergy();

    void updateCapacitorFromSlot();

    void readCustomNBT(@Nonnull ItemStack stack);

    void writeCustomNBT(@Nonnull ItemStack stack);

    void damageCapacitor();
}

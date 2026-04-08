package com.hbm.ntm.common.menu;

import java.util.List;
import java.util.function.IntConsumer;
import java.util.function.IntSupplier;
import net.minecraft.world.inventory.ContainerData;

@SuppressWarnings("null")
public class MachineDataSlots implements ContainerData {
    private final List<IntSupplier> getters;
    private final List<IntConsumer> setters;

    public MachineDataSlots(final List<IntSupplier> getters, final List<IntConsumer> setters) {
        if (getters.size() != setters.size()) {
            throw new IllegalArgumentException("Getter/setter size mismatch");
        }
        this.getters = List.copyOf(getters);
        this.setters = List.copyOf(setters);
    }

    public static MachineDataSlots of(final IntSupplier getter, final IntConsumer setter) {
        return new MachineDataSlots(List.of(getter), List.of(setter));
    }

    @Override
    public int get(final int index) {
        return this.getters.get(index).getAsInt();
    }

    @Override
    public void set(final int index, final int value) {
        this.setters.get(index).accept(value);
    }

    @Override
    public int getCount() {
        return this.getters.size();
    }
}

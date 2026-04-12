package com.hbm.ntm.common.config;

import com.google.gson.JsonObject;
import com.google.gson.stream.JsonWriter;
import com.hbm.ntm.common.machine.IConfigurableMachine;
import java.io.IOException;

public final class IcfMachineConfig implements IConfigurableMachine {
    public static final IcfMachineConfig INSTANCE = new IcfMachineConfig();

    private int energyBuffer = 100_000_000;
    private int maxLaserPerTick = 100_000_000;
    private int coolantTankCapacity = 512_000;
    private int stellarFluxTankCapacity = 24_000;

    private IcfMachineConfig() {
    }

    @Override
    public String getConfigName() {
        return "machine_icf";
    }

    @Override
    public void readIfPresent(final JsonObject object) {
        this.energyBuffer = Math.max(1, IConfigurableMachine.grab(object, "energyBuffer", this.energyBuffer));
        this.maxLaserPerTick = Math.max(1, IConfigurableMachine.grab(object, "maxLaserPerTick", this.maxLaserPerTick));
        this.coolantTankCapacity = Math.max(1, IConfigurableMachine.grab(object, "coolantTankCapacity", this.coolantTankCapacity));
        this.stellarFluxTankCapacity = Math.max(1, IConfigurableMachine.grab(object, "stellarFluxTankCapacity", this.stellarFluxTankCapacity));
    }

    @Override
    public void writeConfig(final JsonWriter writer) throws IOException {
        writer.name("energyBuffer").value(this.energyBuffer);
        writer.name("maxLaserPerTick").value(this.maxLaserPerTick);
        writer.name("coolantTankCapacity").value(this.coolantTankCapacity);
        writer.name("stellarFluxTankCapacity").value(this.stellarFluxTankCapacity);
    }

    public int energyBuffer() {
        return this.energyBuffer;
    }

    public int maxLaserPerTick() {
        return this.maxLaserPerTick;
    }

    public int coolantTankCapacity() {
        return this.coolantTankCapacity;
    }

    public int stellarFluxTankCapacity() {
        return this.stellarFluxTankCapacity;
    }
}
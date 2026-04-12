package com.hbm.ntm.common.config;

import com.google.gson.JsonObject;
import com.google.gson.stream.JsonWriter;
import com.hbm.ntm.common.machine.IConfigurableMachine;
import java.io.IOException;

public final class PurexMachineConfig implements IConfigurableMachine {
    public static final PurexMachineConfig INSTANCE = new PurexMachineConfig();

    private int maxPower = 1_000_000;
    private int fluidTankCapacity = 24_000;
    private int baseProcessTime = 300;
    private int basePowerPerTick = 10_000;

    private PurexMachineConfig() {
    }

    @Override
    public String getConfigName() {
        return "machine_purex";
    }

    @Override
    public void readIfPresent(final JsonObject object) {
        this.maxPower = Math.max(1, IConfigurableMachine.grab(object, "maxPower", this.maxPower));
        this.fluidTankCapacity = Math.max(1, IConfigurableMachine.grab(object, "fluidTankCapacity", this.fluidTankCapacity));
        this.baseProcessTime = Math.max(1, IConfigurableMachine.grab(object, "baseProcessTime", this.baseProcessTime));
        this.basePowerPerTick = Math.max(1, IConfigurableMachine.grab(object, "basePowerPerTick", this.basePowerPerTick));
    }

    @Override
    public void writeConfig(final JsonWriter writer) throws IOException {
        writer.name("maxPower").value(this.maxPower);
        writer.name("fluidTankCapacity").value(this.fluidTankCapacity);
        writer.name("baseProcessTime").value(this.baseProcessTime);
        writer.name("basePowerPerTick").value(this.basePowerPerTick);
    }

    public int maxPower() {
        return this.maxPower;
    }

    public int fluidTankCapacity() {
        return this.fluidTankCapacity;
    }

    public int baseProcessTime() {
        return this.baseProcessTime;
    }

    public int basePowerPerTick() {
        return this.basePowerPerTick;
    }
}

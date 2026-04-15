package com.hbm.ntm.common.config;

import com.google.gson.JsonObject;
import com.google.gson.stream.JsonWriter;
import com.hbm.ntm.common.machine.IConfigurableMachine;
import java.io.IOException;

public final class CyclotronMachineConfig implements IConfigurableMachine {
    public static final CyclotronMachineConfig INSTANCE = new CyclotronMachineConfig();

    private int maxPower = 100_000_000;
    private int processDuration = 690;
    private int basePowerPerTick = 1_000_000;
    private int powerReductionPerLevel = 100_000;
    private int baseCoolantUsePerTick = 500;
    private int waterTankCapacity = 32_000;
    private int steamTankCapacity = 32_000;
    private int antimatterTankCapacity = 8_000;
    private int fluidTransferPerTick = 1_000;
    private int transferIntervalTicks = 1;

    private CyclotronMachineConfig() {
    }

    @Override
    public String getConfigName() {
        return "machine_cyclotron";
    }

    @Override
    public void readIfPresent(final JsonObject object) {
        this.maxPower = Math.max(1, IConfigurableMachine.grab(object, "maxPower", this.maxPower));
        this.processDuration = Math.max(1, IConfigurableMachine.grab(object, "processDuration", this.processDuration));
        this.basePowerPerTick = Math.max(1, IConfigurableMachine.grab(object, "basePowerPerTick", this.basePowerPerTick));
        this.powerReductionPerLevel = Math.max(0, IConfigurableMachine.grab(object, "powerReductionPerLevel", this.powerReductionPerLevel));
        this.baseCoolantUsePerTick = Math.max(1, IConfigurableMachine.grab(object, "baseCoolantUsePerTick", this.baseCoolantUsePerTick));
        this.waterTankCapacity = Math.max(1, IConfigurableMachine.grab(object, "waterTankCapacity", this.waterTankCapacity));
        this.steamTankCapacity = Math.max(1, IConfigurableMachine.grab(object, "steamTankCapacity", this.steamTankCapacity));
        this.antimatterTankCapacity = Math.max(1, IConfigurableMachine.grab(object, "antimatterTankCapacity", this.antimatterTankCapacity));
        this.fluidTransferPerTick = Math.max(1, IConfigurableMachine.grab(object, "fluidTransferPerTick", this.fluidTransferPerTick));
        this.transferIntervalTicks = Math.max(1, IConfigurableMachine.grab(object, "transferIntervalTicks", this.transferIntervalTicks));
    }

    @Override
    public void writeConfig(final JsonWriter writer) throws IOException {
        writer.name("maxPower").value(this.maxPower);
        writer.name("processDuration").value(this.processDuration);
        writer.name("basePowerPerTick").value(this.basePowerPerTick);
        writer.name("powerReductionPerLevel").value(this.powerReductionPerLevel);
        writer.name("baseCoolantUsePerTick").value(this.baseCoolantUsePerTick);
        writer.name("waterTankCapacity").value(this.waterTankCapacity);
        writer.name("steamTankCapacity").value(this.steamTankCapacity);
        writer.name("antimatterTankCapacity").value(this.antimatterTankCapacity);
        writer.name("fluidTransferPerTick").value(this.fluidTransferPerTick);
        writer.name("transferIntervalTicks").value(this.transferIntervalTicks);
    }

    public int maxPower() {
        return this.maxPower;
    }

    public int processDuration() {
        return this.processDuration;
    }

    public int basePowerPerTick() {
        return this.basePowerPerTick;
    }

    public int powerReductionPerLevel() {
        return this.powerReductionPerLevel;
    }

    public int baseCoolantUsePerTick() {
        return this.baseCoolantUsePerTick;
    }

    public int waterTankCapacity() {
        return this.waterTankCapacity;
    }

    public int steamTankCapacity() {
        return this.steamTankCapacity;
    }

    public int antimatterTankCapacity() {
        return this.antimatterTankCapacity;
    }

    public int fluidTransferPerTick() {
        return this.fluidTransferPerTick;
    }

    public int transferIntervalTicks() {
        return this.transferIntervalTicks;
    }
}

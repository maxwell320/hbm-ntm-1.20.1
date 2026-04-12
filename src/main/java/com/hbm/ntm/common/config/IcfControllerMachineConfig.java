package com.hbm.ntm.common.config;

import com.google.gson.JsonObject;
import com.google.gson.stream.JsonWriter;
import com.hbm.ntm.common.machine.IConfigurableMachine;
import java.io.IOException;

public final class IcfControllerMachineConfig implements IConfigurableMachine {
    public static final IcfControllerMachineConfig INSTANCE = new IcfControllerMachineConfig();

    private int energyBuffer = 100_000_000;
    private int capacitorPower = 2_500_000;
    private int turboPower = 5_000_000;
    private int maxBeamLength = 50;

    private IcfControllerMachineConfig() {
    }

    @Override
    public String getConfigName() {
        return "machine_icf_controller";
    }

    @Override
    public void readIfPresent(final JsonObject object) {
        this.energyBuffer = Math.max(1, IConfigurableMachine.grab(object, "energyBuffer", this.energyBuffer));
        this.capacitorPower = Math.max(1, IConfigurableMachine.grab(object, "capacitorPower", this.capacitorPower));
        this.turboPower = Math.max(1, IConfigurableMachine.grab(object, "turboPower", this.turboPower));
        this.maxBeamLength = Math.max(1, IConfigurableMachine.grab(object, "maxBeamLength", this.maxBeamLength));
    }

    @Override
    public void writeConfig(final JsonWriter writer) throws IOException {
        writer.name("energyBuffer").value(this.energyBuffer);
        writer.name("capacitorPower").value(this.capacitorPower);
        writer.name("turboPower").value(this.turboPower);
        writer.name("maxBeamLength").value(this.maxBeamLength);
    }

    public int energyBuffer() {
        return this.energyBuffer;
    }

    public int capacitorPower() {
        return this.capacitorPower;
    }

    public int turboPower() {
        return this.turboPower;
    }

    public int maxBeamLength() {
        return this.maxBeamLength;
    }
}
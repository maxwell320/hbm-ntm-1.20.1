package com.hbm.ntm.common.config;

import com.hbm.ntm.common.machine.ConfigurableMachineRegistry;

public final class MachineConfigBootstrap {
    private static boolean registered;

    private MachineConfigBootstrap() {
    }

    public static void ensureRegistered() {
        if (registered) {
            return;
        }
        registered = true;
        ConfigurableMachineRegistry.register(() -> PressMachineConfig.INSTANCE);
        ConfigurableMachineRegistry.register(() -> ShredderMachineConfig.INSTANCE);
        ConfigurableMachineRegistry.register(() -> AssemblyMachineConfig.INSTANCE);
        ConfigurableMachineRegistry.register(() -> SolderingStationMachineConfig.INSTANCE);
        ConfigurableMachineRegistry.register(() -> CentrifugeMachineConfig.INSTANCE);
        ConfigurableMachineRegistry.register(() -> GasCentrifugeMachineConfig.INSTANCE);
        ConfigurableMachineRegistry.register(() -> PurexMachineConfig.INSTANCE);
        ConfigurableMachineRegistry.register(() -> IcfMachineConfig.INSTANCE);
        ConfigurableMachineRegistry.register(() -> IcfControllerMachineConfig.INSTANCE);
    }
}


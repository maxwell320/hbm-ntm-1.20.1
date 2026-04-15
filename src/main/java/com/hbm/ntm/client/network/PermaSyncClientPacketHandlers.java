package com.hbm.ntm.client.network;

import com.hbm.ntm.client.sync.PermaSyncClientState;
import com.hbm.ntm.common.network.PermaSyncPacket;

public final class PermaSyncClientPacketHandlers {
    private PermaSyncClientPacketHandlers() {
    }

    public static void handlePermaSync(final PermaSyncPacket packet) {
        PermaSyncClientState.apply(packet.data());
    }
}

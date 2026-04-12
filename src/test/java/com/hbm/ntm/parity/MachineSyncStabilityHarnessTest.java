package com.hbm.ntm.parity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.junit.jupiter.api.Test;

class MachineSyncStabilityHarnessTest {
    private static final Path FIXTURE_PATH = Path.of("src", "test", "resources", "parity", "machine-sync-baseline.csv");

    private static final Map<String, FileExpectation> EXPECTATIONS = new LinkedHashMap<>();

    static {
        EXPECTATIONS.put("heartbeat_dedupe", expectation(
            Path.of("src", "main", "java", "com", "hbm", "ntm", "common", "block", "entity", "MachineBlockEntity.java"),
            List.of(
                "private boolean shouldSyncMachineState(final CompoundTag payload) {",
                "final boolean heartbeat = this.level.getGameTime() % 20L == 0L;",
                "this.lastSyncedMachineState == null || !this.lastSyncedMachineState.equals(payload) || heartbeat"
            )
        ));

        EXPECTATIONS.put("force_sync_path", expectation(
            Path.of("src", "main", "java", "com", "hbm", "ntm", "common", "block", "entity", "MachineBlockEntity.java"),
            List.of(
                "private void syncMachineStatePacket(final boolean force) {",
                "if (!force && !this.shouldSyncMachineState(payload)) {",
                "HbmPacketHandler.syncMachineState(this, payload);",
                "this.lastSyncedMachineState = payload.copy();"
            )
        ));

        EXPECTATIONS.put("packet_registration", expectation(
            Path.of("src", "main", "java", "com", "hbm", "ntm", "common", "network", "HbmPacketHandler.java"),
            List.of(
                "CHANNEL.registerMessage(nextId++, MachineControlPacket.class, MachineControlPacket::encode, MachineControlPacket::decode, MachineControlPacket::handle);",
                "CHANNEL.registerMessage(nextId++, MachineStateSyncPacket.class, MachineStateSyncPacket::encode, MachineStateSyncPacket::decode, MachineStateSyncPacket::handle);",
                "CHANNEL.registerMessage(nextId++, MachineStateRequestPacket.class, MachineStateRequestPacket::encode, MachineStateRequestPacket::decode,",
                "MachineStateRequestPacket::handle);"
            )
        ));

        EXPECTATIONS.put("request_guarded_sync", expectation(
            Path.of("src", "main", "java", "com", "hbm", "ntm", "common", "network", "MachineStateRequestPacket.java"),
            List.of(
                "if (!machine.canPlayerControl(player)) {",
                "if (menu instanceof MachineMenuBase<?> machineMenu && machineMenu.machine() != null && machineMenu.machine().getBlockPos().equals(packet.pos)) {",
                "machine.syncMachineStateToPlayer(player, true);"
            )
        ));

        EXPECTATIONS.put("request_sender_and_block_guards", expectation(
            Path.of("src", "main", "java", "com", "hbm", "ntm", "common", "network", "MachineStateRequestPacket.java"),
            List.of(
                "final ServerPlayer player = context.getSender();",
                "if (player == null) {",
                "final BlockEntity blockEntity = player.level().getBlockEntity(packet.pos);",
                "if (!(blockEntity instanceof MachineBlockEntity machine)) {"
            )
        ));

        EXPECTATIONS.put("control_packet_permission_gate", expectation(
            Path.of("src", "main", "java", "com", "hbm", "ntm", "common", "network", "MachineControlPacket.java"),
            List.of(
                "if (!(blockEntity instanceof IMachineControlReceiver receiver)) {",
                "if (!receiver.canPlayerControl(player)) {",
                "receiver.receiveControl(player, packet.data.copy());"
            )
        ));

        EXPECTATIONS.put("client_dispatch_wired", expectation(
            Path.of("src", "main", "java", "com", "hbm", "ntm", "client", "HbmClientSetup.java"),
            List.of(
                "HbmPacketHandler.setMachineStateClientDispatcher(MachineClientPacketHandlers::handleMachineStateSync);"
            )
        ));

        EXPECTATIONS.put("client_applies_block_and_menu", expectation(
            Path.of("src", "main", "java", "com", "hbm", "ntm", "client", "network", "MachineClientPacketHandlers.java"),
            List.of(
                "if (blockEntity instanceof IMachineStateSyncReceiver receiver) {",
                "receiver.applyMachineStateSync(packet.data().copy());",
                "if (menu instanceof final MachineMenuBase<?> machineMenu) {",
                "machineMenu.applyMachineStateSync(packet.pos(), packet.data().copy());"
            )
        ));

        EXPECTATIONS.put("client_level_guard", expectation(
            Path.of("src", "main", "java", "com", "hbm", "ntm", "client", "network", "MachineClientPacketHandlers.java"),
            List.of(
                "if (minecraft.level == null) {",
                "return;",
                "final BlockEntity blockEntity = minecraft.level.getBlockEntity(packet.pos());"
            )
        ));

        EXPECTATIONS.put("menu_pos_guard", expectation(
            Path.of("src", "main", "java", "com", "hbm", "ntm", "common", "menu", "MachineMenuBase.java"),
            List.of(
                "public final boolean applyMachineStateSync(final BlockPos pos, final CompoundTag data) {",
                "if (this.machine == null || !this.machine.getBlockPos().equals(pos)) {",
                "return false;"
            )
        ));

        EXPECTATIONS.put("menu_repair_material_hydration", expectation(
            Path.of("src", "main", "java", "com", "hbm", "ntm", "common", "menu", "MachineMenuBase.java"),
            List.of(
                "if (!data.contains(\"repairMaterials\", Tag.TAG_LIST)) {",
                "this.repairMaterials = List.of();",
                "if (listTag.isEmpty()) {",
                "this.repairMaterials = List.of();",
                "this.repairMaterials = List.copyOf(synced);"
            )
        ));

        EXPECTATIONS.put("screen_init_requests_state", expectation(
            Path.of("src", "main", "java", "com", "hbm", "ntm", "client", "screen", "MachineScreenBase.java"),
            List.of(
                "protected void init() {",
                "super.init();",
                "this.requestMachineStateFromServer();"
            )
        ));

        EXPECTATIONS.put("screen_request_guard", expectation(
            Path.of("src", "main", "java", "com", "hbm", "ntm", "client", "screen", "MachineScreenBase.java"),
            List.of(
                "protected final void requestMachineStateFromServer() {",
                "if (this.menu.machine() == null || this.minecraft == null || this.minecraft.player == null) {",
                "return;",
                "HbmPacketHandler.CHANNEL.sendToServer(new MachineStateRequestPacket(this.menu.machine().getBlockPos()));"
            )
        ));

        EXPECTATIONS.put("packet_handle_dispatch", expectation(
            Path.of("src", "main", "java", "com", "hbm", "ntm", "common", "network", "MachineStateSyncPacket.java"),
            List.of(
                "context.enqueueWork(() -> HbmPacketHandler.dispatchMachineStateOnClient(packet));",
                "context.setPacketHandled(true);"
            )
        ));

        EXPECTATIONS.put("sync_packet_payload_copy", expectation(
            Path.of("src", "main", "java", "com", "hbm", "ntm", "common", "network", "HbmPacketHandler.java"),
            List.of(
                "public static void syncMachineState(final MachineBlockEntity machine, final net.minecraft.nbt.CompoundTag data) {",
                "CHANNEL.send(PacketDistributor.TRACKING_CHUNK.with(() -> chunk), new MachineStateSyncPacket(machine.getBlockPos(), data.copy()));",
                "public static void syncMachineStateToPlayer(final MachineBlockEntity machine, final net.minecraft.nbt.CompoundTag data, final ServerPlayer player) {",
                "CHANNEL.send(PacketDistributor.PLAYER.with(() -> player), new MachineStateSyncPacket(machine.getBlockPos(), data.copy()));"
            )
        ));

        EXPECTATIONS.put("client_dispatcher_null_fallback", expectation(
            Path.of("src", "main", "java", "com", "hbm", "ntm", "common", "network", "HbmPacketHandler.java"),
            List.of(
                "private static Consumer<MachineStateSyncPacket> machineStateClientDispatcher = packet -> {",
                "public static void setMachineStateClientDispatcher(final Consumer<MachineStateSyncPacket> dispatcher) {",
                "machineStateClientDispatcher = dispatcher == null ? packet -> {",
                "} : dispatcher;"
            )
        ));
    }

    @Test
    void fixtureExists() {
        assertTrue(Files.exists(FIXTURE_PATH), "Machine sync baseline fixture must exist: " + FIXTURE_PATH);
    }

    @Test
    void fixtureHasExpectedSchemaAndRows() throws IOException {
        final var lines = Files.readAllLines(FIXTURE_PATH);
        assertFalse(lines.isEmpty(), "Fixture must contain a header row");
        assertEquals("check_id,file_ref,notes", lines.get(0), "Fixture header must match schema");

        final Set<String> fixtureIds = new LinkedHashSet<>();
        int dataRows = 0;
        for (int i = 1; i < lines.size(); i++) {
            final String line = lines.get(i).trim();
            if (line.isEmpty()) {
                continue;
            }
            final String[] cells = line.split(",", -1);
            assertEquals(3, cells.length, "Each fixture row must have 3 columns: " + line);
            assertFalse(cells[0].isBlank(), "check_id must be non-blank");
            assertFalse(cells[1].isBlank(), "file_ref must be non-blank");
            assertTrue(cells[2].contains("_"), "notes should preserve explicit check context");
            fixtureIds.add(cells[0]);
            dataRows++;
        }

        assertTrue(dataRows >= 12, "Fixture should include at least 12 seeded sync checks");
        assertEquals(EXPECTATIONS.keySet(), fixtureIds, "Fixture IDs must align with seeded sync expectations");
    }

    @Test
    void syncStabilityAnchorsExistAcrossCoreFiles() throws IOException {
        final Map<Path, String> sourceCache = new LinkedHashMap<>();

        for (final var entry : EXPECTATIONS.entrySet()) {
            final String checkId = entry.getKey();
            final FileExpectation expectation = entry.getValue();
            sourceCache.computeIfAbsent(expectation.filePath(), path -> {
                try {
                    return Files.readString(path);
                } catch (final IOException exception) {
                    throw new RuntimeException(exception);
                }
            });
            final String source = sourceCache.get(expectation.filePath());

            int cursor = 0;
            for (final String token : expectation.orderedTokens()) {
                final int foundAt = source.indexOf(token, cursor);
                assertTrue(foundAt >= 0, "Check " + checkId + " missing expected token: " + token);
                cursor = foundAt + token.length();
            }
        }
    }

    private static FileExpectation expectation(final Path filePath, final List<String> orderedTokens) {
        return new FileExpectation(filePath, orderedTokens);
    }

    private record FileExpectation(Path filePath, List<String> orderedTokens) {
    }
}

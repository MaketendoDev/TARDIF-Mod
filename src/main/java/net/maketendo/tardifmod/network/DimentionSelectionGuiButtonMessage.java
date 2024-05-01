
package net.maketendo.tardifmod.network;

import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import net.minecraft.world.level.Level;
import net.minecraft.world.entity.player.Player;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.core.BlockPos;

import net.maketendo.tardifmod.world.inventory.DimentionSelectionGuiMenu;
import net.maketendo.tardifmod.procedures.SetOverworldProcedure;
import net.maketendo.tardifmod.procedures.SetNetherProcedure;
import net.maketendo.tardifmod.procedures.SetEndProcedure;
import net.maketendo.tardifmod.procedures.SetClassicNetherProcedure;
import net.maketendo.tardifmod.TardifModMod;

import java.util.function.Supplier;
import java.util.HashMap;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class DimentionSelectionGuiButtonMessage {
	private final int buttonID, x, y, z;

	public DimentionSelectionGuiButtonMessage(FriendlyByteBuf buffer) {
		this.buttonID = buffer.readInt();
		this.x = buffer.readInt();
		this.y = buffer.readInt();
		this.z = buffer.readInt();
	}

	public DimentionSelectionGuiButtonMessage(int buttonID, int x, int y, int z) {
		this.buttonID = buttonID;
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public static void buffer(DimentionSelectionGuiButtonMessage message, FriendlyByteBuf buffer) {
		buffer.writeInt(message.buttonID);
		buffer.writeInt(message.x);
		buffer.writeInt(message.y);
		buffer.writeInt(message.z);
	}

	public static void handler(DimentionSelectionGuiButtonMessage message, Supplier<NetworkEvent.Context> contextSupplier) {
		NetworkEvent.Context context = contextSupplier.get();
		context.enqueueWork(() -> {
			Player entity = context.getSender();
			int buttonID = message.buttonID;
			int x = message.x;
			int y = message.y;
			int z = message.z;
			handleButtonAction(entity, buttonID, x, y, z);
		});
		context.setPacketHandled(true);
	}

	public static void handleButtonAction(Player entity, int buttonID, int x, int y, int z) {
		Level world = entity.level();
		HashMap guistate = DimentionSelectionGuiMenu.guistate;
		// security measure to prevent arbitrary chunk generation
		if (!world.hasChunkAt(new BlockPos(x, y, z)))
			return;
		if (buttonID == 1) {

			SetOverworldProcedure.execute(world, entity);
		}
		if (buttonID == 2) {

			SetNetherProcedure.execute(world, entity);
		}
		if (buttonID == 3) {

			SetEndProcedure.execute(world, entity);
		}
		if (buttonID == 4) {

			SetClassicNetherProcedure.execute(world, entity);
		}
	}

	@SubscribeEvent
	public static void registerMessage(FMLCommonSetupEvent event) {
		TardifModMod.addNetworkMessage(DimentionSelectionGuiButtonMessage.class, DimentionSelectionGuiButtonMessage::buffer, DimentionSelectionGuiButtonMessage::new, DimentionSelectionGuiButtonMessage::handler);
	}
}

package net.maketendo.tardifmod.network;

import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import net.minecraft.world.level.Level;
import net.minecraft.world.entity.player.Player;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.core.BlockPos;

import net.maketendo.tardifmod.world.inventory.CoordinateSelectGUIMenu;
import net.maketendo.tardifmod.procedures.SaveCoordsProcedure;
import net.maketendo.tardifmod.procedures.RotateButtonProcedure;
import net.maketendo.tardifmod.procedures.RandomiserProcedure;
import net.maketendo.tardifmod.TardifModMod;

import java.util.function.Supplier;
import java.util.HashMap;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class CoordinateSelectGUIButtonMessage {
	private final int buttonID, x, y, z;

	public CoordinateSelectGUIButtonMessage(FriendlyByteBuf buffer) {
		this.buttonID = buffer.readInt();
		this.x = buffer.readInt();
		this.y = buffer.readInt();
		this.z = buffer.readInt();
	}

	public CoordinateSelectGUIButtonMessage(int buttonID, int x, int y, int z) {
		this.buttonID = buttonID;
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public static void buffer(CoordinateSelectGUIButtonMessage message, FriendlyByteBuf buffer) {
		buffer.writeInt(message.buttonID);
		buffer.writeInt(message.x);
		buffer.writeInt(message.y);
		buffer.writeInt(message.z);
	}

	public static void handler(CoordinateSelectGUIButtonMessage message, Supplier<NetworkEvent.Context> contextSupplier) {
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
		HashMap guistate = CoordinateSelectGUIMenu.guistate;
		// security measure to prevent arbitrary chunk generation
		if (!world.hasChunkAt(new BlockPos(x, y, z)))
			return;
		if (buttonID == 0) {

			SaveCoordsProcedure.execute(world, x, y, z, entity, guistate);
		}
		if (buttonID == 1) {

			RandomiserProcedure.execute(guistate);
		}
		if (buttonID == 2) {

			RotateButtonProcedure.execute(world);
		}
	}

	@SubscribeEvent
	public static void registerMessage(FMLCommonSetupEvent event) {
		TardifModMod.addNetworkMessage(CoordinateSelectGUIButtonMessage.class, CoordinateSelectGUIButtonMessage::buffer, CoordinateSelectGUIButtonMessage::new, CoordinateSelectGUIButtonMessage::handler);
	}
}

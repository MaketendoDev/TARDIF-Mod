package net.maketendo.tardifmod.procedures;

import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.core.BlockPos;

import net.maketendo.tardifmod.network.TardifModModVariables;

public class TARDISDimPlayerEntersDimensionProcedure {
	public static void execute(LevelAccessor world) {
		if (TardifModModVariables.MapVariables.get(world).InteriorLoad == false) {
			if (world instanceof ServerLevel _serverworld) {
				StructureTemplate template = _serverworld.getStructureManager().getOrCreate(new ResourceLocation("tardif_mod", "mini_tardis_classix_interior"));
				if (template != null) {
					template.placeInWorld(_serverworld, new BlockPos(0, 10, 0), new BlockPos(0, 10, 0), new StructurePlaceSettings().setRotation(Rotation.NONE).setMirror(Mirror.NONE).setIgnoreEntities(false), _serverworld.random, 3);
				}
			}
			TardifModModVariables.MapVariables.get(world).InteriorLoad = true;
			TardifModModVariables.MapVariables.get(world).syncData(world);
			TardifModModVariables.MapVariables.get(world).Desktop = "Default Classic";
			TardifModModVariables.MapVariables.get(world).syncData(world);
		}
	}
}
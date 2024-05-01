
package net.maketendo.tardifmod.item;

import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.Item;

public class RawDalekiumItem extends Item {
	public RawDalekiumItem() {
		super(new Item.Properties().stacksTo(64).fireResistant().rarity(Rarity.UNCOMMON));
	}
}

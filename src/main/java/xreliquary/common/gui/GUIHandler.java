package xreliquary.common.gui;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;
import xreliquary.client.gui.GuiAlkahestTome;
import xreliquary.client.gui.GuiMobCharmBelt;

public class GUIHandler implements IGuiHandler {
	public static final int ALKAHESTRY_TOME = 0;
	public static final int MOB_CHARM_BELT = 1;

	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		switch (ID) {
			case ALKAHESTRY_TOME:
				return new ContainerAlkahestTome();
			case MOB_CHARM_BELT:
				return new ContainerMobCharmBelt();
		}
		return null;
	}

	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		switch(ID) {
			case ALKAHESTRY_TOME:
				return new GuiAlkahestTome(new ContainerAlkahestTome());
			case MOB_CHARM_BELT:
				return new GuiMobCharmBelt(new ContainerMobCharmBelt());
		}
		return null;
	}

}

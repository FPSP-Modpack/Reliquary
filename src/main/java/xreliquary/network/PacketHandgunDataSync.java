package xreliquary.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import xreliquary.init.ModCapabilities;
import xreliquary.items.util.handgun.IHandgunData;

public class PacketHandgunDataSync implements IMessage, IMessageHandler<PacketHandgunDataSync, IMessage> {
	private int playerSlotNumber;
	private EnumHand hand = EnumHand.MAIN_HAND;
	private short bulletCount;
	private short bulletType;
	private boolean isInCooldown;
	private long cooldownTime;

	private static final int INVALID_SLOT = -1;

	public PacketHandgunDataSync() {
	}

	public PacketHandgunDataSync(int playerSlotNumber, short bulletCount, short bulletType, boolean isInCooldown, long cooldownTime) {
		this.playerSlotNumber = playerSlotNumber;
		this.bulletCount = bulletCount;
		this.bulletType = bulletType;
		this.isInCooldown = isInCooldown;
		this.cooldownTime = cooldownTime;
	}

	public PacketHandgunDataSync(EnumHand hand, short bulletCount, short bulletType, boolean isInCooldown, long cooldownTime) {
		this.hand = hand;
		this.isInCooldown = isInCooldown;
		this.cooldownTime = cooldownTime;
		this.playerSlotNumber = INVALID_SLOT;
		this.bulletCount = bulletCount;
		this.bulletType = bulletType;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		playerSlotNumber = buf.readInt();
		hand = buf.readBoolean() ? EnumHand.MAIN_HAND : EnumHand.OFF_HAND;
		bulletCount = buf.readShort();
		bulletType = buf.readShort();
		isInCooldown = buf.readBoolean();
		cooldownTime = buf.readLong();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(playerSlotNumber);
		buf.writeBoolean(hand == EnumHand.MAIN_HAND);
		buf.writeShort(bulletCount);
		buf.writeShort(bulletType);
		buf.writeBoolean(isInCooldown);
		buf.writeLong(cooldownTime);
	}

	@SideOnly(Side.CLIENT)
	@Override
	public IMessage onMessage(PacketHandgunDataSync message, MessageContext ctx) {
		EntityPlayer player = Minecraft.getMinecraft().thePlayer;

		ItemStack stack;
		if(message.playerSlotNumber > INVALID_SLOT) {
			stack = player.inventory.getStackInSlot(message.playerSlotNumber);
		} else {
			stack = player.getHeldItem(message.hand);
		}

		if(stack != null) {
			IHandgunData data = stack.getCapability(ModCapabilities.HANDGUN_DATA_CAPABILITY, null);

			if(data != null) {
				data.setBulletCount(message.bulletCount);
				data.setBulletType(message.bulletType);
				data.setInCoolDown(message.isInCooldown);
				data.setCoolDownTime(message.cooldownTime);
			}
		}

		return null;
	}
}
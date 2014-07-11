package latmod.emcc.net;
import java.io.*;

import net.minecraft.entity.player.EntityPlayer;
import latmod.emcc.tile.*;

public class PacketButtonPressed extends PacketCondenser
{
	public static final int PACKET_ID = EMCCNetHandler.nextPacketID();
	
	public int buttonID;
	public int mouseButton;
	
	public PacketButtonPressed(int b, int mb)
	{
		super(PACKET_ID);
		buttonID = b;
		mouseButton = mb;
	}
	
	public void writePacket(TileCondenser t, DataOutputStream dos) throws Exception
	{
		dos.writeByte(buttonID);
		dos.writeByte(mouseButton);
	}
	
	public void readPacket(TileCondenser t, DataInputStream dis, EntityPlayer ep) throws Exception
	{
		buttonID = dis.readByte();
		t.handleGuiButton(true, ep, buttonID, mouseButton);
	}
}
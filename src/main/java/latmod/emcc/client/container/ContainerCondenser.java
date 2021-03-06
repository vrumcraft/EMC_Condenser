package latmod.emcc.client.container;
import latmod.emcc.tile.TileCondenser;
import latmod.ftbu.core.gui.ContainerLM;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class ContainerCondenser extends ContainerLM
{
	public ContainerCondenser(EntityPlayer ep, TileCondenser t)
	{
		super(ep, t);
		
		addSlotToContainer(new SlotCondenserTarget(t, TileCondenser.SLOT_TARGET, 8, 9));
		
		for(int i = 0; i < TileCondenser.INPUT_SLOTS.length; i++)
		{
			int x = i % 9;
			int y = i / 9;
			
			addSlotToContainer(new Slot(t, TileCondenser.INPUT_SLOTS[i], 8 + x * 18, 32 + y * 18));
		}
		
		for(int i = 0; i < TileCondenser.OUTPUT_SLOTS.length; i++)
		{
			int x = i % 9;
			int y = i / 9;
			
			addSlotToContainer(new SlotOutput(t, TileCondenser.OUTPUT_SLOTS[i], 8 + x * 18, 107 + y * 18));
		}
		
		addPlayerSlots(154);
	}
	
	@Override
	public boolean canInteractWith(EntityPlayer ep)
	{ return true; }
	
	public ItemStack transferStackInSlot(EntityPlayer ep, int i)
	{
		ItemStack is = null;
		Slot slot = (Slot)inventorySlots.get(i);
		
		if (slot != null && slot.getHasStack())
		{
			ItemStack is1 = slot.getStack();
			is = is1.copy();
			
			int maxSlot = TileCondenser.SLOT_COUNT - TileCondenser.OUTPUT_SLOTS.length;
			
			if (i < maxSlot)
			{
				if (!mergeItemStack(is1, maxSlot, inventorySlots.size(), true))
					return null;
			}
			else if (!mergeItemStack(is1, 0, maxSlot, false))
				return null;
			
			if (is1.stackSize == 0)
				slot.putStack((ItemStack)null);
			else slot.onSlotChanged();
		}
		
		return is;
	}
}
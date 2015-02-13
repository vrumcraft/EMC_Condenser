package latmod.emcc;
import latmod.core.mod.LC;
import latmod.emcc.api.IEmcStorageItem;
import net.minecraft.item.Item;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import cpw.mods.fml.common.eventhandler.*;
import cpw.mods.fml.relauncher.*;

@SideOnly(Side.CLIENT)
public class EMCCClientEventHandler
{
	public static final EMCCClientEventHandler instance = new EMCCClientEventHandler();
	
	@SubscribeEvent(priority = EventPriority.LOW)
	public void onItemTooltip(ItemTooltipEvent e)
	{
		Item item = e.itemStack.getItem();
		
		if(item instanceof IEmcStorageItem)
		{
			IEmcStorageItem i = (IEmcStorageItem)item;
			
			double stored = i.getStoredEmc(e.itemStack);
			
			String s = "";
			
			if(LC.proxy.isShiftDown())
			{
				s += stored;
				if(s.endsWith(".0")) s = s.substring(0, s.length() - 2);
			}
			else
			{
				double maxStored = i.getMaxStoredEmc(e.itemStack);
				
				if(maxStored == Double.POSITIVE_INFINITY)
				{
					s += stored;
					if(s.endsWith(".0")) s = s.substring(0, s.length() - 2);
				}
				else
				{
					s += ( ((long)(stored / maxStored * 100D * 100D)) / 100D );
					if(s.endsWith(".0")) s = s.substring(0, s.length() - 2);
					s += " %";
				}
			}
			
			e.toolTip.add(EMCC.mod.translate("storedEMC", s));
		}
		
		if(EMCCConfig.General.removeNoEMCTooltip)
		for(int j = 0; j < e.toolTip.size(); j++)
		{
			String s = e.toolTip.get(j);
			if(s != null && !s.isEmpty() && s.contains("No Exchange Energy value"))
				e.toolTip.remove(j);
		}
	}
}

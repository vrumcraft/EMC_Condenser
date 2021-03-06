package latmod.emcc.item;

import latmod.emcc.EMCCConfig;
import latmod.emcc.api.IEmcStorageItem;
import latmod.ftbu.core.ODItems;
import net.minecraft.entity.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import baubles.api.*;

public class ItemEmcBattery extends ItemEmcStorage implements IBauble
{
	public ItemEmcBattery(String s)
	{
		super(s);
	}
	
	public boolean canDischargeEmc(ItemStack is)
	{ return true; }
	
	public double getMaxStoredEmc(ItemStack is)
	{ return Double.POSITIVE_INFINITY; }
	
	public double getEmcTrasferLimit(ItemStack is)
	{ return Integer.MAX_VALUE; }
	
	public void loadRecipes()
	{
		if(EMCCConfig.Tools.enableBattery)
			mod.recipes.addRecipe(new ItemStack(this), "QRQ", "QUQ", "QGQ",
				'Q', ODItems.QUARTZ,
				'R', ODItems.REDSTONE,
				'G', ODItems.GLOWSTONE,
				'U', ItemMaterialsEMCC.ITEM_UUS);
	}
	
	public void onPostLoaded()
	{
		itemsAdded.add(new ItemStack(this, 1, 0));
		ItemStack is1 = new ItemStack(this, 1, 1);
		is1.stackTagCompound = new NBTTagCompound();
		setStoredEmc(is1, Double.POSITIVE_INFINITY);
		itemsAdded.add(is1);
	}
	
	public void onUpdate(ItemStack is, World w, Entity e, int t, boolean b)
	{ if(!w.isRemote && e instanceof EntityPlayer) onWornTick(is, (EntityPlayer)e); }

	public BaubleType getBaubleType(ItemStack is)
	{ return BaubleType.AMULET; }
	
	public void onWornTick(ItemStack is, EntityLivingBase el)
	{
		if(el.worldObj.isRemote || !(el instanceof EntityPlayer)) return;
		
		EntityPlayer ep = (EntityPlayer)el;
		
		if(is.getItemDamage() == 1 && (el.worldObj.getWorldTime() % 8 == 0))
		{
			if(!EMCCConfig.Tools.enableBattery) return;
			
			chargeInv(is, ep, ep.inventory);
			IInventory baubInv = BaublesApi.getBaubles(ep);
			if(baubInv != null) chargeInv(is, ep, baubInv);
		}
	}
	
	public void chargeInv(ItemStack is, EntityPlayer ep, IInventory inv)
	{
		double emc = getStoredEmc(is);
		
		if(emc <= 0D) return;
		
		for(int i = 0; i < inv.getSizeInventory(); i++)
		{
			ItemStack is1 = inv.getStackInSlot(i);
			
			if(is1 != null && is1.getItem() instanceof IEmcStorageItem && is1.getItem() != this)
			{
				IEmcStorageItem si = (IEmcStorageItem)is1.getItem();
				
				double max = si.getMaxStoredEmc(is1);
				
				if(max > 0D)
				{
					double siEmc = si.getStoredEmc(is1);
					
					double a = Math.min(si.getEmcTrasferLimit(is1), max - siEmc);
					
					if(a > 0D && emc >= a)
					{
						emc -= a;
						setStoredEmc(is, emc);
						si.setStoredEmc(is1, siEmc + a);
						inv.setInventorySlotContents(i, is1);
						inv.markDirty();
					}
				}
			}
		}
	}
}
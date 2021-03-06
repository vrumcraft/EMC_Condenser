package latmod.emcc.block;
import latmod.emcc.client.render.world.RenderCondenser;
import latmod.emcc.tile.TileCondenser;
import latmod.ftbu.core.tile.TileLM;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.*;

public class BlockCondenser extends BlockEMCC
{
	@SideOnly(Side.CLIENT)
	public IIcon icon_top_empty, icon_top_glow, icon_side_empty, icon_side_glow;
	
	public BlockCondenser(String s)
	{
		super(s, Material.rock);
		isBlockContainer = true;
		setResistance(100000F);
		
		mod.addTile(TileCondenser.class, s);
	}
	
	public TileLM createNewTileEntity(World w, int m)
	{ return new TileCondenser(); }
	
	public void loadRecipes()
	{
	}
	
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister ir)
	{
		icon_top_empty = ir.registerIcon(mod.assets + "cond_top_empty");
		icon_top_glow = ir.registerIcon(mod.assets + "cond_top_glow");
		icon_side_empty = ir.registerIcon(mod.assets + "cond_side_empty");
		icon_side_glow = ir.registerIcon(mod.assets + "cond_side_glow");
		blockIcon = icon_side_empty;
	}
	
	@SideOnly(Side.CLIENT)
	public int getRenderType()
	{ return RenderCondenser.instance.getRenderId(); }
	
	public boolean renderAsNormalBlock()
	{ return false; }
}
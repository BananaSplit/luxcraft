package luxcraft.block;

import buildcraft.api.core.Position;
import buildcraft.core.utils.Utils;
import buildcraft.factory.TileQuarry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Icon;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;

public class BlockLightFurnace extends Block {
    
    private Icon textureSide;
    private Icon textureTop;
    private Icon textureFront;
    
    public BlockLightFurnace(int id, Material par2Material)
    {
        super(id, par2Material);
        
        setCreativeTab(CreativeTabs.tabMisc);
        setUnlocalizedName("lightfurnace");
        setHardness(2F);
        setStepSound(Block.soundStoneFootstep);
    }
    
    @Override
    public void onBlockPlacedBy(World world, int i, int j, int k, EntityLiving entityliving, ItemStack stack) {
        super.onBlockPlacedBy(world, i, j, k, entityliving, stack);

        ForgeDirection orientation = Utils.get2dOrientation(new Position(entityliving.posX, entityliving.posY, entityliving.posZ), new Position(i, j, k));

        world.setBlockMetadataWithNotify(i, j, k, orientation.getOpposite().ordinal(),1);
    }
    
    @Override
    public Icon getIcon(int i, int j) {
        // If no metadata is set, then this is an icon.
        if (j == 0 && i == 3)
            return this.textureFront;

        if (i == j)
            return this.textureFront;

        switch (i) {
        case 1:
            return this.textureTop;
        default:
            return this.textureSide;
        }
    }
    
    @SideOnly(Side.CLIENT)
    public void registerIcons(IconRegister par1IconRegister)
    {
        this.textureSide = par1IconRegister.registerIcon("lc:lightfurnace_side");
        this.textureTop = par1IconRegister.registerIcon("lc:lightfurnace_top");
        this.textureFront = par1IconRegister.registerIcon("lc:lightfurnace_front");
    }

}

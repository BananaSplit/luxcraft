package luxcraft.block;

import java.util.Random;

import luxcraft.Luxcraft;
import luxcraft.tileentity.TileLightFurnace;
import buildcraft.api.core.Position;
import buildcraft.core.utils.Utils;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Icon;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;

public class BlockLightFurnace extends BlockContainer implements ITileEntityProvider {
    
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
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int i, float f, float g, float t){
        TileEntity tile_entity = world.getBlockTileEntity(x, y, z);

        if(tile_entity == null || player.isSneaking()){
            return false;
        }

        player.openGui(Luxcraft.instance, 0, world, x, y, z);
        return true;
    }
    
    @Override
    public void breakBlock(World world, int x, int y, int z, int i, int j){
        dropItems(world, x, y, z);
        super.breakBlock(world, x, y, z, i, j);
    }


    private void dropItems(World world, int x, int y, int z){
        Random rand = new Random();

        TileEntity tile_entity = world.getBlockTileEntity(x, y, z);

        if(!(tile_entity instanceof IInventory)){
            return;
        }

        IInventory inventory = (IInventory) tile_entity;

        for(int i = 0; i < inventory.getSizeInventory(); i++){
            ItemStack item = inventory.getStackInSlot(i);

            if(item != null && item.stackSize > 0){
                float rx = rand.nextFloat() * 0.6F + 0.1F;
                float ry = rand.nextFloat() * 0.6F + 0.1F;
                float rz = rand.nextFloat() * 0.6F + 0.1F;

                EntityItem entity_item = new EntityItem(world, x + rx, y + ry, z + rz, new ItemStack(item.itemID, item.stackSize, item.getItemDamage()));

                float factor = 0.5F;

                entity_item.motionX = rand.nextGaussian() * factor;
                entity_item.motionY = rand.nextGaussian() * factor + 0.2F;
                entity_item.motionZ = rand.nextGaussian() * factor;
                world.spawnEntityInWorld(entity_item);
                item.stackSize = 0;
            }
        }
    }
    
    @Override
    public TileEntity createNewTileEntity(World world) {
        return new TileLightFurnace();
    }
    
    //Texture Stuff
    
    @Override
    public Icon getIcon(int i, int j) {
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

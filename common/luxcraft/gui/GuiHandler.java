package luxcraft.gui;

import luxcraft.client.gui.inventory.GuiLightFurnace;
import luxcraft.inventory.ContainerLightFurnace;
import luxcraft.tileentity.TileLightFurnace;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import cpw.mods.fml.common.network.IGuiHandler;

    
    public class GuiHandler implements IGuiHandler{

        @Override
        public Object getServerGuiElement(int id, EntityPlayer player, World world, int x, int y, int z){

            TileEntity tile_entity = world.getBlockTileEntity(x, y, z);

            if(tile_entity instanceof TileLightFurnace){

                return new ContainerLightFurnace(player.inventory, (TileLightFurnace) tile_entity);
            }

            return null;
        }


        @Override
        public Object getClientGuiElement(int id, EntityPlayer player, World world, int x, int y, int z){

            TileEntity tile_entity = world.getBlockTileEntity(x, y, z);


            if(tile_entity instanceof TileLightFurnace){

                return new GuiLightFurnace(player.inventory, (TileLightFurnace) tile_entity);
            }

            return null;
        }
    }


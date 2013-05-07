package luxcraft.inventory;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.inventory.SlotFurnace;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.tileentity.TileEntityFurnace;
import luxcraft.tileentity.TileLightFurnace;

public class ContainerLightFurnace extends Container{
    
    private TileLightFurnace lightFurnace = new TileLightFurnace();
    
    private int lastEnergyStored = 0;
    private int lastCookTime = 0;
    
    public ContainerLightFurnace(InventoryPlayer player_inventory, TileLightFurnace tile_entity){
        
        lightFurnace = tile_entity;
        
        this.addSlotToContainer(new Slot(tile_entity, 0, 56, 17));
        this.addSlotToContainer(new Slot(tile_entity, 1, 56, 53));
        this.addSlotToContainer(new SlotFurnace(player_inventory.player, tile_entity, 2, 116, 35));
        
        
        bindPlayerInventory(player_inventory);

    }
    
    protected void bindPlayerInventory(InventoryPlayer player_inventory){
        
        int i = 0;
        
        for (i = 0; i < 3; ++i)
        {
            for (int j = 0; j < 9; ++j)
            {
                this.addSlotToContainer(new Slot(player_inventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }

        for (i = 0; i < 9; ++i)
        {
            this.addSlotToContainer(new Slot(player_inventory, i, 8 + i * 18, 142));
        }

    }
    
    @SideOnly(Side.CLIENT)
    public void updateProgressBar(int par1, int par2)
    {
        if (par1 == 0)
        {
            lightFurnace.setEnergyStored(par2);
        }
        if (par1 == 1)
        {
            lightFurnace.cookTime = par2;
        }

    }
    
    public void detectAndSendChanges()
    {
        super.detectAndSendChanges();

        for (int i = 0; i < this.crafters.size(); ++i)
        {
            ICrafting icrafting = (ICrafting)this.crafters.get(i);

            if (this.lastEnergyStored != (int) this.lightFurnace.getPowerProvider().getEnergyStored())
            {
                icrafting.sendProgressBarUpdate(this, 0, (int) this.lightFurnace.getPowerProvider().getEnergyStored());
            }
            
            if (this.lastCookTime != lightFurnace.cookTime);
            {
                icrafting.sendProgressBarUpdate(this, 1, lightFurnace.cookTime);
            }
            
        }

        this.lastEnergyStored = (int) this.lightFurnace.getPowerProvider().getEnergyStored();
        this.lastCookTime = lightFurnace.cookTime;
    }

    @Override
    public boolean canInteractWith(EntityPlayer entityplayer) {
        return lightFurnace.isUseableByPlayer(entityplayer);
    }
    
    public ItemStack transferStackInSlot(EntityPlayer par1EntityPlayer, int par2)
    {
        ItemStack itemstack = null;
        Slot slot = (Slot)this.inventorySlots.get(par2);

        if (slot != null && slot.getHasStack())
        {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();

            if (par2 == 2)
            {
                if (!this.mergeItemStack(itemstack1, 3, 39, true))
                {
                    return null;
                }

                slot.onSlotChange(itemstack1, itemstack);
            }
            else if (par2 != 1 && par2 != 0)
            {
                if (FurnaceRecipes.smelting().getSmeltingResult(itemstack1) != null)
                {
                    if (!this.mergeItemStack(itemstack1, 0, 1, false))
                    {
                        return null;
                    }
                }
                else if (TileEntityFurnace.isItemFuel(itemstack1))
                {
                    if (!this.mergeItemStack(itemstack1, 1, 2, false))
                    {
                        return null;
                    }
                }
                else if (par2 >= 3 && par2 < 30)
                {
                    if (!this.mergeItemStack(itemstack1, 30, 39, false))
                    {
                        return null;
                    }
                }
                else if (par2 >= 30 && par2 < 39 && !this.mergeItemStack(itemstack1, 3, 30, false))
                {
                    return null;
                }
            }
            else if (!this.mergeItemStack(itemstack1, 3, 39, false))
            {
                return null;
            }

            if (itemstack1.stackSize == 0)
            {
                slot.putStack((ItemStack)null);
            }
            else
            {
                slot.onSlotChanged();
            }

            if (itemstack1.stackSize == itemstack.stackSize)
            {
                return null;
            }

            slot.onPickupFromSlot(par1EntityPlayer, itemstack1);
        }

        return itemstack;
    }
}

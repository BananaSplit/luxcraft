package luxcraft.tileentity;

import buildcraft.api.power.IPowerProvider;
import buildcraft.api.power.IPowerReceptor;
import buildcraft.api.power.PowerFramework;
import buildcraft.api.transport.IPipeConnection;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.ForgeDirection;

public class TileLightFurnace extends TileEntity implements IPowerReceptor, IInventory, IPipeConnection {
   
    IPowerProvider powerProvider;
    
    private ItemStack[] inventory;
    
    boolean isActive = true;
    public int cookTime = 0;
    
    public TileLightFurnace() {
        
        powerProvider = PowerFramework.currentFramework.createPowerProvider();
        powerProvider.configure(20, 1, 25, 25, 5000);
        
        this.inventory = new ItemStack[3];
        
    }

    @Override
    public void setPowerProvider(IPowerProvider provider) {
        powerProvider = provider;
        
    }

    @Override
    public IPowerProvider getPowerProvider() {
        return powerProvider;
    }

    @Override
    public void doWork() {
        
        //Nothing
    }

    @Override
    public int powerRequest(ForgeDirection from) {
        if (isActive())
            return (int) Math.ceil(Math.min(getPowerProvider().getMaxEnergyReceived(), getPowerProvider().getMaxEnergyStored()
                    - getPowerProvider().getEnergyStored()));
        else
            return 0;
    }
    
    public boolean isActive()
    {
        return isActive;
    }
    
    public void updateEntity()
    {
        boolean flag1 = false;
 
        if (!this.worldObj.isRemote)
        {
         
            if (this.canSmelt() && this.getPowerProvider().getEnergyStored() > 0)
            {
                this.getPowerProvider().useEnergy(1, 1, true);
                this.cookTime++;
                
                if (cookTime >= 160)
                {
                    this.smeltItem();
                    cookTime = 0;
                }

            }
            else
            {
                cookTime = 0;
            }

        }

        if (flag1)
        {
            this.onInventoryChanged();
        }
    }
    
    /**
     * Returns true if the furnace can smelt an item, i.e. has a source item, destination stack isn't full, etc.
     */
    private boolean canSmelt()
    {
        if (this.inventory[0] == null)
        {
            return false;
        }
        else
        {
            ItemStack itemstack = FurnaceRecipes.smelting().getSmeltingResult(this.inventory[0]);
            if (itemstack == null) return false;
            if (this.inventory[2] == null) return true;
            if (!this.inventory[2].isItemEqual(itemstack)) return false;
            int result = inventory[2].stackSize + itemstack.stackSize;
            return (result <= getInventoryStackLimit() && result <= itemstack.getMaxStackSize());
        }
    }
    
    /**
     * Turn one item from the furnace source stack into the appropriate smelted item in the furnace result stack
     */
    public void smeltItem()
    {
        if (this.canSmelt())
        {
            ItemStack itemstack = FurnaceRecipes.smelting().getSmeltingResult(this.inventory[0]);

            if (this.inventory[2] == null)
            {
                this.inventory[2] = itemstack.copy();
            }
            else if (this.inventory[2].isItemEqual(itemstack))
            {
                inventory[2].stackSize += itemstack.stackSize;
            }

            --this.inventory[0].stackSize;

            if (this.inventory[0].stackSize <= 0)
            {
                this.inventory[0] = null;
            }
        }
    }


    @Override
    public int getSizeInventory() {
        return this.inventory.length;
    }

    @Override
    public ItemStack getStackInSlot(int i) {
        return this.inventory[i];
    }

    @Override
    public ItemStack decrStackSize(int slotIndex, int amount) {
        ItemStack stack = getStackInSlot(slotIndex);
        
        if(stack != null){
       
                if(stack.stackSize <= amount){
                        setInventorySlotContents(slotIndex, null);
                }
                else{
                        stack = stack.splitStack(amount);
                        if(stack.stackSize == 0){
                                setInventorySlotContents(slotIndex, null);
                        }
                }
        }


        return stack;
    }

    @Override
    public ItemStack getStackInSlotOnClosing(int slotIndex) {
        ItemStack stack = getStackInSlot(slotIndex);
        
        
        if(stack != null){
                setInventorySlotContents(slotIndex, null);
        }
       
       
        return stack;
    }

    @Override
    public void setInventorySlotContents(int slot, ItemStack stack) {
        this.inventory[slot] = stack;
        
        if(stack != null && stack.stackSize > getInventoryStackLimit()){
                stack.stackSize = getInventoryStackLimit();
                
        }       
    }

    @Override
    public String getInvName() {
        return "lightfurnace";
    }

    @Override
    public boolean isInvNameLocalized() {
        return true;
    }

    @Override
    public int getInventoryStackLimit() {
        return 64;
    }

    @Override
    public boolean isUseableByPlayer(EntityPlayer player){
            return worldObj.getBlockTileEntity(xCoord, yCoord, zCoord) == this && player.getDistanceSq(xCoord + 0.5, yCoord + 0.5, zCoord + 0.5) < 64;
    }

    @Override
    public void openChest() {
        
    }

    @Override
    public void closeChest() {
        
    }

    @Override
    public boolean isStackValidForSlot(int i, ItemStack itemstack) {
        return true;
    }

    public int getBurnTimeRemainingScaled(int i) {
        
        int j = (int) getPowerProvider().getEnergyStored() * i / getPowerProvider().getMaxEnergyStored();     
        return j;
    }
    
    public int getCookProgressScaled(int i) {
        return this.cookTime * i / 160;
    }
    
    public void setEnergyStored(int par1) {  
        float caca = this.getPowerProvider().getEnergyStored() - par1; 
        this.getPowerProvider().useEnergy(caca, caca, true);   
    }
    
    @Override
    public boolean isPipeConnected(ForgeDirection with) {
        return true;
    }
    
    /**
     * Reads a tile entity from NBT.
     */
    public void readFromNBT(NBTTagCompound par1NBTTagCompound)
    {
        super.readFromNBT(par1NBTTagCompound);
        NBTTagList nbttaglist = par1NBTTagCompound.getTagList("Items");
        this.inventory = new ItemStack[this.getSizeInventory()];

        for (int i = 0; i < nbttaglist.tagCount(); ++i)
        {
            NBTTagCompound nbttagcompound1 = (NBTTagCompound)nbttaglist.tagAt(i);
            byte b0 = nbttagcompound1.getByte("Slot");

            if (b0 >= 0 && b0 < this.inventory.length)
            {
                this.inventory[b0] = ItemStack.loadItemStackFromNBT(nbttagcompound1);
            }
        }
        
        PowerFramework.currentFramework.loadPowerProvider(this, par1NBTTagCompound);
        powerProvider.configure(20, 1, 25, 25, 5000);
    }

    /**
     * Writes a tile entity to NBT.
     */
    public void writeToNBT(NBTTagCompound par1NBTTagCompound)
    {
        super.writeToNBT(par1NBTTagCompound);
        
        PowerFramework.currentFramework.savePowerProvider(this, par1NBTTagCompound);
        
        NBTTagList nbttaglist = new NBTTagList();

        for (int i = 0; i < this.inventory.length; ++i)
        {
            if (this.inventory[i] != null)
            {
                NBTTagCompound nbttagcompound1 = new NBTTagCompound();
                nbttagcompound1.setByte("Slot", (byte)i);
                this.inventory[i].writeToNBT(nbttagcompound1);
                nbttaglist.appendTag(nbttagcompound1);
            }
        }

        par1NBTTagCompound.setTag("Items", nbttaglist);
    }

}

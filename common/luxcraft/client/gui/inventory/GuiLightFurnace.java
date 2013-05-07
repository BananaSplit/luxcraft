package luxcraft.client.gui.inventory;

import luxcraft.inventory.ContainerLightFurnace;
import luxcraft.tileentity.TileLightFurnace;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.StatCollector;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiLightFurnace extends GuiContainer {

    private TileLightFurnace lightFurnaceInventory;

    public GuiLightFurnace(InventoryPlayer par1InventoryPlayer,
            TileLightFurnace par2TileEntityFurnace) {
        super(new ContainerLightFurnace(par1InventoryPlayer,
                par2TileEntityFurnace));
        lightFurnaceInventory = par2TileEntityFurnace;
    }

    /**
     * Draw the foreground layer for the GuiContainer (everything in front of
     * the items)
     */
    @Override
    protected void drawGuiContainerForegroundLayer(int par1, int par2) {
        
        fontRenderer.drawString("Light Furnace", 6, 6, 0x000000);
        
        fontRenderer.drawString(String.valueOf((int) lightFurnaceInventory.getPowerProvider().getEnergyStored()) + " MJ", 6, 40, 0x000000);
        fontRenderer.drawString(StatCollector.translateToLocal("container.inventory"), 6, ySize - 96 , 0x000000);
        
    }

    /**
     * Draw the background layer for the GuiContainer (everything behind the
     * items)
     */
    @Override
    protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3) {
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        mc.renderEngine.bindTexture("/mods/lc/textures/gui/lightfurnace.png");
        int k = (width - xSize) / 2;
        int l = (height - ySize) / 2;
        this.drawTexturedModalRect(k, l, 0, 0, xSize, ySize);
        
        int i1 = 0;

        i1 = this.lightFurnaceInventory.getBurnTimeRemainingScaled(12);
        this.drawTexturedModalRect(k + 56, l + 36 + 12 - i1, 176, 12 - i1, 14, i1 + 2);
        
        i1 = this.lightFurnaceInventory.getCookProgressScaled(24);
        this.drawTexturedModalRect(k + 79, l + 34, 176, 14, i1 + 1, 16);
    }

}

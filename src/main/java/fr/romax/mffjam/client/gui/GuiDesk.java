package fr.romax.mffjam.client.gui;

import fr.romax.mffjam.MFFJam;
import fr.romax.mffjam.common.blocks.TileEntityDesk;
import fr.romax.mffjam.common.inventory.ContainerDesk;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;

public class GuiDesk extends GuiContainer
{
	private static final ResourceLocation BACKGROUND = new ResourceLocation(MFFJam.MODID, "textures/gui/container/desk_gui.png");
	private final TileEntityDesk desk;
	private final String inventoryName;

	public GuiDesk(TileEntityDesk desk, InventoryPlayer playerInv)
	{
		super(new ContainerDesk(desk, playerInv));
		this.desk = desk;
		this.ySize = 254;
		this.xSize = 256;
		this.inventoryName = playerInv.getDisplayName().getUnformattedText();
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
	{
		this.drawDefaultBackground();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.getTextureManager().bindTexture(BACKGROUND);
		this.drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);
	}
	
	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
	{
        this.fontRenderer.drawString(this.inventoryName, 48, this.ySize - 93, 4210752);
    }
	
}

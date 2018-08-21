package fr.romax.mffjam.client.gui;

import java.io.IOException;

import fr.romax.mffjam.MFFJam;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiReadMessage extends GuiScreen
{
	public static final ResourceLocation DESK_ICONS = new ResourceLocation(MFFJam.MODID, "textures/gui/container/desk_gui_icons.png");
	public static final int PAPER_ICON_WIDTH = 98, PAPER_ICON_HEIGHT = 115, BORDER = 5;
	
	
	private String pageContent = "";
	
	/** The GuiButton to sign this page. */
	private GuiButton buttonCancel;
	
	public GuiReadMessage(String content)
	{
		this.pageContent = content;
	}
	
	@Override
	public void initGui()
	{
		super.initGui();
		this.buttonCancel = this.addButton(new GuiButton(1, this.width / 2 - 49, (this.height + PAPER_ICON_HEIGHT) / 2 + 20, 98, 20, I18n.format("gui.cancel")));
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks)
	{
		this.drawDefaultBackground();
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		
		this.mc.getTextureManager().bindTexture(DESK_ICONS);
		int x = (this.width - PAPER_ICON_WIDTH) / 2;
		int y = (this.height - PAPER_ICON_HEIGHT) / 2;
		this.drawTexturedModalRect(x, y + 16, 0, 0, PAPER_ICON_WIDTH, PAPER_ICON_HEIGHT);
		
		String displayText = this.pageContent;
		
		this.fontRenderer.drawSplitString(displayText, x + BORDER, y + BORDER + 19, PAPER_ICON_WIDTH - 2 * BORDER, 0);
		
		super.drawScreen(mouseX, mouseY, partialTicks);
	}
	
	@Override
	protected void actionPerformed(GuiButton button) throws IOException
	{
		if (button == this.buttonCancel)
		{
			this.mc.displayGuiScreen(null);
		}
	}
	
	@Override
	public boolean doesGuiPauseGame()
	{
		return false;
	}
	
}

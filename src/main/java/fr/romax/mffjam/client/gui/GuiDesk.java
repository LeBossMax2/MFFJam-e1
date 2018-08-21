package fr.romax.mffjam.client.gui;

import java.io.IOException;

import fr.romax.mffjam.MFFJam;
import fr.romax.mffjam.common.ModNetwork;
import fr.romax.mffjam.common.blocks.TileEntityDesk;
import fr.romax.mffjam.common.inventory.ContainerDesk;
import fr.romax.mffjam.common.network.MessageWritePage;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ChatAllowedCharacters;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiDesk extends GuiContainer
{
	
	private static final ResourceLocation BACKGROUND = new ResourceLocation(MFFJam.MODID, "textures/gui/container/desk_gui.png");
	private static final ResourceLocation ICONS = new ResourceLocation(MFFJam.MODID, "textures/gui/container/desk_gui_icons.png");
	
	private static final int PAPER_ICON_WIDTH = 98, PAPER_ICON_HEIGHT = 115, BORDER = 5;
	private final TileEntityDesk desk;
	private final String inventoryName;
	
	/** Update ticks since the gui was opened */
	private int updateCount;
	private boolean isListening = false;
	/** Determines if the signing screen is open */
	private boolean gettingSigned;
	private String title = "";
	private String signedName;
	private String pageContent = "";
	
	/** The GuiButton to sign this page. */
	private GuiButton buttonSign;
    private GuiButton buttonCancel;
	
	public GuiDesk(TileEntityDesk desk, InventoryPlayer playerInv)
	{
		super(new ContainerDesk(desk, playerInv));
		this.desk = desk;
		this.ySize = 254;
		this.xSize = 256;
		this.inventoryName = playerInv.getDisplayName().getUnformattedText();
		this.signedName = playerInv.player.getName();
	}
	
	@Override
	public void initGui()
	{
		super.initGui();
		this.buttonSign = this.addButton(new GuiButton(0, this.width / 2 - 100, this.guiTop + 136, 98, 20, I18n.format("book.signButton")));
		this.buttonCancel = this.addButton(new GuiButton(1, this.width / 2 + 2, this.guiTop + 136, 98, 20, I18n.format("gui.cancel")));
	}
	
	@Override
	public void updateScreen()
	{
		super.updateScreen();
		++this.updateCount;
	}
	
	protected boolean hasPaper()
	{
		return this.inventorySlots.getSlot(0).getHasStack();
	}
	
	protected void updateState()
	{
		boolean hasPaper = this.hasPaper();
		
		this.buttonSign.visible = hasPaper;
		this.buttonSign.enabled = !this.pageContent.isEmpty() && (!this.gettingSigned || !this.title.isEmpty() );
		this.buttonCancel.visible = this.gettingSigned && hasPaper;
	}
	
	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException
	{
		int x = this.guiLeft + (this.xSize - PAPER_ICON_WIDTH) / 2;
		
		this.drawTexturedModalRect(x, this.guiTop + 16, 0, 0, PAPER_ICON_WIDTH, PAPER_ICON_HEIGHT);
		if (mouseX > x && mouseX <= x + PAPER_ICON_WIDTH && mouseY > this.guiTop + 16 && mouseY <= this.guiTop + 16 + PAPER_ICON_HEIGHT)
		{
			this.isListening = true;
		}
		else
		{
			this.isListening = false;
			super.mouseClicked(mouseX, mouseY, mouseButton);
		}
	}
	
	@Override
	protected void keyTyped(char typedChar, int keyCode) throws IOException
	{
		if (this.isListening)
		{
			if (keyCode == 1)
			{
				if (this.gettingSigned)
				{
					this.gettingSigned = false;
				}
				else
				{
					this.isListening = false;
				}
			}
			else
			{
				if (this.gettingSigned)
				{
					switch (keyCode)
					{
					case 14:
						
						if (!this.title.isEmpty())
						{
							this.title = this.title.substring(0, this.title.length() - 1);
							this.updateState();
						}
						return;
					case 28:
					case 156:
						
						if (!this.title.isEmpty())
						{
							this.sendPageToServer();
						}
						
						return;
					default:
						
						if (this.title.length() < 16 && ChatAllowedCharacters.isAllowedCharacter(typedChar))
						{
							this.title = this.title + Character.toString(typedChar);
							this.updateState();
						}
					}
				}
				else
				{
					if (GuiScreen.isKeyComboCtrlV(keyCode))
					{
						this.pageInsertIntoCurrent(GuiScreen.getClipboardString());
					}
					else
					{
						switch (keyCode)
						{
						case 14:
							if (!this.pageContent.isEmpty())
							{
								this.pageContent = this.pageContent.substring(0, this.pageContent.length() - 1);
							}
							
							return;
						case 28:
						case 156:
							this.pageInsertIntoCurrent("\n");
							return;
						default:
							
							if (ChatAllowedCharacters.isAllowedCharacter(typedChar))
							{
								this.pageInsertIntoCurrent(Character.toString(typedChar));
							}
						}
					}
				}
			}
		}
		else
		{
			super.keyTyped(typedChar, keyCode);
		}
	}
	
	private void pageInsertIntoCurrent(String toInsert)
	{
		String newContent = this.pageContent + toInsert;
		int i = this.fontRenderer.getWordWrappedHeight(newContent + "" + TextFormatting.BLACK + "_", PAPER_ICON_WIDTH - 2 * BORDER);
		
		if (i <= PAPER_ICON_HEIGHT - 2 * BORDER && newContent.length() < 512)
		{
			this.pageContent = newContent;
		}
	}
	
	private void sendPageToServer()
	{
		ModNetwork.MOD_CHANNEL.sendToServer(new MessageWritePage(this.inventorySlots.windowId, this.pageContent, this.title));
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks)
	{
		this.updateState();
		super.drawScreen(mouseX, mouseY, partialTicks);
		this.renderHoveredToolTip(mouseX, mouseY);
	}
	
	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
	{
		this.drawDefaultBackground();
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.getTextureManager().bindTexture(BACKGROUND);
		this.drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);
		if (this.hasPaper())
		{
			this.mc.getTextureManager().bindTexture(ICONS);
			int x = this.guiLeft + (this.xSize - PAPER_ICON_WIDTH) / 2;
			this.drawTexturedModalRect(x, this.guiTop + 16, 0, 0, PAPER_ICON_WIDTH, PAPER_ICON_HEIGHT);
			
			if (this.gettingSigned)
			{
				String displayTitle = this.title;
				
				if (this.isListening)
				{
					if (this.updateCount / 6 % 2 == 0)
					{
						displayTitle = displayTitle + "" + TextFormatting.BLACK + "_";
					}
					else
					{
						displayTitle = displayTitle + "" + TextFormatting.GRAY + "_";
					}
				}
				
				String editText = I18n.format("page.editTitle");
				this.fontRenderer.drawString(editText, x + (PAPER_ICON_WIDTH - this.fontRenderer.getStringWidth(editText)) / 2, this.guiTop + BORDER + 19, 0);
				this.fontRenderer.drawString(displayTitle, x + (PAPER_ICON_WIDTH - this.fontRenderer.getStringWidth(displayTitle)) / 2, this.guiTop + BORDER + 35, 0);
				String authorText = I18n.format("page.byAuthor", this.signedName);
				this.fontRenderer.drawString(TextFormatting.DARK_GRAY + authorText, x + (PAPER_ICON_WIDTH - this.fontRenderer.getStringWidth(authorText)) / 2, this.guiTop + BORDER + 45, 0);
				this.fontRenderer.drawSplitString(I18n.format("page.finalizeWarning"), x + BORDER, this.guiTop + BORDER + 67, PAPER_ICON_WIDTH - 2 * BORDER, 0);
			}
			else
			{
				String displayText = this.pageContent;
				
				if (this.isListening)
				{
					if (this.fontRenderer.getBidiFlag())
					{
						displayText = displayText + "_";
					}
					else if (this.updateCount / 6 % 2 == 0)
					{
						displayText = displayText + "" + TextFormatting.BLACK + "_";
					}
					else
					{
						displayText = displayText + "" + TextFormatting.GRAY + "_";
					}
				}
				
				this.fontRenderer.drawSplitString(displayText, x + BORDER, this.guiTop + BORDER + 19, PAPER_ICON_WIDTH - 2 * BORDER, 0);
			}
		}
	}
	
	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
	{
		this.fontRenderer.drawString(this.inventoryName, 48, this.ySize - 93, 4210752);
	}
	
	@Override
	protected void actionPerformed(GuiButton button) throws IOException
	{
		if (button == this.buttonSign)
		{
			if (this.hasPaper())
			{
				if (!this.pageContent.isEmpty())
				{
					if (this.gettingSigned && !this.title.isEmpty())
					{
						this.sendPageToServer();
					}
					this.gettingSigned = true;
				}
				this.isListening = true;
			}
		}
		else if (button == this.buttonCancel)
		{
			this.gettingSigned = false;
			if (this.hasPaper()) this.isListening = true;
		}
	}
	
}

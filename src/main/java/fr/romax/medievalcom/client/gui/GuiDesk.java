package fr.romax.medievalcom.client.gui;

import static fr.romax.medievalcom.client.gui.GuiReadMessage.BORDER;
import static fr.romax.medievalcom.client.gui.GuiReadMessage.DESK_ICONS;
import static fr.romax.medievalcom.client.gui.GuiReadMessage.PAPER_ICON_HEIGHT;
import static fr.romax.medievalcom.client.gui.GuiReadMessage.PAPER_ICON_WIDTH;

import java.io.IOException;

import org.lwjgl.input.Keyboard;

import fr.romax.medievalcom.MedievalCommunications;
import fr.romax.medievalcom.common.ModNetwork;
import fr.romax.medievalcom.common.blocks.TileEntityDesk;
import fr.romax.medievalcom.common.inventory.ContainerDesk;
import fr.romax.medievalcom.common.network.MessageWritePage;
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
	
	private static final ResourceLocation BACKGROUND = new ResourceLocation(MedievalCommunications.MODID, "textures/gui/container/desk_gui.png");
	private final TileEntityDesk desk;
	private final String inventoryName;
	
	/** Update ticks since the gui was opened */
	private int updateCount;
	private boolean isListening = false;
	/** Determines if the signing screen is open */
	private boolean gettingFinilized = false;
	private boolean focusTitle = true;
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
		this.buttonSign = this.addButton(new GuiButton(0, this.width / 2 - 100, this.guiTop + 136, 98, 20, I18n.format("page.signButton")));
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
		this.buttonSign.enabled = !this.pageContent.isEmpty() && (!this.gettingFinilized || !this.title.isEmpty());
		this.buttonCancel.visible = this.gettingFinilized && hasPaper;
	}
	
	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException
	{
		int x = this.guiLeft + (this.xSize - PAPER_ICON_WIDTH) / 2;
		
		if (mouseX > x && mouseX <= x + PAPER_ICON_WIDTH && mouseY > this.guiTop + 16 && mouseY <= this.guiTop + 16 + PAPER_ICON_HEIGHT)
		{
			this.isListening = true;
			
			if (this.gettingFinilized)
			{
				if (mouseY > this.guiTop + BORDER + 34 && mouseY < this.guiTop + BORDER + 36 + this.fontRenderer.FONT_HEIGHT)
				{
					this.focusTitle = true;
				}
				else if (mouseY > this.guiTop + BORDER + 44 && mouseY < this.guiTop + BORDER + 46 + this.fontRenderer.FONT_HEIGHT)
				{
					this.focusTitle = false;
				}
				
			}
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
				if (this.gettingFinilized)
				{
					this.gettingFinilized = false;
				}
				else
				{
					this.isListening = false;
				}
			}
			else
			{
				if (this.gettingFinilized)
				{
					switch (keyCode)
					{
					case Keyboard.KEY_BACK:
						
						if (this.focusTitle)
						{
							if (!this.title.isEmpty())
							{
								this.title = this.title.substring(0, this.title.length() - 1);
								this.updateState();
							}
						}
						else
						{
							if (!this.signedName.isEmpty())
							{
								this.signedName = this.signedName.substring(0, this.signedName.length() - 1);
							}
						}
						return;
					case Keyboard.KEY_RETURN:
					case Keyboard.KEY_NUMPADENTER:
						
						if (!this.title.isEmpty())
						{
							this.sendPageToServer();
						}
						
						return;
					case Keyboard.KEY_TAB:
						this.focusTitle = !this.focusTitle;
					default:
						
						if (this.focusTitle)
						{
							if (this.title.length() < 16 && ChatAllowedCharacters.isAllowedCharacter(typedChar))
							{
								this.title = this.title + Character.toString(typedChar);
								this.updateState();
							}
						}
						else
						{
							if (this.signedName.length() < 16 && ChatAllowedCharacters.isAllowedCharacter(typedChar))
							{
								this.signedName += Character.toString(typedChar);
							}
						}
					}
				}
				else
				{
					if (GuiScreen.isKeyComboCtrlV(keyCode))
					{
						this.insertIntoPage(GuiScreen.getClipboardString());
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
							this.insertIntoPage("\n");
							return;
						default:
							
							if (ChatAllowedCharacters.isAllowedCharacter(typedChar))
							{
								this.insertIntoPage(Character.toString(typedChar));
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
	
	private void insertIntoPage(String toInsert)
	{
		String newContent = this.pageContent + toInsert;
		int i = this.fontRenderer.getWordWrappedHeight(newContent + "" + TextFormatting.BLACK + "_", PAPER_ICON_WIDTH - 2 * BORDER);
		
		if (i <= PAPER_ICON_HEIGHT - 2 * BORDER && newContent.length() < 480)
		{
			this.pageContent = newContent;
		}
	}
	
	private void sendPageToServer()
	{
		ModNetwork.MOD_CHANNEL.sendToServer(new MessageWritePage(this.inventorySlots.windowId, this.pageContent, this.title, this.signedName));
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
			this.mc.getTextureManager().bindTexture(DESK_ICONS);
			int x = this.guiLeft + (this.xSize - PAPER_ICON_WIDTH) / 2;
			this.drawTexturedModalRect(x, this.guiTop + 16, 0, 0, PAPER_ICON_WIDTH, PAPER_ICON_HEIGHT);
			
			if (this.gettingFinilized)
			{
				String displayTitle = this.title;
				//Use unknown author when the author field don't have the focus
				boolean useUnknownAuthor = this.signedName.isEmpty() && (!this.isListening || this.focusTitle);
				String authorName = useUnknownAuthor ? I18n.format("page.byUnknownAuthor") : I18n.format("page.byAuthor",  this.signedName);
				String authorSting = authorName;
				
				if (this.isListening)
				{
					String cursor = (this.updateCount / 6 % 2 == 0 ? TextFormatting.BLACK : TextFormatting.GRAY) + "_";
					if (this.focusTitle)
					{
						displayTitle += cursor;
					}
					else
					{
						authorSting += cursor;
					}
				}
				
				String editText = I18n.format("page.editTitle");
				this.fontRenderer.drawString(editText, x + 1 + (PAPER_ICON_WIDTH - this.fontRenderer.getStringWidth(editText)) / 2, this.guiTop + BORDER + 19, 0);
				this.fontRenderer.drawString(displayTitle, x + 1 + (PAPER_ICON_WIDTH - this.fontRenderer.getStringWidth(this.title)) / 2, this.guiTop + BORDER + 35, 0);
				this.fontRenderer.drawString(TextFormatting.DARK_GRAY + authorSting, x + 1 + (PAPER_ICON_WIDTH - this.fontRenderer.getStringWidth(authorName)) / 2, this.guiTop + BORDER + 45, 0);
				this.fontRenderer.drawSplitString(I18n.format("page.finalizeWarning"), x + 2 + BORDER, this.guiTop + BORDER + 67, PAPER_ICON_WIDTH - 2 * BORDER, 0);
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
					else
					{
						displayText += (this.updateCount / 6 % 2 == 0 ? TextFormatting.BLACK : TextFormatting.GRAY) + "_";
					}
				}
				
				this.fontRenderer.drawSplitString(displayText, x + 2 + BORDER, this.guiTop + BORDER + 19, PAPER_ICON_WIDTH - 2 * BORDER, 0);
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
					if (this.gettingFinilized && !this.title.isEmpty())
					{
						this.sendPageToServer();
					}
					this.gettingFinilized = true;
				}
				this.isListening = true;
			}
		}
		else if (button == this.buttonCancel)
		{
			this.gettingFinilized = false;
			if (this.hasPaper()) this.isListening = true;
		}
	}
	
}

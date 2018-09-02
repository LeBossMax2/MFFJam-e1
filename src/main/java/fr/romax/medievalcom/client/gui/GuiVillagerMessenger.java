package fr.romax.medievalcom.client.gui;

import static fr.romax.medievalcom.client.gui.GuiReadMessage.BORDER;
import static fr.romax.medievalcom.client.gui.GuiReadMessage.PAPER_ICON_WIDTH;

import java.io.IOException;

import fr.romax.medievalcom.MedievalCommunications;
import fr.romax.medievalcom.common.entities.EntityVillagerMessenger;
import fr.romax.medievalcom.common.inventory.ContainerVillagerMessenger;
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
public class GuiVillagerMessenger extends GuiContainer
{
	
	private static final ResourceLocation BACKGROUND = new ResourceLocation(MedievalCommunications.MODID, "textures/gui/container/villager_messenger.png");
	private final EntityVillagerMessenger entity;
	private final String inventoryName;
	
	/** Update ticks since the gui was opened */
	private int updateCount;
	private boolean isListening = false;
	private String addressee = "";
	
	private GuiButton buttonSend;
	
	public GuiVillagerMessenger(EntityVillagerMessenger entity, InventoryPlayer playerInv)
	{
		super(new ContainerVillagerMessenger(entity, playerInv));
		this.entity = entity;
		this.inventoryName = playerInv.getDisplayName().getUnformattedText();
	}
	
	@Override
	public void initGui()
	{
		super.initGui();
		this.buttonSend = this.addButton(new GuiButton(1, this.width / 2 + 2, this.guiTop + 51, 80, 20, I18n.format("gui.messenger.send")));
	}
	
	@Override
	public void updateScreen()
	{
		super.updateScreen();
		++this.updateCount;
	}
	
	protected boolean hasItems()
	{
		return this.inventorySlots.getSlot(0).getHasStack() && this.inventorySlots.getSlot(1).getHasStack();
	}
	
	protected void updateState()
	{
		this.buttonSend.enabled = !this.addressee.isEmpty() && this.hasItems();
	}
	
	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException
	{
		if (mouseX > this.guiLeft && mouseX <= this.guiLeft + this.xSize && mouseY > this.guiTop && mouseY <= this.guiTop + 40)
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
				this.isListening = false;
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
						if (!this.addressee.isEmpty())
						{
							this.addressee = this.addressee.substring(0, this.addressee.length() - 1);
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
		else
		{
			super.keyTyped(typedChar, keyCode);
		}
	}
	
	private void insertIntoPage(String toInsert)
	{
		String newContent = this.addressee + toInsert;
		
		if (newContent.length() <= 16)
		{
			this.addressee = newContent;
		}
	}
	
	private void sendPageToServer()
	{
		
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
		
		int x = this.guiLeft + (this.xSize - PAPER_ICON_WIDTH) / 2;
		
		String displayAddressee = this.addressee;
		
		if (this.isListening)
		{
			if (this.fontRenderer.getBidiFlag())
			{
				displayAddressee = displayAddressee + "_";
			}
			else
			{
				displayAddressee += (this.updateCount / 6 % 2 == 0 ? TextFormatting.BLACK : TextFormatting.GRAY) + "_";
			}
		}
		else if (displayAddressee.isEmpty())
		{
			displayAddressee += TextFormatting.DARK_GRAY + "_";
		}

		String sendToText = I18n.format("gui.messenger.addressee",  displayAddressee);
		this.fontRenderer.drawString(sendToText, x + 1 + (PAPER_ICON_WIDTH - this.fontRenderer.getStringWidth(sendToText)) / 2, this.guiTop + BORDER + 19, 0);
	}
	
	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
	{
        String villagerName = this.entity.getName();
        this.fontRenderer.drawString(villagerName, (this.xSize - this.fontRenderer.getStringWidth(villagerName)) / 2, 6, 4210752);
		this.fontRenderer.drawString(this.inventoryName, 48, this.ySize - 93, 4210752);
	}
	
	@Override
	protected void actionPerformed(GuiButton button) throws IOException
	{
		if (button == this.buttonSend)
		{
			if (this.hasItems() && !this.addressee.isEmpty())
			{
				this.sendPageToServer();
			}
			this.isListening = true;
		}
	}
	
}

package fr.romax.mffjam.client.renderers.entity;

import org.lwjgl.opengl.GL11;

import fr.romax.mffjam.client.gui.GuiReadMessage;
import fr.romax.mffjam.common.entities.EntityHangingMessage;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderHangingMessage<T extends EntityHangingMessage> extends Render<T>
{
	private final double TEXTURE_WIDTH = GuiReadMessage.PAPER_ICON_WIDTH / 256D;
	private final double TEXTURE_HEIGHT = GuiReadMessage.PAPER_ICON_HEIGHT / 256D;
	private final double SCALE_TO_TEXT = 16.0f / GuiReadMessage.PAPER_ICON_HEIGHT;

	public RenderHangingMessage(RenderManager renderManagerIn)
	{
		super(renderManagerIn);
	}

	@Override
	protected ResourceLocation getEntityTexture(T entity)
	{
		return GuiReadMessage.DESK_ICONS;
	}
	
	@Override
	public void doRender(T entity, double x, double y, double z, float entityYaw, float partialTicks)
	{
		this.bindEntityTexture(entity);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.pushMatrix();
        GlStateManager.disableLighting();
        GlStateManager.translate((float)x, (float)y, (float)z);
        
        GlStateManager.rotate(180, 0, 0, 1);
        GlStateManager.rotate(entity.prevRotationYaw + (entity.rotationYaw - entity.prevRotationYaw) * partialTicks, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate(entity.prevRotationPitch + (entity.rotationPitch - entity.prevRotationPitch) * partialTicks, 0.0F, 0.0F, 1.0F);
        GlStateManager.rotate(180, 0, 1, 0);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        GlStateManager.enableRescaleNormal();
        float scale = 1.0f / 16;
        GlStateManager.scale(scale, scale, scale);

        if (this.renderOutlines)
        {
            GlStateManager.enableColorMaterial();
            GlStateManager.enableOutlineMode(this.getTeamColor(entity));
        }

        GlStateManager.glNormal3f(0.0F, 0.0F, 0.05625F);
        bufferbuilder.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
        bufferbuilder.pos(-7.0D, 8.0D, 0.0D).tex(0.0D, TEXTURE_HEIGHT).endVertex();
        bufferbuilder.pos(-7.0D,-8.0D, 0.0D).tex(0.0D, 0.0D).endVertex();
        bufferbuilder.pos( 7.0D,-8.0D, 0.0D).tex(TEXTURE_WIDTH , 0.0D).endVertex();
        bufferbuilder.pos( 7.0D, 8.0D, 0.0D).tex(TEXTURE_WIDTH , TEXTURE_HEIGHT).endVertex();
        tessellator.draw();
        GlStateManager.glNormal3f(0.0F, 0.0F, -0.05625F);
        bufferbuilder.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
        bufferbuilder.pos( 7.0D, 8.0D, 0.0D).tex(TEXTURE_WIDTH, TEXTURE_HEIGHT).endVertex();
        bufferbuilder.pos( 7.0D,-8.0D, 0.0D).tex(TEXTURE_WIDTH, 0.0D).endVertex();
        bufferbuilder.pos(-7.0D,-8.0D, 0.0D).tex(0.0D , 0.0D).endVertex();
        bufferbuilder.pos(-7.0D, 8.0D, 0.0D).tex(0.0D , TEXTURE_HEIGHT).endVertex();
        tessellator.draw();
        
        GlStateManager.translate(-7.0F, -8.0F, -0.01F);
        
        GlStateManager.scale(SCALE_TO_TEXT, SCALE_TO_TEXT, SCALE_TO_TEXT);
        
        FontRenderer fontRenderer = this.getFontRendererFromRenderManager();
		String displayText = entity.pageContent();
		
		fontRenderer.drawSplitString(displayText, GuiReadMessage.BORDER + 3, GuiReadMessage.BORDER + 3, GuiReadMessage.PAPER_ICON_WIDTH - 2 * GuiReadMessage.BORDER, 0x000000);

        if (this.renderOutlines)
        {
            GlStateManager.disableOutlineMode();
            GlStateManager.disableColorMaterial();
        }

        GlStateManager.disableRescaleNormal();
        GlStateManager.enableLighting();
        GlStateManager.popMatrix();
		super.doRender(entity, x, y, z, entityYaw, partialTicks);
	}
	
}

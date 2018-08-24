package fr.romax.mffjam.client.renderers.entity;

import org.lwjgl.opengl.GL11;

import fr.romax.mffjam.MFFJam;
import fr.romax.mffjam.common.entities.EntityMessageArrow;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderArrow;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderMessageArrow extends RenderArrow<EntityMessageArrow>
{
    public static final ResourceLocation ARROW = new ResourceLocation(MFFJam.MODID, "textures/entity/projectiles/message_arrow.png");

	public RenderMessageArrow(RenderManager renderManagerIn)
	{
		super(renderManagerIn);
	}

	@Override
	protected ResourceLocation getEntityTexture(EntityMessageArrow entity)
	{
		return ARROW;
	}
	
	@Override
	public void doRender(EntityMessageArrow entity, double x, double y, double z, float entityYaw, float partialTicks)
	{
		this.bindEntityTexture(entity);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.pushMatrix();
        GlStateManager.disableLighting();
        GlStateManager.disableCull();
        GlStateManager.translate((float)x, (float)y, (float)z);
        GlStateManager.rotate(entity.prevRotationYaw + (entity.rotationYaw - entity.prevRotationYaw) * partialTicks - 90.0F, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate(entity.prevRotationPitch + (entity.rotationPitch - entity.prevRotationPitch) * partialTicks, 0.0F, 0.0F, 1.0F);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        GlStateManager.enableRescaleNormal();
        float f9 = entity.arrowShake - partialTicks;

        if (f9 > 0.0F)
        {
            float f10 = -MathHelper.sin(f9 * 3.0F) * f9;
            GlStateManager.rotate(f10, 0.0F, 0.0F, 1.0F);
        }
        
        GlStateManager.scale(0.05625F, 0.05625F, 0.05625F);
        GlStateManager.translate(-4.0F, 0.0F, 0.0F);

        if (this.renderOutlines)
        {
            GlStateManager.enableColorMaterial();
            GlStateManager.enableOutlineMode(this.getTeamColor(entity));
        }
        
        float texturePixel = 1.0F / 32.0F;
        float texW = 8 * texturePixel;
        
        
        for (int i = 0; i < 4; i++)
        {
        	float v = texturePixel * (10 + i);
        	GlStateManager.glNormal3f(0.0F, 0.0F, 0.05625F);
        	bufferbuilder.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
        	bufferbuilder.pos(-4.0D,-0.5D, 0.5D).tex(0.0D, v).endVertex();
        	bufferbuilder.pos(-4.0D,-0.5D,-0.5D).tex(0.0D, v + texturePixel).endVertex();
        	bufferbuilder.pos( 4.0D,-0.5D,-0.5D).tex(texW, v + texturePixel).endVertex();
        	bufferbuilder.pos( 4.0D,-0.5D, 0.5D).tex(texW, v).endVertex();
        	tessellator.draw();
        	
            GlStateManager.rotate(90.0F, 1.0F, 0.0F, 0.0F);
        }
        
    	float v = texturePixel * 14;
    	GlStateManager.glNormal3f(0.0F, 0.0F, 0.05625F);
    	bufferbuilder.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
    	bufferbuilder.pos(-4.0D,-0.5D, 0.5D).tex(0.0D, v).endVertex();
    	bufferbuilder.pos(-4.0D,-2.5D, 0.5D).tex(0.0D, v + texturePixel * 2).endVertex();
    	bufferbuilder.pos( 4.0D,-2.5D, 0.5D).tex(texW, v + texturePixel * 2).endVertex();
    	bufferbuilder.pos( 4.0D,-0.5D, 0.5D).tex(texW, v).endVertex();
    	tessellator.draw();
    	
    	
        if (this.renderOutlines)
        {
            GlStateManager.disableOutlineMode();
            GlStateManager.disableColorMaterial();
        }

        GlStateManager.disableRescaleNormal();
    	GlStateManager.enableCull();
        GlStateManager.enableLighting();
        GlStateManager.popMatrix();
		super.doRender(entity, x, y, z, entityYaw, partialTicks);
	}
	
}

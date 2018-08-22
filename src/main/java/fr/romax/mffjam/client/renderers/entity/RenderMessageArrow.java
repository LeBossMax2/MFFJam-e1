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
        GlStateManager.translate((float)x, (float)y, (float)z);
        GlStateManager.rotate(entity.prevRotationYaw + (entity.rotationYaw - entity.prevRotationYaw) * partialTicks - 90.0F, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate(entity.prevRotationPitch + (entity.rotationPitch - entity.prevRotationPitch) * partialTicks, 0.0F, 0.0F, 1.0F);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuffer();
        int i = 0;
        float f = 0.0F;
        float f1 = 0.5F;
        float f2 = 0.0F;
        float f3 = 0.15625F;
        float f4 = 0.0F;
        float f5 = 0.15625F;
        float f6 = 0.15625F;
        float f7 = 0.3125F;
        float f8 = 0.05625F;
        GlStateManager.enableRescaleNormal();
        float f9 = entity.arrowShake - partialTicks;

        if (f9 > 0.0F)
        {
            float f10 = -MathHelper.sin(f9 * 3.0F) * f9;
            GlStateManager.rotate(f10, 0.0F, 0.0F, 1.0F);
        }

        //GlStateManager.rotate(45.0F, 1.0F, 0.0F, 0.0F);
        GlStateManager.scale(0.05625F, 0.05625F, 0.05625F);
        GlStateManager.translate(-4.0F, 0.0F, 0.0F);

        if (this.renderOutlines)
        {
            GlStateManager.enableColorMaterial();
            GlStateManager.enableOutlineMode(this.getTeamColor(entity));
        }

        GlStateManager.glNormal3f(0.0F, 0.0F, 0.05625F);
        bufferbuilder.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
        bufferbuilder.pos(-4.0D, 0.0D, 0.0D).tex(0.0D , 0.3125D).endVertex();
        bufferbuilder.pos(-4.0D,-9.0D, 0.0D).tex(0.0D , 0.59375D).endVertex();
        bufferbuilder.pos( 4.0D,-9.0D, 0.0D).tex(0.25D, 0.59375D).endVertex();
        bufferbuilder.pos( 4.0D, 0.0D, 0.0D).tex(0.25D, 0.3125D).endVertex();
        tessellator.draw();
        GlStateManager.glNormal3f(0.0F, 0.0F, -0.05625F);
        bufferbuilder.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
        bufferbuilder.pos( 4.0D, 0.0D, 0.0D).tex(0.0D , 0.3125D).endVertex();
        bufferbuilder.pos( 4.0D,-9.0D, 0.0D).tex(0.0D , 0.59375D).endVertex();
        bufferbuilder.pos(-4.0D,-9.0D, 0.0D).tex(0.25D, 0.59375D).endVertex();
        bufferbuilder.pos(-4.0D, 0.0D, 0.0D).tex(0.25D, 0.3125D).endVertex();
        tessellator.draw();

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

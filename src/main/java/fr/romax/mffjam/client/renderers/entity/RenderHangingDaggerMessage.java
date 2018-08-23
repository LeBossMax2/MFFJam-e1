package fr.romax.mffjam.client.renderers.entity;

import fr.romax.mffjam.common.entities.EntityHangingDaggerMessage;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderHangingDaggerMessage extends RenderHangingMessage<EntityHangingDaggerMessage>
{
	protected final RenderItem itemRenderer;

	public RenderHangingDaggerMessage(RenderManager renderManagerIn)
	{
		super(renderManagerIn);
		this.itemRenderer = Minecraft.getMinecraft().getRenderItem();
	}
	
	@Override
	protected void renderEntity(EntityHangingDaggerMessage entity)
	{
		GlStateManager.pushMatrix();
        GlStateManager.translate(entity.getDaggerX() - 0.5F, 0.5F - entity.getDaggerY(), -0.04f);
        GlStateManager.rotate(90.0F, 0, 1, 0);
        GlStateManager.rotate(45.0F, 0, 0, 1);
        GlStateManager.scale(0.5F, 0.5F, 0.5F);
        GlStateManager.pushAttrib();
        RenderHelper.enableStandardItemLighting();
        this.itemRenderer.renderItem(entity.getDaggerStack(), ItemCameraTransforms.TransformType.FIXED);
        RenderHelper.disableStandardItemLighting();
        GlStateManager.popAttrib();
        GlStateManager.popMatrix();
        
		super.renderEntity(entity);
	}
	
}

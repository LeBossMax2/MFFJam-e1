package fr.romax.medievalcom.client.renderers.entity;

import fr.romax.medievalcom.MedievalCommunications;
import fr.romax.medievalcom.common.entities.EntityVillagerMessager;
import net.minecraft.client.model.ModelVillager;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.layers.LayerCustomHead;
import net.minecraft.util.ResourceLocation;

public class RenderVillagerMessager extends RenderLiving<EntityVillagerMessager>
{
    private static final ResourceLocation MESSAGER_TEXTURE = new ResourceLocation(MedievalCommunications.MODID, "textures/entity/villager/messenger.png");
	
	public RenderVillagerMessager(RenderManager renderManagerIn)
	{
		super(renderManagerIn, new ModelVillager(0.0F), 0.5F);
        this.addLayer(new LayerCustomHead(this.getMainModel().villagerHead));
	}

	@Override
	protected ResourceLocation getEntityTexture(EntityVillagerMessager entity)
	{
		return MESSAGER_TEXTURE;
	}
	
	public ModelVillager getMainModel()
    {
        return (ModelVillager)super.getMainModel();
    }

    /**
     * Allows the render to do state modifications necessary before the model is rendered.
     */
	@Override
    protected void preRenderCallback(EntityVillagerMessager entitylivingbaseIn, float partialTickTime)
    {
        float f = 0.9375F;

        if (entitylivingbaseIn.getGrowingAge() < 0)
        {
            f = (float)(f * 0.5D);
            this.shadowSize = 0.25F;
        }
        else
        {
            this.shadowSize = 0.5F;
        }

        GlStateManager.scale(f, f, f);
    }

}

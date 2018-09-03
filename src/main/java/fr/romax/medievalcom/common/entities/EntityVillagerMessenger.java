package fr.romax.medievalcom.common.entities;

import java.util.UUID;

import javax.annotation.Nullable;

import fr.romax.medievalcom.common.GuiHandler;
import fr.romax.medievalcom.common.entities.ai.EntityAIMoveToMessageTarget;
import fr.romax.medievalcom.common.entities.ai.EntityAILookAtCustomer;
import fr.romax.medievalcom.common.entities.ai.EntityAITradeWithPlayer;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.INpc;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAvoidEntity;
import net.minecraft.entity.ai.EntityAIMoveIndoors;
import net.minecraft.entity.ai.EntityAIMoveTowardsRestriction;
import net.minecraft.entity.ai.EntityAIOpenDoor;
import net.minecraft.entity.ai.EntityAIRestrictOpenDoor;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.ai.EntityAIWanderAvoidWater;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.ai.EntityAIWatchClosest2;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.monster.EntityEvoker;
import net.minecraft.entity.monster.EntityVex;
import net.minecraft.entity.monster.EntityVindicator;
import net.minecraft.entity.monster.EntityWitch;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.pathfinding.PathNavigateGround;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.scoreboard.Team;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.village.Village;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootTableList;
import net.minecraftforge.common.util.Constants.NBT;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class EntityVillagerMessenger extends EntityAgeable implements INpc, IEntityAdditionalSpawnData
{
	protected UUID targetId;
	protected ItemStack message;

    private int careerId;
    
	/** This villager's current customer. */
    @Nullable
    private EntityPlayer customer;
	
	private int randomTickDivider;
    protected Village village;
	
	public EntityVillagerMessenger(World worldIn)
	{
		super(worldIn);
        this.setSize(0.6F, 1.95F);
        ((PathNavigateGround)this.getNavigator()).setBreakDoors(true);
        this.setCanPickUpLoot(true);
        this.careerId = this.rand.nextInt(4) + 1;
	}
	
	@Override
    protected void initEntityAI()
    {
        this.tasks.addTask(0, new EntityAISwimming(this));
        this.tasks.addTask(1, new EntityAIAvoidEntity<>(this, EntityZombie.class, 8.0F, 0.6D, 0.6D));
        this.tasks.addTask(1, new EntityAIAvoidEntity<>(this, EntityEvoker.class, 12.0F, 0.8D, 0.8D));
        this.tasks.addTask(1, new EntityAIAvoidEntity<>(this, EntityVindicator.class, 8.0F, 0.8D, 0.8D));
        this.tasks.addTask(1, new EntityAIAvoidEntity<>(this, EntityVex.class, 8.0F, 0.6D, 0.6D));
        
        this.tasks.addTask(2, new EntityAIMoveToMessageTarget(this, 0.6D));
        
        this.tasks.addTask(3, new EntityAITradeWithPlayer(this));
        this.tasks.addTask(3, new EntityAILookAtCustomer(this));
        this.tasks.addTask(4, new EntityAIMoveIndoors(this));
        this.tasks.addTask(5, new EntityAIRestrictOpenDoor(this));
        this.tasks.addTask(6, new EntityAIOpenDoor(this, true));
        this.tasks.addTask(7, new EntityAIMoveTowardsRestriction(this, 0.6D));
        this.tasks.addTask(8, new EntityAIWatchClosest2(this, EntityPlayer.class, 3.0F, 1.0F));
        this.tasks.addTask(8, new EntityAIWanderAvoidWater(this, 0.6D));
        this.tasks.addTask(9, new EntityAIWatchClosest(this, EntityLiving.class, 8.0F));
    }
	
	@Override
	protected void applyEntityAttributes()
	{
		super.applyEntityAttributes();
        this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.5D);
	}
	
	@Override
	protected void updateAITasks()
	{
		if (--this.randomTickDivider <= 0)
        {
            BlockPos blockpos = new BlockPos(this);
            this.world.getVillageCollection().addToVillagerPositionList(blockpos);
            this.randomTickDivider = 70 + this.rand.nextInt(50);
            this.village = this.world.getVillageCollection().getNearestVillage(blockpos, 32);

            if (this.village == null)
            {
                this.detachHome();
            }
            else
            {
                BlockPos blockpos1 = this.village.getCenter();
                this.setHomePosAndDistance(blockpos1, this.village.getVillageRadius());
            }
        }
		
		super.updateAITasks();
	}
	
	@Override
	public void writeEntityToNBT(NBTTagCompound compound)
	{
		super.writeEntityToNBT(compound);
		compound.setInteger("Career", this.careerId);
		if (this.hasMessage())
		{
			compound.setString("TargetUUID", this.targetId.toString());
			compound.setTag("MessageItem", this.message.serializeNBT());
		}
	}
	
	@Override
	public void readEntityFromNBT(NBTTagCompound compound)
	{
		super.readEntityFromNBT(compound);
		this.careerId = compound.getInteger("Career");
		if (compound.hasKey("TargetUUID", NBT.TAG_STRING))
		{
			try
            {
				this.targetId = UUID.fromString(compound.getString("TargetUUID"));
            }
            catch (Throwable var4)
            { }
			this.message = new ItemStack(compound.getCompoundTag("MessageItem"));
		}
	}

	@Override
	public void writeSpawnData(ByteBuf buffer)
	{
		buffer.writeInt(this.careerId);
	}

	@Override
	public void readSpawnData(ByteBuf buffer)
	{
		this.careerId = buffer.readInt();
	}
	
	@Override
	public boolean processInteract(EntityPlayer player, EnumHand hand)
	{
        ItemStack itemstack = player.getHeldItem(hand);
        boolean flag = itemstack.getItem() == Items.NAME_TAG;

        if (flag)
        {
            itemstack.interactWithEntity(player, this, hand);
            return true;
        }
        else if (!this.holdingSpawnEggOfClass(itemstack, this.getClass()) && this.isEntityAlive() && !this.isChild() && !player.isSneaking())
        {
            if (!this.world.isRemote && !this.hasMessage())
            {
            	this.setCustomer(player);
            	GuiHandler.openEntityGui(this.world, player, this);
            }

            return true;
        }
        else
        {
            return super.processInteract(player, hand);
        }
	}
	
	@Override
	public void onCollideWithPlayer(EntityPlayer player)
	{
		if (this.hasMessage() && this.getMessageTarget() == player)
		{
			this.deliverMessage(player);
		}
	}
	
	private void deliverMessage(EntityPlayer target)
	{
		target.inventory.placeItemBackInInventory(this.world, this.message);
		this.message = null;
		this.targetId = null;
	}
	
	@Override
	protected boolean canDespawn()
	{
		return false;
	}
	
	@Override
	protected SoundEvent getAmbientSound()
	{
		return this.isTrading() ? SoundEvents.ENTITY_VILLAGER_TRADING : SoundEvents.ENTITY_VILLAGER_AMBIENT;
	}
	
	@Override
    protected SoundEvent getHurtSound(DamageSource damageSourceIn)
    {
        return SoundEvents.ENTITY_VILLAGER_HURT;
    }
	
	@Override
    protected SoundEvent getDeathSound()
    {
        return SoundEvents.ENTITY_VILLAGER_DEATH;
    }
	
    @Nullable
	@Override
    protected ResourceLocation getLootTable()
    {
        return LootTableList.ENTITIES_VILLAGER;
    }
    
    @Override
    public void setRevengeTarget(EntityLivingBase livingBase)
    {
    	super.setRevengeTarget(livingBase);
    	
    	if (this.village != null && livingBase != null)
        {
            this.village.addOrRenewAgressor(livingBase);

            if (livingBase instanceof EntityPlayer)
            {
                int i = -1;

                if (this.isChild())
                {
                    i = -3;
                }

                this.village.modifyPlayerReputation(livingBase.getUniqueID(), i);

                if (this.isEntityAlive())
                {
                    this.world.setEntityState(this, (byte)13);
                }
            }
        }
    }
    
    @Override
    public void onDeath(DamageSource cause)
    {
    	if (this.village != null)
        {
            Entity entity = cause.getTrueSource();

            if (entity != null)
            {
                if (entity instanceof EntityPlayer)
                {
                    this.village.modifyPlayerReputation(entity.getUniqueID(), -2);
                }
                else if (entity instanceof IMob)
                {
                    this.village.endMatingSeason();
                }
            }
            else
            {
                EntityPlayer entityplayer = this.world.getClosestPlayerToEntity(this, 16.0D);

                if (entityplayer != null)
                {
                    this.village.endMatingSeason();
                }
            }
        }
    	
    	super.onDeath(cause);
    }

    public boolean isTrading()
    {
        return this.customer != null;
    }

	public EntityPlayer getCustomer()
	{
		return this.customer;
	}

	public void setCustomer(EntityPlayer customer)
	{
		this.customer = customer;
	}

    public void verifySellingItem(ItemStack stack)
    {
        if (!this.world.isRemote && this.livingSoundTime > -this.getTalkInterval() + 20)
        {
            this.livingSoundTime = -this.getTalkInterval();
            this.playSound(stack.isEmpty() ? SoundEvents.ENTITY_VILLAGER_NO : SoundEvents.ENTITY_VILLAGER_YES, this.getSoundVolume(), this.getSoundPitch());
        }
    }

    @Override
    public ITextComponent getDisplayName()
    {
        Team team = this.getTeam();
        String customName = this.getCustomNameTag();

        if (customName != null && !customName.isEmpty())
        {
        	ITextComponent displayName = new TextComponentString(ScorePlayerTeam.formatPlayerName(team, customName));
            displayName.getStyle().setHoverEvent(this.getHoverEvent());
            displayName.getStyle().setInsertion(this.getCachedUniqueIdString());
            return displayName;
        }
        else
        {
            String name = "messenger";

            switch (this.careerId)
            {
            case 1:
                name = "messenger";
                break;
            case 2:
                name = "envoy";
                break;
            case 3:
                name = "secretAgent";
                break;
            case 4:
                name = "spy";
                break;
            }
            
            ITextComponent displayName = new TextComponentTranslation("entity.villager_messenger." + name, new Object[0]);
            displayName.getStyle().setHoverEvent(this.getHoverEvent());
            displayName.getStyle().setInsertion(this.getCachedUniqueIdString());

            if (team != null)
            {
                displayName.getStyle().setColor(team.getColor());
            }

            return displayName;
        }
    }
    
    @Override
    public float getEyeHeight()
    {
        return this.isChild() ? 0.81F : 1.62F;
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    public void handleStatusUpdate(byte id)
    {
        if (id == 13)
        {
            this.spawnParticles(EnumParticleTypes.VILLAGER_ANGRY);
        }
        else
        {
            super.handleStatusUpdate(id);
        }
    }

    @SideOnly(Side.CLIENT)
    private void spawnParticles(EnumParticleTypes particleType)
    {
        for (int i = 0; i < 5; ++i)
        {
            double speedX = this.rand.nextGaussian() * 0.02D;
            double speedY = this.rand.nextGaussian() * 0.02D;
            double speedZ = this.rand.nextGaussian() * 0.02D;
            this.world.spawnParticle(particleType, this.posX + this.rand.nextFloat() * this.width * 2.0F - this.width, this.posY + 1.0D + this.rand.nextFloat() * this.height, this.posZ + this.rand.nextFloat() * this.width * 2.0F - this.width, speedX, speedY, speedZ);
        }
    }

	@Override
	public EntityAgeable createChild(EntityAgeable ageable)
	{
		EntityVillager villager = new EntityVillager(this.world);
        villager.onInitialSpawn(this.world.getDifficultyForLocation(new BlockPos(villager)), null);
        return villager;
	}
	
	@Override
	public boolean canBeLeashedTo(EntityPlayer player)
	{
		return false;
	}
	
	@Override
	public void onStruckByLightning(EntityLightningBolt lightningBolt)
	{
		if (!this.world.isRemote && !this.isDead)
        {
            EntityWitch witch = new EntityWitch(this.world);
            witch.setLocationAndAngles(this.posX, this.posY, this.posZ, this.rotationYaw, this.rotationPitch);
            witch.onInitialSpawn(this.world.getDifficultyForLocation(new BlockPos(witch)), null);
            witch.setNoAI(this.isAIDisabled());

            if (this.hasCustomName())
            {
                witch.setCustomNameTag(this.getCustomNameTag());
                witch.setAlwaysRenderNameTag(this.getAlwaysRenderNameTag());
            }
            
            this.world.spawnEntity(witch);
            this.setDead();
        }
	}

	public void sendMessage(EntityPlayer addresse, ItemStack message)
	{
		this.targetId = addresse.getUniqueID();
		this.message = message;
		this.setCustomer(null);
	}
	
	public boolean hasMessage()
	{
		return this.message != null && !this.message.isEmpty() && this.targetId != null;
	}
	
	public boolean hasMessageTarget()
	{
		return this.getMessageTarget() != null;
	}
	
	public EntityPlayer getMessageTarget()
	{
		return this.world.getPlayerEntityByUUID(this.targetId);
	}
	
}

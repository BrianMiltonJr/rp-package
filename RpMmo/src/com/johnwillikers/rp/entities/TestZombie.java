package com.johnwillikers.rp.entities;

import com.johnwillikers.rp.NMSUtils.Attributes;

import net.minecraft.server.v1_12_R1.DifficultyDamageScaler;
import net.minecraft.server.v1_12_R1.EntityCreeper;
import net.minecraft.server.v1_12_R1.EntityHuman;
import net.minecraft.server.v1_12_R1.EntitySkeleton;
import net.minecraft.server.v1_12_R1.EntityZombie;
import net.minecraft.server.v1_12_R1.EnumItemSlot;
import net.minecraft.server.v1_12_R1.GroupDataEntity;
import net.minecraft.server.v1_12_R1.ItemStack;
import net.minecraft.server.v1_12_R1.Items;
import net.minecraft.server.v1_12_R1.PathfinderGoalFloat;
import net.minecraft.server.v1_12_R1.PathfinderGoalLookAtPlayer;
import net.minecraft.server.v1_12_R1.PathfinderGoalMoveTowardsRestriction;
import net.minecraft.server.v1_12_R1.PathfinderGoalNearestAttackableTarget;
import net.minecraft.server.v1_12_R1.PathfinderGoalRandomLookaround;
import net.minecraft.server.v1_12_R1.PathfinderGoalRandomStrollLand;
import net.minecraft.server.v1_12_R1.PathfinderGoalZombieAttack;
import net.minecraft.server.v1_12_R1.World;


public class TestZombie extends EntityZombie{

	public TestZombie(World world) {
		super(world);
		
	}
	
	@Override
	protected void r() {
		 // Adding our custom pathfinder selectors.
	    // Grants our zombie the ability to swim.
	    this.goalSelector.a(0, new PathfinderGoalFloat(this));
	    this.goalSelector.a(2, new PathfinderGoalZombieAttack(this, 1.0, true));
	    // Gets our zombie to attack creepers and skeletons!
	    this.targetSelector.a(3, new PathfinderGoalNearestAttackableTarget<>(this, EntityCreeper.class, true));
	    this.targetSelector.a(3, new PathfinderGoalNearestAttackableTarget<>(this, EntitySkeleton.class, true));
	    // Causes our zombie to walk towards it restriction.
	    this.goalSelector.a(5, new PathfinderGoalMoveTowardsRestriction(this, 1.0));
	    // Causes the zombie to walk around randomly.
	    this.goalSelector.a(7, new PathfinderGoalRandomStrollLand(this, 1.0));
	    // Causes the zombie to look at players. Optional in our case. Last
	    // argument is range.
	    this.goalSelector.a(8, new PathfinderGoalLookAtPlayer(this, EntityHuman.class, 8.0f));
	    // Causes the zombie to randomly look around.
	    this.goalSelector.a(8, new PathfinderGoalRandomLookaround(this));
	}

	@Override
	public GroupDataEntity prepare(DifficultyDamageScaler dds, GroupDataEntity gde) {
	    // Calling the super method FIRST, so in case it changes the equipment, our equipment overrides it.
	    gde = super.prepare(dds, gde);
	    // We'll set the main hand to a bow and head to a pumpkin now!
	    this.setSlot(EnumItemSlot.MAINHAND, new ItemStack(Items.IRON_SWORD));
	    this.setSlot(EnumItemSlot.HEAD, new ItemStack(Items.IRON_HELMET));
	    // Last, returning the GroupDataEntity called gde.
	    return gde;
	}
	
	@Override
	protected void initAttributes() {
	    // Calling the super method for the rest of the attributes.
	    super.initAttributes();
	    // Next, overriding armor and max health!
	    // Setting the max health to 40:
	    this.getAttributeInstance(Attributes.MAX_HEALTH.asIAttribute()).setValue(200.0);
	    // Setting the 'defense' (armor) to 5:
	    this.getAttributeInstance(Attributes.ARMOR.asIAttribute()).setValue(5.0);
	}
}

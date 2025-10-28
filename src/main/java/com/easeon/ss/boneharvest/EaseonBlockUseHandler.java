package com.easeon.ss.boneharvest;

import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.world.World;
import java.util.HashMap;
import java.util.Map;

public class EaseonBlockUseHandler {
    private static final Map<Item, Item> HARVESTABLE_PLANTS = new HashMap<>();

    static {
        HARVESTABLE_PLANTS.put(Items.SUGAR_CANE, Items.SUGAR_CANE);       // 사탕수수
        HARVESTABLE_PLANTS.put(Items.VINE, Items.VINE);                   // 덩굴
        HARVESTABLE_PLANTS.put(Items.GLOW_LICHEN, Items.GLOW_LICHEN);     // 발광이끼
        HARVESTABLE_PLANTS.put(Items.BUSH, Items.BUSH);                   // 덤불
        HARVESTABLE_PLANTS.put(Items.FIREFLY_BUSH, Items.FIREFLY_BUSH);   // 반딧불이 덤불
    }

    public static ActionResult useBlock(PlayerEntity player, World world, Hand hand, BlockHitResult hitResult) {
        ItemStack stack = player.getStackInHand(hand);
        Item stackItem = stack.getItem();

        if (world.isClient()) return ActionResult.SUCCESS;

        if (stackItem != Items.BONE_MEAL && stackItem != Items.BONE && stackItem != Items.BONE_BLOCK) {
            return ActionResult.PASS;
        }

        Item blockItem = world.getBlockState(hitResult.getBlockPos()).getBlock().asItem();
        Item dropItem = HARVESTABLE_PLANTS.get(blockItem);

        if (dropItem == null) {
            return ActionResult.PASS;
        }

        double x = hitResult.getBlockPos().getX() + 0.5;
        double y = hitResult.getBlockPos().getY() + 0.50;
        double z = hitResult.getBlockPos().getZ() + 0.5;

        // 클라이언트 사이드
        if (world.isClient()) {
            return ActionResult.SUCCESS;
        }

        // 서버 사이드 - 파티클 생성
        if (world instanceof ServerWorld serverWorld) {
            for (int i = 0; i < 15; i++) {
                double dx = x + (world.getRandom().nextDouble() - 0.5) * 1;
                double dy = y + (world.getRandom().nextDouble() * 0.5);
                double dz = z + (world.getRandom().nextDouble() - 0.5) * 1;
                serverWorld.spawnParticles(ParticleTypes.HAPPY_VILLAGER, dx, dy, dz, 1, 0.0, 0.1, 0.0, 0.0);
            }
        }

        int itemsToConsume;
        if (!player.getAbilities().creativeMode && player.isSneaking()) {
            itemsToConsume = stack.getCount();
        } else {
            itemsToConsume = 1;
        }

        int count;
        if (stackItem == Items.BONE_MEAL) {
            count = 1;
        } else if (stackItem == Items.BONE) {
            count = 3;
        } else {
            count = 9;
        }

        if (!player.getAbilities().creativeMode) {
            stack.decrement(itemsToConsume);
        }

        ItemEntity drop = new ItemEntity(world, x, y, z, new ItemStack(dropItem, count * itemsToConsume));
        world.spawnEntity(drop);

        return ActionResult.SUCCESS;
    }
}
package net.luhcarti.silentpearl.item;

import net.luhcarti.silentpearl.SilentPearl;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.projectile.ThrownEnderpearl;
import net.minecraft.world.item.EnderpearlItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.Level;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, SilentPearl.MOD_ID);

    public static final RegistryObject<Item> SILENT_ENDER_PEARL = ITEMS.register("silent_ender_pearl",
            () -> new SilentEnderpearlItem(new Item.Properties().stacksTo(16))); // Set stack size to 16

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }

    public static class SilentEnderpearlItem extends EnderpearlItem {
        public SilentEnderpearlItem(Properties properties) {
            super(properties);
        }

        @Override
        public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pHand) {
            ItemStack $$3 = pPlayer.getItemInHand(pHand);
            pLevel.playSound((Player)null, pPlayer.getX(), pPlayer.getY(), pPlayer.getZ(), SoundEvents.ENDER_PEARL_THROW, SoundSource.NEUTRAL, 0F, 0F / (pLevel.getRandom().nextFloat() * 0.4F + 0.8F));
            pPlayer.getCooldowns().addCooldown(this, 20);
            if (!pLevel.isClientSide) {
                ThrownEnderpearl $$4 = new ThrownEnderpearl(pLevel, pPlayer);
                $$4.setItem($$3);
                $$4.shootFromRotation(pPlayer, pPlayer.getXRot(), pPlayer.getYRot(), 0.0F, 1.5F, 1.0F);
                pLevel.addFreshEntity($$4);
            }

            pPlayer.awardStat(Stats.ITEM_USED.get(this));
            if (!pPlayer.getAbilities().instabuild) {
                $$3.shrink(1);
            }

            return InteractionResultHolder.sidedSuccess($$3, pLevel.isClientSide());
        }
    }
}


/*
 * Copyright (C) 2025  Tete
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.trs.separatistfrogs.mixin;

import com.simibubi.create.AllSoundEvents;
import com.simibubi.create.content.logistics.packagePort.frogport.FrogportBlockEntity;
import com.trs.separatistfrogs.SeparatistFrogs;
import net.createmod.catnip.animation.LerpedFloat;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(FrogportBlockEntity.class)
public abstract class FrogportBlockEntityMixin extends BlockEntity
{
    @Shadow public abstract boolean isAnimationInProgress();

    @Shadow public LerpedFloat animationProgress;

    //Ignore this it makes them mixin work
    public FrogportBlockEntityMixin(BlockEntityType<?> type, BlockPos pos, BlockState blockState)
    {
        super(type, pos, blockState);
    }

    @Inject(method = "tick", at = @At("RETURN"))
    void injectTick(CallbackInfo ci)
    {
        if (level.isClientSide())
        {
            if ((level.random.nextInt(0, 100) == 5) && !isAnimationInProgress())
            {
                level.playLocalSound(
                        getBlockPos(),
                        SoundEvent.createFixedRangeEvent(
                                ResourceLocation.fromNamespaceAndPath(SeparatistFrogs.MODID, "droid_talk"),
                                4
                        ),
                        SoundSource.BLOCKS,
                        1f,
                        1f,
                        false
                );
                animationProgress.startWithValue(0.5);
                animationProgress.chase(1, 0.6, LerpedFloat.Chaser.LINEAR);
            }
        }
    }
}

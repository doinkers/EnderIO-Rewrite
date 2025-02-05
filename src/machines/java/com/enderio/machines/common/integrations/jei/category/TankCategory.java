package com.enderio.machines.common.integrations.jei.category;

import com.enderio.EnderIO;
import com.enderio.machines.client.gui.screen.FluidTankScreen;
import com.enderio.machines.client.gui.screen.SoulBinderScreen;
import com.enderio.machines.common.blockentity.FluidTankBlockEntity;
import com.enderio.machines.common.init.MachineBlocks;
import com.enderio.machines.common.lang.MachineLang;
import com.enderio.machines.common.recipe.SoulBindingRecipe;
import com.enderio.machines.common.recipe.TankRecipe;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import mezz.jei.api.forge.ForgeTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FastColor;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.extensions.common.IClientFluidTypeExtensions;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.templates.FluidTank;

import java.util.List;

// TODO: 1.20.1+ Add a custom TankRecipe for JEI to show mending and maybe fill/empty too.
public class TankCategory implements IRecipeCategory<TankRecipe> {
    public static final RecipeType<TankRecipe> TYPE = RecipeType.create(EnderIO.MODID, "tank", TankRecipe.class);

    private final IDrawable background;
    private final IDrawable icon;

    public TankCategory(IGuiHelper guiHelper) {
        this.background = guiHelper.createDrawable(FluidTankScreen.BG_TEXTURE, 41, 18, 94, 53);
        this.icon = guiHelper.createDrawableItemStack(new ItemStack(MachineBlocks.FLUID_TANK.get()));
    }

    @Override
    public RecipeType<TankRecipe> getRecipeType() {
        return TYPE;
    }

    @Override
    public Component getTitle() {
        return MachineLang.CATEGORY_TANK;
    }

    @Override
    public IDrawable getBackground() {
        return background;
    }

    @Override
    public IDrawable getIcon() {
        return icon;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, TankRecipe recipe, IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, 39, 3)
            .addIngredients(ForgeTypes.FLUID_STACK, List.of(recipe.getFluid()))
            .setFluidRenderer(FluidTankBlockEntity.Standard.CAPACITY, false, 16, 47);

        if (recipe.isEmptying()) {
            builder.addSlot(RecipeIngredientRole.INPUT, 3, 3)
                .addIngredients(recipe.getInput());

            builder.addSlot(RecipeIngredientRole.OUTPUT, 3, 34)
                .addItemStack(new ItemStack(recipe.getOutput()));
        } else {
            builder.addSlot(RecipeIngredientRole.INPUT, 75, 3)
                .addIngredients(recipe.getInput());

            builder.addSlot(RecipeIngredientRole.OUTPUT, 75, 34)
                .addItemStack(new ItemStack(recipe.getOutput()));
        }
    }
}

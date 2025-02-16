package com.maciej916.indreb.integration.jei.category.impl;

import com.maciej916.indreb.common.receipe.impl.AlloySmeltingRecipe;
import com.maciej916.indreb.common.registries.ModBlocks;
import com.maciej916.indreb.common.registries.ModRecipeSerializer;
import com.maciej916.indreb.common.util.GuiUtil;
import com.maciej916.indreb.integration.jei.category.AbstractRecipeCategory;
import com.mojang.blaze3d.vertex.PoseStack;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.drawable.IDrawableAnimated;
import mezz.jei.api.gui.ingredient.IGuiItemStackGroup;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredients;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

import static com.maciej916.indreb.common.util.Constants.*;

public class AlloySmeltingCategory extends AbstractRecipeCategory<AlloySmeltingRecipe> {

    public static final ResourceLocation UID = ModRecipeSerializer.ALLOY_SMELTING.getRegistryName();

    protected IDrawableAnimated progress;
    protected IDrawableAnimated energy;
    protected IDrawableAnimated fire;

    public AlloySmeltingCategory(IGuiHelper guiHelper) {
        super(
                AlloySmeltingRecipe.class,
                UID,
                "alloy_smelting",
                guiHelper,
                () -> guiHelper.createDrawable(JEI_LARGE, 0, 0, 150, 54),
                () -> guiHelper.createDrawableIngredient(new ItemStack(ModBlocks.ALLOY_SMELTER))
        );
    }

    @Override
    public void setIngredients(AlloySmeltingRecipe recipe, IIngredients ingredients) {
        ingredients.setInputLists(VanillaTypes.ITEM, recipe.getIngredientMap().keySet().stream().map(ingredient -> Arrays.asList(ingredient.getItems())).collect(Collectors.toList()));
        ingredients.setOutput(VanillaTypes.ITEM, recipe.getResultItem());
    }

    @Override
    public void setRecipe(IRecipeLayout recipeLayout, AlloySmeltingRecipe recipe, IIngredients ingredients) {
        IGuiItemStackGroup guiItemStacks = recipeLayout.getItemStacks();

        guiItemStacks.init(0, true, 4, 18);
        guiItemStacks.init(1, true, 25, 6);
        guiItemStacks.init(2, true, 46, 18);
        guiItemStacks.init(3, false, 102, 18);

        int i = 0;
        for (Map.Entry<Ingredient, Integer> entry : recipe.getIngredientMap().entrySet()) {
            Ingredient ingredient = entry.getKey();
            Integer count = entry.getValue();
            guiItemStacks.set(i++, Arrays.stream(ingredient.getItems())
                    .map(s -> {
                        ItemStack stack = s.copy();
                        stack.setCount(count);
                        return stack;
                    })
                    .collect(Collectors.toList())
            );
        }

        guiItemStacks.set(3, recipe.getResultItem());

        this.progress = guiHelper.drawableBuilder(PROCESS, 25, 0, 24, 16).buildAnimated(recipe.getDuration(), IDrawableAnimated.StartDirection.LEFT, false);
        this.energy = guiHelper.drawableBuilder(JEI, 249, 0, 7, 37).buildAnimated(200, IDrawableAnimated.StartDirection.TOP, true);
        this.fire = guiHelper.drawableBuilder(PROCESS, 67, 0, 16,  16).buildAnimated(100, IDrawableAnimated.StartDirection.TOP, true);
    }

    @Override
    public void draw(AlloySmeltingRecipe recipe, PoseStack poseStack, double mouseX, double mouseY) {
        this.progress.draw(poseStack, halfX - 5, 19);
        this.energy.draw(poseStack, halfX + 59, 7);
        this.fire.draw(poseStack, halfX - 49, 27);

        if (recipe.getExperience() > 0) {
            GuiUtil.renderScaled(poseStack, recipe.getExperience() + " XP", 0, 0, 0.75f, 0x7E7E7E, false);
        }
        GuiUtil.renderScaled(poseStack, recipe.getPowerCost() + " IE/T", 0, 48, 0.75f, 0x7E7E7E, false);
    }
}

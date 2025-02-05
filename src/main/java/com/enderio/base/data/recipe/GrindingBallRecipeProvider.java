package com.enderio.base.data.recipe;

import com.enderio.EnderIO;
import com.enderio.base.common.init.EIOItems;
import com.enderio.base.common.init.EIORecipes;
import com.enderio.core.data.recipes.EnderRecipeProvider;
import com.google.gson.JsonObject;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Set;
import java.util.function.Consumer;

public class GrindingBallRecipeProvider extends EnderRecipeProvider {

    public GrindingBallRecipeProvider(PackOutput packOutput) {
        super(packOutput);
    }
    
    @Override
    protected void buildRecipes(Consumer<FinishedRecipe> pFinishedRecipeConsumer) {
        build(Items.FLINT, 1.2F, 1.25F, 0.85F, 24000, pFinishedRecipeConsumer);
        build(EIOItems.DARK_STEEL_BALL.get(), 1.35F, 2.00F, 0.7F, 125000, pFinishedRecipeConsumer);
        build(EIOItems.COPPER_ALLOY_BALL.get(), 1.2F, 1.65F, 0.8F, 40000, pFinishedRecipeConsumer);
        build(EIOItems.ENERGETIC_ALLOY_BALL.get(), 1.6F, 1.1F, 1.1F, 80000, pFinishedRecipeConsumer);
        build(EIOItems.VIBRANT_ALLOY_BALL.get(), 1.75F, 1.35F, 1.13F, 80000, pFinishedRecipeConsumer);
        build(EIOItems.REDSTONE_ALLOY_BALL.get(), 1.00F, 1.00F, 0.35F, 30000, pFinishedRecipeConsumer);
        build(EIOItems.CONDUCTIVE_ALLOY_BALL.get(), 1.35F, 1.00F, 1.0F, 40000, pFinishedRecipeConsumer);
        build(EIOItems.PULSATING_ALLOY_BALL.get(), 1.00F, 1.85F, 1.0F, 100000, pFinishedRecipeConsumer);
        build(EIOItems.SOULARIUM_BALL.get(), 1.2F, 2.15F, 0.9F, 80000, pFinishedRecipeConsumer);
        build(EIOItems.END_STEEL_BALL.get(), 1.4F, 2.4F, 0.7F, 75000, pFinishedRecipeConsumer);
    }

    protected void build(Item item, float grinding, float chance, float power, int durability, Consumer<FinishedRecipe> recipeConsumer) {
        recipeConsumer.accept(new FinishedGrindingBall(EnderIO.loc("grindingball/" + ForgeRegistries.ITEMS.getKey(item).getPath()), item, grinding, chance, power, durability));
    }

    protected static class FinishedGrindingBall extends EnderFinishedRecipe {

        private final Item item;
        private final float mainOutput;
        private final float bonusOutput;
        private final float powerUse;
        private final int durability;

        public FinishedGrindingBall(ResourceLocation id, Item item, float mainOutput, float bonusOutput, float powerUse, int durability) {
            super(id);
            this.item = item;
            this.mainOutput = mainOutput;
            this.bonusOutput = bonusOutput;
            this.powerUse = powerUse;
            this.durability = durability;
        }

        @Override
        public void serializeRecipeData(JsonObject json) {
            json.addProperty("item", ForgeRegistries.ITEMS.getKey(item).toString());
            json.addProperty("grinding", mainOutput);
            json.addProperty("chance", bonusOutput);
            json.addProperty("power", powerUse);
            json.addProperty("durability", durability);

            super.serializeRecipeData(json);
        }

        @Override
        protected Set<String> getModDependencies() {
            return Set.of(ForgeRegistries.ITEMS.getKey(item).getNamespace());
        }

        @Override
        public RecipeSerializer<?> getType() {
            return EIORecipes.GRINDING_BALL.serializer().get();
        }
    }

}

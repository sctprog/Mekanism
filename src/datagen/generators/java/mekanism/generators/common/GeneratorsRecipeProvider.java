package mekanism.generators.common;

import java.util.function.Consumer;
import javax.annotation.ParametersAreNonnullByDefault;
import mekanism.api.datagen.recipe.RecipeCriterion;
import mekanism.api.datagen.recipe.builder.MetallurgicInfuserRecipeBuilder;
import mekanism.api.recipes.inputs.InfusionIngredient;
import mekanism.api.recipes.inputs.ItemStackIngredient;
import mekanism.common.recipe.BaseRecipeProvider;
import mekanism.common.recipe.Criterion;
import mekanism.common.recipe.Pattern;
import mekanism.common.recipe.RecipePattern;
import mekanism.common.recipe.RecipePattern.TripleLine;
import mekanism.common.recipe.builder.ExtendedShapedRecipeBuilder;
import mekanism.common.registries.MekanismBlocks;
import mekanism.common.registries.MekanismItems;
import mekanism.common.tags.MekanismTags;
import mekanism.generators.common.registries.GeneratorsBlocks;
import mekanism.generators.common.registries.GeneratorsItems;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.item.Items;
import net.minecraft.tags.ItemTags;
import net.minecraftforge.common.Tags;

@ParametersAreNonnullByDefault
public class GeneratorsRecipeProvider extends BaseRecipeProvider {

    private static final char GLASS_CHAR = 'G';
    private static final char IRON_BARS_CHAR = 'B';
    private static final char BUCKET_CHAR = 'B';
    private static final char BIO_FUEL_CHAR = 'B';
    private static final char FRAME_CHAR = 'F';
    private static final char ELECTROLYTIC_CORE_CHAR = 'C';
    private static final char COPPER_CHAR = 'C';
    private static final char FURNACE_CHAR = 'F';

    public GeneratorsRecipeProvider(DataGenerator gen) {
        super(gen, MekanismGenerators.MODID);
    }

    @Override
    protected void registerRecipes(Consumer<IFinishedRecipe> consumer) {
        addGeneratorRecipes(consumer);
        addReactorRecipes(consumer);
        addTurbineRecipes(consumer);
    }

    private void addGeneratorRecipes(Consumer<IFinishedRecipe> consumer) {
        //Solar panel (item component)
        ExtendedShapedRecipeBuilder.shapedRecipe(GeneratorsItems.SOLAR_PANEL)
              .pattern(RecipePattern.createPattern(
                    TripleLine.of(GLASS_CHAR, GLASS_CHAR, GLASS_CHAR),
                    TripleLine.of(Pattern.REDSTONE, Pattern.ALLOY, Pattern.REDSTONE),
                    TripleLine.of(Pattern.OSMIUM, Pattern.OSMIUM, Pattern.OSMIUM))
              ).key(GLASS_CHAR, Tags.Items.GLASS_PANES)
              .key(Pattern.OSMIUM, MekanismTags.Items.INGOTS_OSMIUM)
              .key(Pattern.REDSTONE, Tags.Items.DUSTS_REDSTONE)
              .key(Pattern.ALLOY, MekanismTags.Items.ALLOYS_INFUSED)
              .addCriterion(Criterion.HAS_INFUSED_ALLOY)
              .build(consumer);
        //Solar Generator
        ExtendedShapedRecipeBuilder.shapedRecipe(GeneratorsBlocks.SOLAR_GENERATOR)
              .pattern(RecipePattern.createPattern(
                    TripleLine.of(Pattern.CONSTANT, Pattern.CONSTANT, Pattern.CONSTANT),
                    TripleLine.of(Pattern.ALLOY, Pattern.INGOT, Pattern.ALLOY),
                    TripleLine.of(Pattern.OSMIUM, Pattern.ENERGY, Pattern.OSMIUM))
              ).key(Pattern.CONSTANT, GeneratorsItems.SOLAR_PANEL)
              .key(Pattern.ALLOY, MekanismTags.Items.ALLOYS_INFUSED)
              .key(Pattern.INGOT, Tags.Items.INGOTS_IRON)
              .key(Pattern.ENERGY, MekanismItems.ENERGY_TABLET)
              .key(Pattern.OSMIUM, MekanismTags.Items.INGOTS_OSMIUM)
              .addCriterion(Criterion.has(GeneratorsItems.SOLAR_PANEL))
              .build(consumer, MekanismGenerators.rl("generator/solar"));
        //Advanced Solar Generator
        ExtendedShapedRecipeBuilder.shapedRecipe(GeneratorsBlocks.ADVANCED_SOLAR_GENERATOR)
              .pattern(RecipePattern.createPattern(
                    TripleLine.of(Pattern.CONSTANT, Pattern.ALLOY, Pattern.CONSTANT),
                    TripleLine.of(Pattern.CONSTANT, Pattern.ALLOY, Pattern.CONSTANT),
                    TripleLine.of(Pattern.INGOT, Pattern.INGOT, Pattern.INGOT))
              ).key(Pattern.CONSTANT, GeneratorsBlocks.SOLAR_GENERATOR)
              .key(Pattern.ALLOY, MekanismTags.Items.ALLOYS_INFUSED)
              .key(Pattern.INGOT, Tags.Items.INGOTS_IRON)
              .addCriterion(Criterion.has(GeneratorsBlocks.SOLAR_GENERATOR))
              .build(consumer, MekanismGenerators.rl("generator/advanced_solar"));
        //Bio
        ExtendedShapedRecipeBuilder.shapedRecipe(GeneratorsBlocks.BIO_GENERATOR)
              .pattern(RecipePattern.createPattern(
                    TripleLine.of(Pattern.REDSTONE, Pattern.ALLOY, Pattern.REDSTONE),
                    TripleLine.of(BIO_FUEL_CHAR, Pattern.CIRCUIT, BIO_FUEL_CHAR),
                    TripleLine.of(Pattern.INGOT, Pattern.ALLOY, Pattern.INGOT))
              ).key(Pattern.REDSTONE, Tags.Items.DUSTS_REDSTONE)
              .key(Pattern.ALLOY, MekanismTags.Items.ALLOYS_INFUSED)
              .key(Pattern.INGOT, Tags.Items.INGOTS_IRON)
              .key(Pattern.CIRCUIT, MekanismTags.Items.CIRCUITS_BASIC)
              .key(BIO_FUEL_CHAR, MekanismTags.Items.FUELS_BIO)
              .addCriterion(Criterion.HAS_ENERGY_TABLET)
              .addCriterion(Criterion.HAS_BIO_FUEL)
              .build(consumer, MekanismGenerators.rl("generator/bio"));
        //Gas Burning
        ExtendedShapedRecipeBuilder.shapedRecipe(GeneratorsBlocks.GAS_BURNING_GENERATOR)
              .pattern(RecipePattern.createPattern(
                    TripleLine.of(Pattern.OSMIUM, Pattern.ALLOY, Pattern.OSMIUM),
                    TripleLine.of(Pattern.STEEL_CASING, ELECTROLYTIC_CORE_CHAR, Pattern.STEEL_CASING),
                    TripleLine.of(Pattern.OSMIUM, Pattern.ALLOY, Pattern.OSMIUM))
              ).key(ELECTROLYTIC_CORE_CHAR, MekanismItems.ELECTROLYTIC_CORE)
              .key(Pattern.ALLOY, MekanismTags.Items.ALLOYS_INFUSED)
              .key(Pattern.STEEL_CASING, MekanismBlocks.STEEL_CASING)
              .key(Pattern.OSMIUM, MekanismTags.Items.INGOTS_OSMIUM)
              .addCriterion(Criterion.HAS_ELECTROLYTIC_CORE)
              .addCriterion(Criterion.HAS_STEEL_CASING)
              .addCriterion(Criterion.HAS_INFUSED_ALLOY)
              .build(consumer, MekanismGenerators.rl("generator/gas_burning"));
        //Heat
        ExtendedShapedRecipeBuilder.shapedRecipe(GeneratorsBlocks.HEAT_GENERATOR)
              .pattern(RecipePattern.createPattern(
                    TripleLine.of(Pattern.INGOT, Pattern.INGOT, Pattern.INGOT),
                    TripleLine.of(Pattern.WOOD, Pattern.OSMIUM, Pattern.WOOD),
                    TripleLine.of(COPPER_CHAR, FURNACE_CHAR, COPPER_CHAR))
              ).key(Pattern.WOOD, ItemTags.PLANKS)
              .key(Pattern.INGOT, Tags.Items.INGOTS_IRON)
              .key(Pattern.OSMIUM, MekanismTags.Items.INGOTS_OSMIUM)
              .key(COPPER_CHAR, MekanismTags.Items.INGOTS_COPPER)
              .key(FURNACE_CHAR, Items.FURNACE)
              .addCriterion(Criterion.HAS_OSMIUM)
              .addCriterion(Criterion.HAS_COPPER)
              .addCriterion(Criterion.has(Items.FURNACE))
              .build(consumer, MekanismGenerators.rl("generator/heat"));
        //Wind
        ExtendedShapedRecipeBuilder.shapedRecipe(GeneratorsBlocks.WIND_GENERATOR)
              .pattern(RecipePattern.createPattern(
                    TripleLine.of(Pattern.EMPTY, Pattern.OSMIUM, Pattern.EMPTY),
                    TripleLine.of(Pattern.OSMIUM, Pattern.ALLOY, Pattern.OSMIUM),
                    TripleLine.of(Pattern.ENERGY, Pattern.CIRCUIT, Pattern.ENERGY))
              ).key(Pattern.ALLOY, MekanismTags.Items.ALLOYS_INFUSED)
              .key(Pattern.CIRCUIT, MekanismTags.Items.CIRCUITS_BASIC)
              .key(Pattern.ENERGY, MekanismItems.ENERGY_TABLET)
              .key(Pattern.OSMIUM, MekanismTags.Items.INGOTS_OSMIUM)
              .addCriterion(Criterion.HAS_ENERGY_TABLET)
              .addCriterion(Criterion.HAS_BASIC_CIRCUIT)
              .addCriterion(Criterion.HAS_INFUSED_ALLOY)
              .addCriterion(Criterion.HAS_OSMIUM)
              .build(consumer, MekanismGenerators.rl("generator/wind"));
    }

    private void addReactorRecipes(Consumer<IFinishedRecipe> consumer) {
        //Hohlraum
        MetallurgicInfuserRecipeBuilder.metallurgicInfusing(
              ItemStackIngredient.from(MekanismTags.Items.DUSTS_GOLD, 4),
              InfusionIngredient.from(MekanismTags.InfuseTypes.CARBON, 10),
              GeneratorsItems.HOHLRAUM.getItemStack()
        ).addCriterion(Criterion.HAS_METALLURGIC_INFUSER)
              .build(consumer);
        //Laser Focus Matrix
        ExtendedShapedRecipeBuilder.shapedRecipe(GeneratorsBlocks.LASER_FOCUS_MATRIX, 2)
              .pattern(RecipePattern.createPattern(
                    TripleLine.of(Pattern.EMPTY, GLASS_CHAR, Pattern.EMPTY),
                    TripleLine.of(GLASS_CHAR, Pattern.REDSTONE, GLASS_CHAR),
                    TripleLine.of(Pattern.EMPTY, GLASS_CHAR, Pattern.EMPTY))
              ).key(GLASS_CHAR, GeneratorsBlocks.REACTOR_GLASS)
              .key(Pattern.REDSTONE, Tags.Items.STORAGE_BLOCKS_REDSTONE)
              .addCriterion(Criterion.has(GeneratorsBlocks.REACTOR_GLASS))
              .build(consumer);
        //Frame
        ExtendedShapedRecipeBuilder.shapedRecipe(GeneratorsBlocks.REACTOR_FRAME, 4)
              .pattern(RecipePattern.createPattern(
                    TripleLine.of(Pattern.EMPTY, Pattern.STEEL_CASING, Pattern.EMPTY),
                    TripleLine.of(Pattern.STEEL_CASING, Pattern.ALLOY, Pattern.STEEL_CASING),
                    TripleLine.of(Pattern.EMPTY, Pattern.STEEL_CASING, Pattern.EMPTY))
              ).key(Pattern.STEEL_CASING, MekanismBlocks.STEEL_CASING)
              .key(Pattern.ALLOY, MekanismTags.Items.ALLOYS_ATOMIC)
              .addCriterion(Criterion.HAS_ATOMIC_ALLOY)
              .addCriterion(Criterion.HAS_STEEL_CASING)
              .build(consumer, MekanismGenerators.rl("reactor/frame"));
        RecipeCriterion hasFrame = Criterion.has(GeneratorsBlocks.REACTOR_FRAME);
        //Glass
        ExtendedShapedRecipeBuilder.shapedRecipe(GeneratorsBlocks.REACTOR_GLASS, 4)
              .pattern(RecipePattern.createPattern(
                    TripleLine.of(Pattern.EMPTY, FRAME_CHAR, Pattern.EMPTY),
                    TripleLine.of(FRAME_CHAR, GLASS_CHAR, FRAME_CHAR),
                    TripleLine.of(Pattern.EMPTY, FRAME_CHAR, Pattern.EMPTY))
              ).key(GLASS_CHAR, Tags.Items.GLASS)
              .key(FRAME_CHAR, GeneratorsBlocks.REACTOR_FRAME)
              .addCriterion(hasFrame)
              .build(consumer, MekanismGenerators.rl("reactor/glass"));
        //Port
        ExtendedShapedRecipeBuilder.shapedRecipe(GeneratorsBlocks.REACTOR_PORT, 2)
              .pattern(RecipePattern.createPattern(
                    TripleLine.of(Pattern.EMPTY, FRAME_CHAR, Pattern.EMPTY),
                    TripleLine.of(FRAME_CHAR, Pattern.CIRCUIT, FRAME_CHAR),
                    TripleLine.of(Pattern.EMPTY, FRAME_CHAR, Pattern.EMPTY))
              ).key(Pattern.CIRCUIT, MekanismTags.Items.CIRCUITS_ULTIMATE)
              .key(FRAME_CHAR, GeneratorsBlocks.REACTOR_FRAME)
              .addCriterion(hasFrame)
              .addCriterion(Criterion.HAS_ULTIMATE_CIRCUIT)
              .build(consumer, MekanismGenerators.rl("reactor/port"));
        //Logic Adapter
        ExtendedShapedRecipeBuilder.shapedRecipe(GeneratorsBlocks.REACTOR_LOGIC_ADAPTER)
              .pattern(RecipePattern.createPattern(
                    TripleLine.of(Pattern.EMPTY, Pattern.REDSTONE, Pattern.EMPTY),
                    TripleLine.of(Pattern.REDSTONE, FRAME_CHAR, Pattern.REDSTONE),
                    TripleLine.of(Pattern.EMPTY, Pattern.REDSTONE, Pattern.EMPTY))
              ).key(FRAME_CHAR, GeneratorsBlocks.REACTOR_FRAME)
              .key(Pattern.REDSTONE, Tags.Items.DUSTS_REDSTONE)
              .addCriterion(hasFrame)
              .build(consumer, MekanismGenerators.rl("reactor/logic_adapter"));
        //Controller
        ExtendedShapedRecipeBuilder.shapedRecipe(GeneratorsBlocks.REACTOR_CONTROLLER)
              .pattern(RecipePattern.createPattern(
                    TripleLine.of(Pattern.CIRCUIT, GLASS_CHAR, Pattern.CIRCUIT),
                    TripleLine.of(FRAME_CHAR, Pattern.TANK, FRAME_CHAR),
                    TripleLine.of(FRAME_CHAR, FRAME_CHAR, FRAME_CHAR))
              ).key(Pattern.CIRCUIT, MekanismTags.Items.CIRCUITS_ULTIMATE)
              .key(GLASS_CHAR, Tags.Items.GLASS_PANES)
              .key(FRAME_CHAR, GeneratorsBlocks.REACTOR_FRAME)
              .key(Pattern.TANK, MekanismBlocks.BASIC_GAS_TANK)
              .addCriterion(hasFrame)
              .build(consumer, MekanismGenerators.rl("reactor/controller"));
    }

    private void addTurbineRecipes(Consumer<IFinishedRecipe> consumer) {
        //Electromagnetic Coil
        ExtendedShapedRecipeBuilder.shapedRecipe(GeneratorsBlocks.ELECTROMAGNETIC_COIL)
              .pattern(RecipePattern.createPattern(
                    TripleLine.of(Pattern.STEEL, Pattern.INGOT, Pattern.STEEL),
                    TripleLine.of(Pattern.INGOT, Pattern.ENERGY, Pattern.INGOT),
                    TripleLine.of(Pattern.STEEL, Pattern.INGOT, Pattern.STEEL))
              ).key(Pattern.STEEL, MekanismTags.Items.INGOTS_STEEL)
              .key(Pattern.INGOT, Tags.Items.INGOTS_GOLD)
              .key(Pattern.ENERGY, MekanismItems.ENERGY_TABLET)
              .addCriterion(Criterion.HAS_STEEL)
              .addCriterion(Criterion.HAS_ENERGY_TABLET)
              .build(consumer);
        //Rotational Complex
        ExtendedShapedRecipeBuilder.shapedRecipe(GeneratorsBlocks.ROTATIONAL_COMPLEX)
              .pattern(RecipePattern.createPattern(
                    TripleLine.of(Pattern.STEEL, Pattern.ALLOY, Pattern.STEEL),
                    TripleLine.of(Pattern.CIRCUIT, Pattern.ALLOY, Pattern.CIRCUIT),
                    TripleLine.of(Pattern.STEEL, Pattern.ALLOY, Pattern.STEEL))
              ).key(Pattern.STEEL, MekanismTags.Items.INGOTS_STEEL)
              .key(Pattern.ALLOY, MekanismTags.Items.ALLOYS_INFUSED)
              .key(Pattern.CIRCUIT, MekanismTags.Items.CIRCUITS_ADVANCED)
              .addCriterion(Criterion.HAS_STEEL)
              .addCriterion(Criterion.HAS_ADVANCED_CIRCUIT)
              .addCriterion(Criterion.HAS_INFUSED_ALLOY)
              .build(consumer);
        //Saturating Condenser
        ExtendedShapedRecipeBuilder.shapedRecipe(GeneratorsBlocks.SATURATING_CONDENSER)
              .pattern(RecipePattern.createPattern(
                    TripleLine.of(Pattern.STEEL, Pattern.INGOT, Pattern.STEEL),
                    TripleLine.of(Pattern.INGOT, BUCKET_CHAR, Pattern.INGOT),
                    TripleLine.of(Pattern.STEEL, Pattern.INGOT, Pattern.STEEL))
              ).key(Pattern.STEEL, MekanismTags.Items.INGOTS_STEEL)
              .key(Pattern.INGOT, MekanismTags.Items.INGOTS_TIN)
              .key(BUCKET_CHAR, Items.BUCKET)
              .addCriterion(Criterion.HAS_STEEL)
              .addCriterion(Criterion.HAS_TIN)
              .build(consumer);
        //Blade
        ExtendedShapedRecipeBuilder.shapedRecipe(GeneratorsItems.TURBINE_BLADE)
              .pattern(RecipePattern.createPattern(
                    TripleLine.of(Pattern.EMPTY, Pattern.STEEL, Pattern.EMPTY),
                    TripleLine.of(Pattern.STEEL, Pattern.ALLOY, Pattern.STEEL),
                    TripleLine.of(Pattern.EMPTY, Pattern.STEEL, Pattern.EMPTY))
              ).key(Pattern.STEEL, MekanismTags.Items.INGOTS_STEEL)
              .key(Pattern.ALLOY, MekanismTags.Items.ALLOYS_INFUSED)
              .addCriterion(Criterion.HAS_INFUSED_ALLOY)
              .addCriterion(Criterion.HAS_STEEL)
              .build(consumer, MekanismGenerators.rl("turbine/blade"));
        //Rotor
        ExtendedShapedRecipeBuilder.shapedRecipe(GeneratorsBlocks.TURBINE_ROTOR)
              .pattern(RecipePattern.createPattern(
                    TripleLine.of(Pattern.STEEL, Pattern.ALLOY, Pattern.STEEL),
                    TripleLine.of(Pattern.STEEL, Pattern.ALLOY, Pattern.STEEL),
                    TripleLine.of(Pattern.STEEL, Pattern.ALLOY, Pattern.STEEL))
              ).key(Pattern.STEEL, MekanismTags.Items.INGOTS_STEEL)
              .key(Pattern.ALLOY, MekanismTags.Items.ALLOYS_INFUSED)
              .addCriterion(Criterion.HAS_INFUSED_ALLOY)
              .addCriterion(Criterion.HAS_STEEL)
              .build(consumer, MekanismGenerators.rl("turbine/rotor"));
        //Casing
        ExtendedShapedRecipeBuilder.shapedRecipe(GeneratorsBlocks.TURBINE_CASING, 4)
              .pattern(RecipePattern.createPattern(
                    TripleLine.of(Pattern.EMPTY, Pattern.STEEL, Pattern.EMPTY),
                    TripleLine.of(Pattern.STEEL, Pattern.OSMIUM, Pattern.STEEL),
                    TripleLine.of(Pattern.EMPTY, Pattern.STEEL, Pattern.EMPTY))
              ).key(Pattern.OSMIUM, MekanismTags.Items.INGOTS_OSMIUM)
              .key(Pattern.STEEL, MekanismTags.Items.INGOTS_STEEL)
              .addCriterion(Criterion.HAS_STEEL)
              .addCriterion(Criterion.HAS_OSMIUM)
              .build(consumer, MekanismGenerators.rl("turbine/casing"));
        RecipeCriterion hasCasing = Criterion.has(GeneratorsBlocks.TURBINE_CASING);
        //Valve
        ExtendedShapedRecipeBuilder.shapedRecipe(GeneratorsBlocks.TURBINE_VALVE, 2)
              .pattern(RecipePattern.createPattern(
                    TripleLine.of(Pattern.EMPTY, Pattern.CONSTANT, Pattern.EMPTY),
                    TripleLine.of(Pattern.CONSTANT, Pattern.CIRCUIT, Pattern.CONSTANT),
                    TripleLine.of(Pattern.EMPTY, Pattern.CONSTANT, Pattern.EMPTY))
              ).key(Pattern.CONSTANT, GeneratorsBlocks.TURBINE_CASING)
              .key(Pattern.CIRCUIT, MekanismTags.Items.CIRCUITS_ADVANCED)
              .addCriterion(Criterion.HAS_ADVANCED_CIRCUIT)
              .addCriterion(hasCasing)
              .build(consumer, MekanismGenerators.rl("turbine/valve"));
        //Vent
        ExtendedShapedRecipeBuilder.shapedRecipe(GeneratorsBlocks.TURBINE_VENT, 2)
              .pattern(RecipePattern.createPattern(
                    TripleLine.of(Pattern.EMPTY, Pattern.CONSTANT, Pattern.EMPTY),
                    TripleLine.of(Pattern.CONSTANT, IRON_BARS_CHAR, Pattern.CONSTANT),
                    TripleLine.of(Pattern.EMPTY, Pattern.CONSTANT, Pattern.EMPTY))
              ).key(Pattern.CONSTANT, GeneratorsBlocks.TURBINE_CASING)
              .key(IRON_BARS_CHAR, Items.IRON_BARS)
              .addCriterion(hasCasing)
              .build(consumer, MekanismGenerators.rl("turbine/vent"));
    }
}
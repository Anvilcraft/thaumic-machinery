package net.anvilcraft.thaummach;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

public class RecipesCrucible {
    private static final RecipesCrucible smeltingBase = new RecipesCrucible();
    private Map<Item, Float> smeltingList = new HashMap<>();
    private Map<Pair<Item, Integer>, Float> metaSmeltingList = new HashMap<>();

    static {
        smeltingBase.initSmelting();
    }

    public static final RecipesCrucible smelting() {
        return smeltingBase;
    }

    private RecipesCrucible() {}

    public void initSmelting() {
        //this.addSmelting(mod_ThaumCraft.itemComponents, 6, 35.0F);
        //this.addSmelting(mod_ThaumCraft.itemComponents, 10, 30.0F);
        //this.addSmelting(mod_ThaumCraft.blockCustomWood, 0, 25.0F);
        //this.addSmelting(mod_ThaumCraft.blockCustomWood, 1, 10.0F);

        //int a;
        //for (a = 5; a <= 9; ++a) {
        //    this.addSmelting(mod_ThaumCraft.blockCustomWood, a, 2.5F);
        //}

        //this.addSmelting(mod_ThaumCraft.blockCustomOre, 16.0F);
        //this.addSmelting(mod_ThaumCraft.itemComponents, 0, 17.0F);
        //this.addSmelting(mod_ThaumCraft.itemComponents, 1, 25.0F);
        //this.addSmelting(mod_ThaumCraft.itemComponents, 2, 9.0F);
        //this.addSmelting(mod_ThaumCraft.itemComponents, 8, 16.0F);
        //this.addSmelting(mod_ThaumCraft.itemComponents, 12, 16.0F);
        //this.addSmelting(mod_ThaumCraft.itemComponents, 19, 16.0F);

        //for (a = 0; a < 5; ++a) {
        //    this.addSmelting(mod_ThaumCraft.itemCrystals, a, 25.0F);
        //}

        //this.addSmelting(mod_ThaumCraft.itemCrystals, 5, 9.0F);
        //this.addSmelting(mod_ThaumCraft.blockOreCrystals, 5, 9.0F);
        //this.addSmelting(mod_ThaumCraft.itemCrystals, 6, 4.0F);
        //this.addSmelting(mod_ThaumCraft.itemTheory, 20.0F);
        //this.addSmelting(mod_ThaumCraft.itemDiscovery, 50.0F);
        this.addSmelting(Items.stick, 0.25F);
        this.addSmelting(Items.clay_ball, 1.0F);
        this.addSmelting(Items.brick, 2.0F);
        this.addSmelting(Items.flint, 1.0F);
        this.addSmelting(Items.coal, 2.0F);
        this.addSmelting(Items.snowball, 0.25F);
        this.addSmelting(Items.dye, 0, 4.0F);
        this.addSmelting(Items.dye, 2, 4.0F);
        this.addSmelting(Items.dye, 3, 25.0F);
        this.addSmelting(Items.dye, 4, 9.0F);
        this.addSmelting(Items.iron_ingot, 5.0F);
        this.addSmelting(Items.wheat_seeds, 4.0F);
        this.addSmelting(Items.feather, 4.0F);
        this.addSmelting(Items.bone, 4.0F);
        this.addSmelting(Items.melon, 2.0F);
        this.addSmelting(Items.cooked_beef, 5.0F);
        this.addSmelting(Items.beef, 4.0F);
        this.addSmelting(Items.cooked_chicken, 5.0F);
        this.addSmelting(Items.chicken, 4.0F);
        this.addSmelting(Items.cooked_porkchop, 5.0F);
        this.addSmelting(Items.porkchop, 4.0F);
        this.addSmelting(Items.cooked_fished, 5.0F);
        this.addSmelting(Items.fish, 4.0F);
        this.addSmelting(Items.nether_wart, 4.0F);
        this.addSmelting(Items.rotten_flesh, 4.0F);
        this.addSmelting(Items.string, 4.0F);
        this.addSmelting(Items.leather, 4.0F);
        this.addSmelting(Items.wheat, 4.0F);
        this.addSmelting(Items.reeds, 4.0F);
        this.addSmelting(Items.gunpowder, 10.0F);
        this.addSmelting(Items.glowstone_dust, 9.0F);
        this.addSmelting(Items.redstone, 6.0F);
        this.addSmelting(Items.milk_bucket, 18.0F);
        this.addSmelting(Items.water_bucket, 16.0F);
        this.addSmelting(Items.lava_bucket, 17.0F);
        this.addSmelting(Items.egg, 5.0F);
        this.addSmelting(Items.gold_ingot, 27.0F);
        this.addSmelting(Items.gold_nugget, 3.0F);
        this.addSmelting(Items.slime_ball, 25.0F);
        this.addSmelting(Items.apple, 4.0F);
        this.addSmelting(Items.diamond, 64.0F);
        this.addSmelting(Items.ender_pearl, 64.0F);
        this.addSmelting(Items.record_13, 100.0F);
        this.addSmelting(Items.record_cat, 100.0F);
        this.addSmelting(Items.record_11, 100.0F);
        this.addSmelting(Items.record_chirp, 100.0F);
        this.addSmelting(Items.record_far, 100.0F);
        this.addSmelting(Items.record_mall, 100.0F);
        this.addSmelting(Items.record_mellohi, 100.0F);
        this.addSmelting(Items.record_stal, 100.0F);
        this.addSmelting(Items.record_strad, 100.0F);
        this.addSmelting(Items.record_ward, 100.0F);
        this.addSmelting(Items.blaze_rod, 36.0F);
        this.addSmelting(Items.ghast_tear, 64.0F);
        this.addSmelting(Items.spider_eye, 9.0F);
        this.addSmelting(Items.saddle, 64.0F);

        this.addSmelting(Blocks.cobblestone, 0.1F);
        this.addSmelting(Blocks.planks, 0.5F);
        this.addSmelting(Blocks.mossy_cobblestone, 4.0F);
        this.addSmelting(Blocks.sand, 1.0F);
        this.addSmelting(Blocks.sandstone, 1.0F);
        this.addSmelting(Blocks.dirt, 1.0F);
        this.addSmelting(Blocks.grass, 1.0F);
        this.addSmelting(Blocks.glass, 1.0F);
        this.addSmelting(Blocks.ice, 1.0F);
        this.addSmelting(Blocks.gravel, 1.0F);
        this.addSmelting(Blocks.stone, 1.0F);
        this.addSmelting(Blocks.waterlily, 3.0F);
        this.addSmelting(Blocks.web, 4.0F);
        this.addSmelting(Blocks.nether_brick, 2.0F);
        this.addSmelting(Blocks.end_stone, 2.0F);
        this.addSmelting(Blocks.stonebrick, 1, 1.0F);
        this.addSmelting(Blocks.stonebrick, 2, 1.0F);
        this.addSmelting(Blocks.netherrack, 1.0F);
        this.addSmelting(Blocks.soul_sand, 2.0F);
        this.addSmelting(Blocks.coal_ore, 2.0F);
        this.addSmelting(Blocks.wool, 4.0F);
        this.addSmelting(Blocks.planks, 2.0F);
        this.addSmelting(Blocks.leaves, 2.0F);
        this.addSmelting(Blocks.tallgrass, 2.0F);
        this.addSmelting(Blocks.deadbush, 2.0F);
        this.addSmelting(Blocks.cactus, 2.0F);
        this.addSmelting(Blocks.sapling, 2.0F);
        this.addSmelting(Blocks.mycelium, 3.0F);
        this.addSmelting(Blocks.iron_ore, 4.0F);
        this.addSmelting(Blocks.yellow_flower, 4.0F);
        this.addSmelting(Blocks.red_flower, 4.0F);
        this.addSmelting(Blocks.brown_mushroom, 4.0F);
        this.addSmelting(Blocks.red_mushroom, 4.0F);
        this.addSmelting(Blocks.vine, 4.0F);
        this.addSmelting(Blocks.pumpkin, 4.0F);
        this.addSmelting(Blocks.reeds, 4.0F);
        this.addSmelting(Blocks.redstone_ore, 16.0F);
        this.addSmelting(Blocks.lapis_ore, 9.0F);
        this.addSmelting(Blocks.gold_ore, 25.0F);
        this.addSmelting(Blocks.obsidian, 16.0F);
        this.addSmelting(Blocks.diamond_ore, 64.0F);
    }

    public void addSmelting(Item item, float vis) {
        this.smeltingList.put(item, vis);
    }

    public void addSmelting(Block block, float vis) {
        this.addSmelting(Item.getItemFromBlock(block), vis);
    }

    public void addSmelting(Item item, int meta, float vis) {
        this.metaSmeltingList.put(new ImmutablePair<Item, Integer>(item, meta), vis);
    }

    public void addSmelting(Block block, int meta, float vis) {
        this.addSmelting(Item.getItemFromBlock(block), meta, vis);
    }

    public float getSmeltingResult(ItemStack src, boolean first, boolean basiconly) {
        if (src == null) {
            return 0.0F;
        } else if (this.metaSmeltingList.get(new ImmutablePair<>(src.getItem(), src.getItemDamage())) != null) {
            return this.metaSmeltingList.get(
                new ImmutablePair<>(src.getItem(), src.getItemDamage())
            );
        } else if (this.smeltingList.get(src.getItem()) != null) {
            return this.smeltingList.get(src.getItem());
        }

        return 0.0f;
    }

    //private float recipeCost(ItemStack src) {
    //    float totalCost = 0.0F;
    //    ItemStack[] comps;
    //    int q;
    //    if (RecipesInfuser.infusing().getInfusingCost(src) > 0) {
    //        totalCost += (float) RecipesInfuser.infusing().getInfusingCost(src);
    //        comps = RecipesInfuser.infusing().getInfusingComponents(src);

    //        for (q = 0; q < comps.length; ++q) {
    //            totalCost += this.getSmeltingResult(comps[q], true, false);
    //        }
    //    } else if (RecipesInfuser.infusing().getInfusingCost(src, true) > 0) {
    //        totalCost += (float) RecipesInfuser.infusing().getInfusingCost(src, true);
    //        comps = RecipesInfuser.infusing().getInfusingComponents(src, true);

    //        for (q = 0; q < comps.length; ++q) {
    //            totalCost += this.getSmeltingResult(comps[q], true, false);
    //        }
    //    } else {
    //        for (int q = 0; q < ThaumCraftApi.recipeList.size(); ++q) {
    //            int idR;
    //            int idS;
    //            int i;
    //            if (ThaumCraftApi.recipeList.get(q) instanceof ShapedRecipes) {
    //                ShapedRecipes shapedRecipe
    //                    = (ShapedRecipes) ThaumCraftApi.recipeList.get(q);
    //                idR = shapedRecipe.getRecipeOutput().getItemDamage() < 0
    //                    ? 0
    //                    : shapedRecipe.getRecipeOutput().getItemDamage();
    //                idS = src.getItemDamage() < 0 ? 0 : src.getItemDamage();
    //                if (shapedRecipe.getRecipeOutput().itemID == src.itemID
    //                    && idR == idS) {
    //                    try {
    //                        int width = (Integer) ModLoader.getPrivateValue(
    //                            ShapedRecipes.class, shapedRecipe, 0
    //                        );
    //                        i = (Integer) ModLoader.getPrivateValue(
    //                            ShapedRecipes.class, shapedRecipe, 1
    //                        );
    //                        ItemStack[] items
    //                            = (ItemStack[]) ((ItemStack[])
    //                            ModLoader.getPrivateValue(
    //                                ShapedRecipes.class, shapedRecipe, 2
    //                            ));

    //                        for (int i = 0; i < width && i < 3; ++i) {
    //                            for (int j = 0; j < i && j < 3; ++j) {
    //                                if (items[i + j * width] != null) {
    //                                    float c = this.getSmeltingResult(
    //                                                  items[i + j * width], false, false
    //                                              )
    //                                        / (float) shapedRecipe.getRecipeOutput()
    //                                              .stackSize;
    //                                    if (c == 0.0F) {
    //                                        return 0.0F;
    //                                    }

    //                                    totalCost += c;
    //                                }
    //                            }
    //                        }
    //                    } catch (Exception var13) {
    //                        var13.printStackTrace();
    //                    }

    //                    q = ThaumCraftApi.recipeList.size();
    //                }
    //            } else if (ThaumCraftApi.recipeList.get(q) instanceof ShapelessRecipes)
    //            {
    //                ShapelessRecipes shapelessRecipe
    //                    = (ShapelessRecipes) ThaumCraftApi.recipeList.get(q);
    //                idR = shapelessRecipe.getRecipeOutput().getItemDamage() < 0
    //                    ? 0
    //                    : shapelessRecipe.getRecipeOutput().getItemDamage();
    //                idS = src.getItemDamage() < 0 ? 0 : src.getItemDamage();
    //                if (shapelessRecipe.getRecipeOutput().itemID == src.itemID
    //                    && idR == idS) {
    //                    try {
    //                        List items = (List) ModLoader.getPrivateValue(
    //                            ShapelessRecipes.class, shapelessRecipe, 1
    //                        );

    //                        for (i = 0; i < items.size() && i < 9; ++i) {
    //                            if (items.get(i) != null) {
    //                                float c = this.getSmeltingResult(
    //                                              (ItemStack) items.get(i), false, false
    //                                          )
    //                                    / (float) shapelessRecipe.getRecipeOutput()
    //                                          .stackSize;
    //                                if (c == 0.0F) {
    //                                    return 0.0F;
    //                                }

    //                                totalCost += c;
    //                            }
    //                        }
    //                    } catch (Exception var14) {
    //                        var14.printStackTrace();
    //                    }

    //                    q = ThaumCraftApi.recipeList.size();
    //                }
    //            }
    //        }
    //    }

    //    return totalCost;
    //}
}

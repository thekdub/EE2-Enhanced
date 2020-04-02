package ee;

import ic2.api.Items;
import net.minecraft.server.Block;
import net.minecraft.server.Item;
import net.minecraft.server.ItemStack;
import net.minecraft.server.ModLoader;

public class EEAddonIC2 {
  public EEAddonIC2() {
  }

  public static void initialize() {
    try {
      EEMaps.addAlchemicalValue(Items.getItem("copperIngot"), (EEMaps.getEMC(Item.IRON_INGOT.id) - 1) / 3);
      EEMaps.addAlchemicalValue(Items.getItem("copperDust"), (EEMaps.getEMC(Item.IRON_INGOT.id) - 1) / 3);
      EEMaps.addAlchemicalValue(Items.getItem("tinIngot"), EEMaps.getEMC(Item.IRON_INGOT.id));
      EEMaps.addAlchemicalValue(Items.getItem("tinDust"), EEMaps.getEMC(Item.IRON_INGOT.id));
      EEMaps.addAlchemicalValue(Items.getItem("bronzeIngot"), (EEMaps.getEMC(Items.getItem("copperIngot")) * 3 + EEMaps.getEMC(Items.getItem("tinIngot"))) / 2);
      EEMaps.addAlchemicalValue(Items.getItem("bronzeDust"), (EEMaps.getEMC(Items.getItem("copperDust")) * 3 + EEMaps.getEMC(Items.getItem("tinDust"))) / 2);
      EEMaps.addAlchemicalValue(Items.getItem("ironDust"), EEMaps.getEMC(Item.IRON_INGOT.id));
      EEMaps.addAlchemicalValue(Items.getItem("goldDust"), EEMaps.getEMC(Item.IRON_INGOT.id) * 8);
      EEMaps.addAlchemicalValue(Items.getItem("silverDust"), EEMaps.getEMC(Item.IRON_INGOT.id) * 2);
      EEMaps.addAlchemicalValue(Items.getItem("resin"), EEMaps.getEMC(Item.SLIME_BALL.id));
      EEMaps.addAlchemicalValue(Items.getItem("rubberWood"), EEMaps.getEMC(Block.LOG.id));
      EEMaps.addAlchemicalValue(Items.getItem("rubberSapling"), EEMaps.getEMC(Block.LOG.id));
      EEMaps.addAlchemicalValue(Items.getItem("rubberLeaves"), EEMaps.getEMC(Block.LEAVES.id));
      EEMaps.addAlchemicalValue(Items.getItem("lavaCell"), EEMaps.getEMC(Item.LAVA_BUCKET.id) - EEMaps.getEMC(Item.BUCKET.id) + EEMaps.getEMC(Items.getItem("tinIngot")) / 4);
      EEMaps.addAlchemicalValue(Items.getItem("uraniumDrop"), EEMaps.getEMC(new ItemStack(EEItem.mobiusFuel)) * 2);
      EEMaps.addChargedItem(Items.getItem("constructionFoamSprayer"));
      EEMaps.addChargedItem(Items.getItem("miningDrill"));
      EEMaps.addChargedItem(Items.getItem("diamondDrill"));
      EEMaps.addChargedItem(Items.getItem("chainsaw"));
      EEMaps.addChargedItem(Items.getItem("electricWrench"));
      EEMaps.addChargedItem(Items.getItem("electricTreetap"));
      EEMaps.addChargedItem(Items.getItem("miningLaser"));
      EEMaps.addChargedItem(Items.getItem("odScanner"));
      EEMaps.addChargedItem(Items.getItem("ovScanner"));
      EEMaps.addChargedItem(Items.getItem("nanoSaber"));
      EEMaps.addChargedItem(Items.getItem("enabledNanoSaber"));
      EEMaps.addChargedItem(Items.getItem("nanoHelmet"));
      EEMaps.addChargedItem(Items.getItem("nanoBodyarmor"));
      EEMaps.addChargedItem(Items.getItem("nanoLeggings"));
      EEMaps.addChargedItem(Items.getItem("nanoBoots"));
      EEMaps.addChargedItem(Items.getItem("quantumHelmet"));
      EEMaps.addChargedItem(Items.getItem("quantumBodyarmor"));
      EEMaps.addChargedItem(Items.getItem("quantumLeggings"));
      EEMaps.addChargedItem(Items.getItem("quantumBoots"));
      EEMaps.addChargedItem(Items.getItem("jetpack"));
      EEMaps.addChargedItem(Items.getItem("electricJetpack"));
      EEMaps.addChargedItem(Items.getItem("batPack"));
      EEMaps.addChargedItem(Items.getItem("lapPack"));
      EEMaps.addChargedItem(Items.getItem("cfPack"));
      EEMaps.addChargedItem(Items.getItem("reBattery"));
      EEMaps.addChargedItem(Items.getItem("chargedReBattery"));
      EEMaps.addChargedItem(Items.getItem("energyCrystal"));
      EEMaps.addChargedItem(Items.getItem("lapotronCrystal"));
      EEMaps.addChargedItem(Items.getItem("filledFuelCan"));
      EEMaps.addChargedItem(Items.getItem("uraniumCell"));
      EEMaps.addChargedItem(Items.getItem("coolingCell"));
      EEMaps.addChargedItem(Items.getItem("depletedIsotopeCell"));
      EEMaps.addChargedItem(Items.getItem("integratedReactorPlating"));
      EEMaps.addChargedItem(Items.getItem("integratedHeatDisperser"));
      EEMaps.addChargedItem(Items.getItem("hydratingCell"));
      EEMaps.addChargedItem(Items.getItem("electricHoe"));
      EEMaps.AddRepairRecipe(getRepairable(Items.getItem("treetap")), new Object[]{EEMaps.lcov()});
      EEMaps.AddRepairRecipe(getRepairable(Items.getItem("rubberBoots")), new Object[]{EEMaps.lcov()});
      EEMaps.AddRepairRecipe(getRepairable(Items.getItem("bronzePickaxe")), new Object[]{EEMaps.mcov(), EEMaps.mcov(), EEMaps.mcov()});
      EEMaps.AddRepairRecipe(getRepairable(Items.getItem("bronzeAxe")), new Object[]{EEMaps.mcov(), EEMaps.mcov(), EEMaps.mcov()});
      EEMaps.AddRepairRecipe(getRepairable(Items.getItem("bronzeSword")), new Object[]{EEMaps.mcov(), EEMaps.mcov()});
      EEMaps.AddRepairRecipe(getRepairable(Items.getItem("bronzeShovel")), new Object[]{EEMaps.mcov()});
      EEMaps.AddRepairRecipe(getRepairable(Items.getItem("bronzeHoe")), new Object[]{EEMaps.mcov(), EEMaps.mcov()});
      EEMaps.AddRepairRecipe(getRepairable(Items.getItem("bronzeHelmet")), new Object[]{EEMaps.mcov(), EEMaps.mcov(), EEMaps.mcov(), EEMaps.mcov(), EEMaps.mcov()});
      EEMaps.AddRepairRecipe(getRepairable(Items.getItem("bronzeChestplate")), new Object[]{EEMaps.mcov(), EEMaps.mcov(), EEMaps.mcov(), EEMaps.mcov(), EEMaps.mcov(), EEMaps.mcov(), EEMaps.mcov(), EEMaps.mcov()});
      EEMaps.AddRepairRecipe(getRepairable(Items.getItem("bronzeLeggings")), new Object[]{EEMaps.mcov(), EEMaps.mcov(), EEMaps.mcov(), EEMaps.mcov(), EEMaps.mcov(), EEMaps.mcov(), EEMaps.mcov()});
      EEMaps.AddRepairRecipe(getRepairable(Items.getItem("bronzeBoots")), new Object[]{EEMaps.mcov(), EEMaps.mcov(), EEMaps.mcov(), EEMaps.mcov()});
      EEMaps.AddRepairRecipe(getRepairable(Items.getItem("compositeArmor")), new Object[]{EEMaps.mcov(), EEMaps.mcov(), EEMaps.mcov(), EEMaps.mcov(), EEMaps.mcov(), EEMaps.mcov(), EEMaps.mcov(), EEMaps.mcov()});
      EEMaps.AddRepairRecipe(getRepairable(Items.getItem("wrench")), new Object[]{EEMaps.mcov()});
      EEMaps.AddRepairRecipe(getRepairable(Items.getItem("cutter")), new Object[]{EEMaps.mcov()});
      EEMaps.addOreBlock(Items.getItem("copperOre"));
      EEMaps.addOreBlock(Items.getItem("tinOre"));
      EEMaps.addOreBlock(Items.getItem("uraniumOre"));
      EEMaps.addLeafBlock(Items.getItem("rubberLeaves"));
      EEMaps.addWoodBlock(Items.getItem("rubberWood"));
      if (Items.getItem("lavaCell") != null) {
        EEMaps.addFuelItem(Items.getItem("lavaCell").id);
      }

      if (Items.getItem("uraniumDrop") != null) {
        EEMaps.addFuelItem(Items.getItem("uraniumDrop").id);
      }

      ModLoader.getLogger().fine("[EE2] Loaded EE2-IC2 Addon");
    } catch (Exception var1) {
      ModLoader.getLogger().warning("[EE2] Could not load EE2-IC2 Addon");
      var1.printStackTrace(System.err);
    }

  }

  public static ItemStack getRepairable(ItemStack var0) {
    return var0 == null ? null : new ItemStack(var0.id, 1, -1);
  }
}
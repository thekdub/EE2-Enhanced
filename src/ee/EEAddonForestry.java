package ee;

import forestry.api.core.ForestryBlock;
import forestry.api.core.ItemInterface;
import net.minecraft.server.Item;
import net.minecraft.server.ItemStack;
import net.minecraft.server.ModLoader;

public class EEAddonForestry {
  private static final int APATITE = 0;
  private static final int COPPER = 1;
  private static final int TIN = 2;

  public EEAddonForestry() {
  }

  public static void initialize() {
    try {
      EEMaps.addAlchemicalValue(ItemInterface.getItem("apatite"), 192);
      EEMaps.addEMC(ItemInterface.getItem("beeComb").getItem().id, 11, EEMaps.getEMC(Item.DIAMOND.id));
      EEMaps.addAlchemicalValue(ItemInterface.getItem("ingotCopper"), (EEMaps.getEMC(Item.IRON_INGOT.id) - 1) / 3);
      EEMaps.addAlchemicalValue(ItemInterface.getItem("ingotTin"), EEMaps.getEMC(Item.IRON_INGOT.id));
      EEMaps.addAlchemicalValue(ItemInterface.getItem("ingotBronze"), (EEMaps.getEMC(ItemInterface.getItem("ingotCopper")) * 3 + 3 + EEMaps.getEMC(ItemInterface.getItem("ingotTin"))) / 4);
      EEMaps.addOreBlock(new ItemStack(ForestryBlock.resources, 1, 0));
      EEMaps.addOreBlock(new ItemStack(ForestryBlock.resources, 1, 1));
      EEMaps.addOreBlock(new ItemStack(ForestryBlock.resources, 1, 2));
      EEMaps.addEMC(ItemInterface.getItem("honeyDrop").getItem().id, 1, EEMaps.getEMC(Item.DIAMOND.id));
      ModLoader.getLogger().fine("[EE2] Loaded EE2-Forestry Addon");
    } catch (Exception var1) {
      ModLoader.getLogger().warning("[EE2] Could not load EE2-Forestry Addon");
      var1.printStackTrace(System.err);
    }

  }
}
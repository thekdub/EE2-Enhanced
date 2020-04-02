package ee;

import net.minecraft.server.Block;
import net.minecraft.server.Item;
import net.minecraft.server.ItemStack;
import net.minecraft.server.ModLoader;

public class EEAddonRP2 {
  public static boolean rp2BaseIsInstalled;
  public static boolean rp2WorldIsInstalled;
  public static Block rp2Ores = null;
  public static Block rp2Leaves = null;
  public static Block rp2Logs = null;
  public static Block rp2Stone = null;
  public static Item rp2Resources = null;
  public static Block rp2Plants = null;
  public static Item rp2Seeds = null;
  public static Item rp2PickaxeRuby = null;
  public static Item rp2PickaxeSapphire = null;
  public static Item rp2PickaxeEmerald = null;
  public static Item rp2ShovelRuby = null;
  public static Item rp2ShovelSapphire = null;
  public static Item rp2ShovelEmerald = null;
  public static Item rp2AxeRuby = null;
  public static Item rp2AxeSapphire = null;
  public static Item rp2AxeEmerald = null;
  public static Item rp2SwordRuby = null;
  public static Item rp2SwordSapphire = null;
  public static Item rp2SwordEmerald = null;
  public static Item rp2HoeRuby = null;
  public static Item rp2HoeSapphire = null;
  public static Item rp2HoeEmerald = null;
  public static Item rp2SickleWood = null;
  public static Item rp2SickleStone = null;
  public static Item rp2SickleIron = null;
  public static Item rp2SickleGold = null;
  public static Item rp2SickleDiamond = null;
  public static Item rp2SickleRuby = null;
  public static Item rp2SickleSapphire = null;
  public static Item rp2SickleEmerald = null;
  public static Item rp2HandsawIron = null;
  public static Item rp2HandsawDiamond = null;
  public static Item rp2HandsawRuby = null;
  public static Item rp2HandsawSapphire = null;
  public static Item rp2HandsawEmerald = null;
  public static Item rp2IndigoDye = null;

  public EEAddonRP2() {
  }

  public static void initBase() {
    try {
      rp2Resources = (Item) Class.forName("net.minecraft.server.RedPowerBase").getField("itemResource").get((Object) null);
      rp2HandsawIron = (Item) Class.forName("net.minecraft.server.RedPowerBase").getField("itemHandsawIron").get((Object) null);
      rp2HandsawDiamond = (Item) Class.forName("net.minecraft.server.RedPowerBase").getField("itemHandsawDiamond").get((Object) null);
      rp2IndigoDye = (Item) Class.forName("net.minecraft.server.RedPowerBase").getField("itemDyeIndigo").get((Object) null);
      EEMaps.addEMC(rp2Resources.id, 0, EEMaps.getEMC(Item.DIAMOND.id) / 8);
      EEMaps.addEMC(rp2Resources.id, 1, EEMaps.getEMC(Item.DIAMOND.id) / 8);
      EEMaps.addEMC(rp2Resources.id, 2, EEMaps.getEMC(Item.DIAMOND.id) / 8);
      EEMaps.addEMC(rp2HandsawIron.id, EEMaps.getEMC(Item.IRON_INGOT.id) * 4 + EEMaps.getEMC(Item.STICK.id) * 3);
      EEMaps.addEMC(rp2HandsawDiamond.id, EEMaps.getEMC(Item.DIAMOND.id) * 2 + EEMaps.getEMC(Item.IRON_INGOT.id) * 2 + EEMaps.getEMC(Item.STICK.id) * 3);
      EEMaps.addEMC(rp2Resources.id, 3, EEMaps.getEMC(Item.IRON_INGOT.id) * 2);
      EEMaps.addEMC(rp2Resources.id, 4, EEMaps.getEMC(Item.IRON_INGOT.id));
      EEMaps.addEMC(rp2Resources.id, 5, (EEMaps.getEMC(Item.IRON_INGOT.id) - 1) / 3);
      EEMaps.addEMC(rp2Resources.id, 6, EEMaps.getEMC(Item.REDSTONE.id) * 2);
      EEMaps.addEMC(rp2IndigoDye.id, EEMaps.getEMC(Item.INK_SACK.id, 0));
      EEMaps.addMeta(rp2Resources.id, 6);
      EEMaps.addFuelItem(rp2Resources.id, 6);
      rp2BaseIsInstalled = true;
      ModLoader.getLogger().fine("[EE2] Loaded EE2-RP2 Core Addon");
    } catch (Exception var1) {
      rp2BaseIsInstalled = false;
      ModLoader.getLogger().warning("[EE2] Could not load EE2-RP2 Core Addon");
      var1.printStackTrace(System.err);
    }
  }

  public static void initWorld() {
    try {
      rp2Ores = (Block) Class.forName("net.minecraft.server.RedPowerWorld").getField("blockOres").get((Object) null);
      rp2Leaves = (Block) Class.forName("net.minecraft.server.RedPowerWorld").getField("blockLeaves").get((Object) null);
      rp2Logs = (Block) Class.forName("net.minecraft.server.RedPowerWorld").getField("blockLogs").get((Object) null);
      rp2Stone = (Block) Class.forName("net.minecraft.server.RedPowerWorld").getField("blockStone").get((Object) null);
      rp2Plants = (Block) Class.forName("net.minecraft.server.RedPowerWorld").getField("blockPlants").get((Object) null);
      rp2Seeds = (Item) Class.forName("net.minecraft.server.RedPowerWorld").getField("itemSeeds").get((Object) null);
      rp2PickaxeRuby = (Item) Class.forName("net.minecraft.server.RedPowerWorld").getField("itemPickaxeRuby").get((Object) null);
      rp2PickaxeSapphire = (Item) Class.forName("net.minecraft.server.RedPowerWorld").getField("itemPickaxeSapphire").get((Object) null);
      rp2PickaxeEmerald = (Item) Class.forName("net.minecraft.server.RedPowerWorld").getField("itemPickaxeEmerald").get((Object) null);
      rp2ShovelRuby = (Item) Class.forName("net.minecraft.server.RedPowerWorld").getField("itemShovelRuby").get((Object) null);
      rp2ShovelSapphire = (Item) Class.forName("net.minecraft.server.RedPowerWorld").getField("itemShovelSapphire").get((Object) null);
      rp2ShovelEmerald = (Item) Class.forName("net.minecraft.server.RedPowerWorld").getField("itemShovelEmerald").get((Object) null);
      rp2AxeRuby = (Item) Class.forName("net.minecraft.server.RedPowerWorld").getField("itemAxeRuby").get((Object) null);
      rp2AxeSapphire = (Item) Class.forName("net.minecraft.server.RedPowerWorld").getField("itemAxeSapphire").get((Object) null);
      rp2AxeEmerald = (Item) Class.forName("net.minecraft.server.RedPowerWorld").getField("itemAxeEmerald").get((Object) null);
      rp2SwordRuby = (Item) Class.forName("net.minecraft.server.RedPowerWorld").getField("itemSwordRuby").get((Object) null);
      rp2SwordSapphire = (Item) Class.forName("net.minecraft.server.RedPowerWorld").getField("itemSwordSapphire").get((Object) null);
      rp2SwordEmerald = (Item) Class.forName("net.minecraft.server.RedPowerWorld").getField("itemSwordEmerald").get((Object) null);
      rp2HoeRuby = (Item) Class.forName("net.minecraft.server.RedPowerWorld").getField("itemHoeRuby").get((Object) null);
      rp2HoeSapphire = (Item) Class.forName("net.minecraft.server.RedPowerWorld").getField("itemHoeSapphire").get((Object) null);
      rp2HoeEmerald = (Item) Class.forName("net.minecraft.server.RedPowerWorld").getField("itemHoeEmerald").get((Object) null);
      rp2SickleWood = (Item) Class.forName("net.minecraft.server.RedPowerWorld").getField("itemSickleWood").get((Object) null);
      rp2SickleStone = (Item) Class.forName("net.minecraft.server.RedPowerWorld").getField("itemSickleStone").get((Object) null);
      rp2SickleIron = (Item) Class.forName("net.minecraft.server.RedPowerWorld").getField("itemSickleIron").get((Object) null);
      rp2SickleGold = (Item) Class.forName("net.minecraft.server.RedPowerWorld").getField("itemSickleGold").get((Object) null);
      rp2SickleDiamond = (Item) Class.forName("net.minecraft.server.RedPowerWorld").getField("itemSickleDiamond").get((Object) null);
      rp2SickleRuby = (Item) Class.forName("net.minecraft.server.RedPowerWorld").getField("itemSickleRuby").get((Object) null);
      rp2SickleEmerald = (Item) Class.forName("net.minecraft.server.RedPowerWorld").getField("itemSickleEmerald").get((Object) null);
      rp2SickleSapphire = (Item) Class.forName("net.minecraft.server.RedPowerWorld").getField("itemSickleSapphire").get((Object) null);
      rp2HandsawSapphire = (Item) Class.forName("net.minecraft.server.RedPowerWorld").getField("itemHandsawSapphire").get((Object) null);
      rp2HandsawRuby = (Item) Class.forName("net.minecraft.server.RedPowerWorld").getField("itemHandsawRuby").get((Object) null);
      rp2HandsawEmerald = (Item) Class.forName("net.minecraft.server.RedPowerWorld").getField("itemHandsawEmerald").get((Object) null);
      EEMaps.addEMC(rp2SickleWood.id, EEMaps.getEMC(Block.WOOD.id, 0) * 3 + EEMaps.getEMC(Item.STICK.id));
      EEMaps.addEMC(rp2SickleStone.id, EEMaps.getEMC(Block.COBBLESTONE.id, 0) * 3 + EEMaps.getEMC(Item.STICK.id));
      EEMaps.addEMC(rp2SickleIron.id, EEMaps.getEMC(Item.IRON_INGOT.id) * 3 + EEMaps.getEMC(Item.STICK.id));
      EEMaps.addEMC(rp2SickleGold.id, EEMaps.getEMC(Item.GOLD_INGOT.id) * 3 + EEMaps.getEMC(Item.STICK.id));
      EEMaps.addEMC(rp2SickleDiamond.id, EEMaps.getEMC(Item.DIAMOND.id) * 3 + EEMaps.getEMC(Item.STICK.id));
      EEMaps.addEMC(rp2SickleRuby.id, EEMaps.getEMC(rp2Resources.id, 0) * 3 + EEMaps.getEMC(Item.STICK.id));
      EEMaps.addEMC(rp2SickleEmerald.id, EEMaps.getEMC(rp2Resources.id, 0) * 3 + EEMaps.getEMC(Item.STICK.id));
      EEMaps.addEMC(rp2SickleSapphire.id, EEMaps.getEMC(rp2Resources.id, 0) * 3 + EEMaps.getEMC(Item.STICK.id));
      EEMaps.addEMC(rp2PickaxeRuby.id, EEMaps.getEMC(rp2Resources.id, 0) * 3 + EEMaps.getEMC(Item.STICK.id) * 2);
      EEMaps.addEMC(rp2PickaxeEmerald.id, EEMaps.getEMC(rp2Resources.id, 0) * 3 + EEMaps.getEMC(Item.STICK.id) * 2);
      EEMaps.addEMC(rp2PickaxeSapphire.id, EEMaps.getEMC(rp2Resources.id, 0) * 3 + EEMaps.getEMC(Item.STICK.id) * 2);
      EEMaps.addEMC(rp2AxeRuby.id, EEMaps.getEMC(rp2Resources.id, 0) * 3 + EEMaps.getEMC(Item.STICK.id) * 2);
      EEMaps.addEMC(rp2AxeEmerald.id, EEMaps.getEMC(rp2Resources.id, 0) * 3 + EEMaps.getEMC(Item.STICK.id) * 2);
      EEMaps.addEMC(rp2AxeSapphire.id, EEMaps.getEMC(rp2Resources.id, 0) * 3 + EEMaps.getEMC(Item.STICK.id) * 2);
      EEMaps.addEMC(rp2HoeRuby.id, EEMaps.getEMC(rp2Resources.id, 0) * 2 + EEMaps.getEMC(Item.STICK.id) * 2);
      EEMaps.addEMC(rp2HoeEmerald.id, EEMaps.getEMC(rp2Resources.id, 0) * 2 + EEMaps.getEMC(Item.STICK.id) * 2);
      EEMaps.addEMC(rp2HoeSapphire.id, EEMaps.getEMC(rp2Resources.id, 0) * 2 + EEMaps.getEMC(Item.STICK.id) * 2);
      EEMaps.addEMC(rp2ShovelRuby.id, EEMaps.getEMC(rp2Resources.id, 0) + EEMaps.getEMC(Item.STICK.id) * 2);
      EEMaps.addEMC(rp2ShovelEmerald.id, EEMaps.getEMC(rp2Resources.id, 0) + EEMaps.getEMC(Item.STICK.id) * 2);
      EEMaps.addEMC(rp2ShovelSapphire.id, EEMaps.getEMC(rp2Resources.id, 0) + EEMaps.getEMC(Item.STICK.id) * 2);
      EEMaps.addEMC(rp2SwordRuby.id, EEMaps.getEMC(rp2Resources.id, 0) * 2 + EEMaps.getEMC(Item.STICK.id));
      EEMaps.addEMC(rp2SwordEmerald.id, EEMaps.getEMC(rp2Resources.id, 0) * 2 + EEMaps.getEMC(Item.STICK.id));
      EEMaps.addEMC(rp2SwordSapphire.id, EEMaps.getEMC(rp2Resources.id, 0) * 2 + EEMaps.getEMC(Item.STICK.id));
      EEMaps.addEMC(rp2HandsawSapphire.id, EEMaps.getEMC(Item.IRON_INGOT.id) * 2 + EEMaps.getEMC(rp2Resources.id, 0) * 2 + EEMaps.getEMC(Item.STICK.id) * 3);
      EEMaps.addEMC(rp2HandsawEmerald.id, EEMaps.getEMC(Item.IRON_INGOT.id) * 2 + EEMaps.getEMC(rp2Resources.id, 0) * 2 + EEMaps.getEMC(Item.STICK.id) * 3);
      EEMaps.addEMC(rp2HandsawRuby.id, EEMaps.getEMC(Item.IRON_INGOT.id) * 2 + EEMaps.getEMC(rp2Resources.id, 0) * 2 + EEMaps.getEMC(Item.STICK.id) * 3);
      EEMaps.AddRepairRecipe(getRepairable(rp2PickaxeRuby), new Object[]{EEMaps.mcov(), EEMaps.mcov(), EEMaps.mcov()});
      EEMaps.AddRepairRecipe(getRepairable(rp2PickaxeEmerald), new Object[]{EEMaps.mcov(), EEMaps.mcov(), EEMaps.mcov()});
      EEMaps.AddRepairRecipe(getRepairable(rp2PickaxeSapphire), new Object[]{EEMaps.mcov(), EEMaps.mcov(), EEMaps.mcov()});
      EEMaps.AddRepairRecipe(getRepairable(rp2AxeRuby), new Object[]{EEMaps.mcov(), EEMaps.mcov(), EEMaps.mcov()});
      EEMaps.AddRepairRecipe(getRepairable(rp2AxeEmerald), new Object[]{EEMaps.mcov(), EEMaps.mcov(), EEMaps.mcov()});
      EEMaps.AddRepairRecipe(getRepairable(rp2AxeSapphire), new Object[]{EEMaps.mcov(), EEMaps.mcov(), EEMaps.mcov()});
      EEMaps.AddRepairRecipe(getRepairable(rp2SwordRuby), new Object[]{EEMaps.mcov(), EEMaps.mcov()});
      EEMaps.AddRepairRecipe(getRepairable(rp2SwordEmerald), new Object[]{EEMaps.mcov(), EEMaps.mcov()});
      EEMaps.AddRepairRecipe(getRepairable(rp2SwordSapphire), new Object[]{EEMaps.mcov(), EEMaps.mcov()});
      EEMaps.AddRepairRecipe(getRepairable(rp2HoeRuby), new Object[]{EEMaps.mcov(), EEMaps.mcov()});
      EEMaps.AddRepairRecipe(getRepairable(rp2HoeEmerald), new Object[]{EEMaps.mcov(), EEMaps.mcov()});
      EEMaps.AddRepairRecipe(getRepairable(rp2HoeSapphire), new Object[]{EEMaps.mcov(), EEMaps.mcov()});
      EEMaps.AddRepairRecipe(getRepairable(rp2ShovelRuby), new Object[]{EEMaps.mcov()});
      EEMaps.AddRepairRecipe(getRepairable(rp2ShovelEmerald), new Object[]{EEMaps.mcov()});
      EEMaps.AddRepairRecipe(getRepairable(rp2ShovelSapphire), new Object[]{EEMaps.mcov()});
      EEMaps.AddRepairRecipe(getRepairable(rp2SickleWood), new Object[]{EEMaps.lcov(), EEMaps.lcov(), EEMaps.lcov()});
      EEMaps.AddRepairRecipe(getRepairable(rp2SickleStone), new Object[]{EEMaps.lcov(), EEMaps.lcov(), EEMaps.lcov()});
      EEMaps.AddRepairRecipe(getRepairable(rp2SickleIron), new Object[]{EEMaps.mcov(), EEMaps.mcov(), EEMaps.mcov()});
      EEMaps.AddRepairRecipe(getRepairable(rp2SickleGold), new Object[]{EEMaps.mcov(), EEMaps.mcov(), EEMaps.mcov()});
      EEMaps.AddRepairRecipe(getRepairable(rp2SickleRuby), new Object[]{EEMaps.mcov(), EEMaps.mcov(), EEMaps.mcov()});
      EEMaps.AddRepairRecipe(getRepairable(rp2SickleEmerald), new Object[]{EEMaps.mcov(), EEMaps.mcov(), EEMaps.mcov()});
      EEMaps.AddRepairRecipe(getRepairable(rp2SickleSapphire), new Object[]{EEMaps.mcov(), EEMaps.mcov(), EEMaps.mcov()});
      EEMaps.AddRepairRecipe(getRepairable(rp2SickleDiamond), new Object[]{EEMaps.hcov(), EEMaps.hcov(), EEMaps.hcov()});
      EEMaps.addEMC(rp2Stone.id, 0, EEMaps.getEMC(Block.COBBLESTONE.id));
      EEMaps.addEMC(rp2Stone.id, 1, EEMaps.getEMC(Block.COBBLESTONE.id));
      EEMaps.addEMC(rp2Stone.id, 2, EEMaps.getEMC(Block.COBBLESTONE.id));
      EEMaps.addEMC(rp2Stone.id, 3, EEMaps.getEMC(Block.COBBLESTONE.id));
      EEMaps.addEMC(rp2Stone.id, 4, EEMaps.getEMC(Block.COBBLESTONE.id));
      EEMaps.addMeta(rp2Stone.id, 4);
      EEMaps.addEMC(rp2Seeds.id, 4);
      EEMaps.addEMC(rp2Leaves.id, 0, EEMaps.getEMC(Block.LEAVES.id));
      EEMaps.addEMC(rp2Logs.id, 0, EEMaps.getEMC(Block.LOG.id));
      EEMaps.addEMC(rp2Plants.id, EEMaps.getEMC(Block.RED_ROSE.id));
      EEMaps.addEMC(rp2Ores.id, 6, EEMaps.getEMC(Item.DIAMOND.id) * 2);
      EEMaps.addOreBlock(rp2Ores.id);
      EEMaps.addLeafBlock(rp2Leaves.id);
      EEMaps.addWoodBlock(rp2Logs.id);
      rp2WorldIsInstalled = true;
      ModLoader.getLogger().fine("[EE2] Loaded EE2-RP2 World Addon");
    } catch (Exception var1) {
      rp2WorldIsInstalled = false;
      ModLoader.getLogger().warning("[EE2] Could not load EE2-RP2 World Addon");
      var1.printStackTrace(System.err);
    }
  }

  public static ItemStack getRepairable(Item var0) {
    return var0 == null ? null : new ItemStack(var0, 1, -1);
  }
}
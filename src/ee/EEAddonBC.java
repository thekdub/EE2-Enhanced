package ee;

import net.minecraft.server.Item;
import net.minecraft.server.ModLoader;

public class EEAddonBC {
  public static boolean bcEnergyInstalled;
  public static Item bcItemBucketOil = null;

  public EEAddonBC() {
  }

  public static void initialize() {
    try {
      bcItemBucketOil = (Item) Class.forName("net.minecraft.server.BuildCraftEnergy").getField("bucketOil").get((Object) null);
      EEMaps.addEMC(bcItemBucketOil.id, EEMaps.getEMC(Item.BUCKET.id) + EEMaps.getEMC(Item.GOLD_INGOT.id));
      if (bcItemBucketOil != null) {
        EEMaps.addFuelItem(bcItemBucketOil.id);
      }

      bcEnergyInstalled = true;
    } catch (Exception var1) {
      bcEnergyInstalled = false;
      ModLoader.getLogger().warning("[EE2] Could not load EE2-BC Energy Addon");
      var1.printStackTrace(System.err);
    }

  }
}

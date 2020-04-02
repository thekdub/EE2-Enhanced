package ee;

import forge.ICraftingHandler;
import forge.MinecraftForge;
import net.minecraft.server.*;

import java.io.File;
import java.util.HashMap;
import java.util.List;

public class EEBase {
  public static HashMap playerSwordMode = new HashMap();
  public static HashMap playerWatchCycle = new HashMap();
  public static HashMap playerBuildMode = new HashMap();
  public static HashMap playerInWater = new HashMap();
  public static HashMap playerInLava = new HashMap();
  public static HashMap playerHammerMode = new HashMap();
  public static HashMap playerArmorOffensiveToggle = new HashMap();
  public static HashMap playerArmorMovementToggle = new HashMap();
  public static HashMap playerToggleCooldown = new HashMap();
  public static HashMap playerToolMode = new HashMap();
  public static HashMap playerWatchMagnitude = new HashMap();
  public static HashMap playerLeftClick = new HashMap();
  public static HashMap playerTransGridOpen = new HashMap();
  public static HashMap playerItemCharging = new HashMap();
  public static HashMap playerEffectDurations = new HashMap();
  public static HashMap playerKnowledge = new HashMap();
  private static BaseMod instance;
  private static EEBase eeBaseInstance;
  public static boolean initialized = false;
  public static EEProps props;
  public static int playerWoftFactor = 1;
  private static boolean leftClickWasDown;
  private static boolean extraKeyWasDown;
  private static boolean releaseKeyWasDown;
  private static boolean chargeKeyWasDown;
  private static boolean toggleKeyWasDown;
  public static boolean externalModsInitialized;
  public static int alchChestFront = 0;
  public static int alchChestSide = 1;
  public static int alchChestTop = 2;
  public static int condenserFront = 3;
  public static int condenserSide = 4;
  public static int condenserTop = 5;
  public static int relayFront = 6;
  public static int relaySide = 7;
  public static int relayTop = 8;
  public static int collectorFront = 9;
  public static int collectorSide = 10;
  public static int collectorTop = 11;
  public static int dmFurnaceFront = 12;
  public static int dmBlockSide = 13;
  public static int rmFurnaceFront = 14;
  public static int rmBlockSide = 15;
  public static int iTorchSide = 16;
  public static int novaCatalystSide = 17;
  public static int novaCataclysmSide = 18;
  public static int novaCatalystTop = 19;
  public static int novaCatalystBottom = 20;
  public static int collector2Top = 21;
  public static int collector3Top = 22;
  public static int relay2Top = 23;
  public static int relay3Top = 24;
  public static int transTabletSide = 25;
  public static int transTabletBottom = 26;
  public static int transTabletTop = 27;
  public static int portalDeviceSide = 28;
  public static int portalDeviceBottom = 29;
  public static int portalDeviceTop = 30;
  public static HashMap pedestalCoords = new HashMap();
  private static int machineFactor;

  public EEBase() {
  }

  public static void init(BaseMod ee2) {
    if (!initialized) {
      initialized = true;
      instance = ee2;
      props = new EEProps((new File("mod_EE.props")).getPath());
      props = EEMaps.InitProps(props);
      props.func_26596_save();
      machineFactor = props.getInt("machineFactor");
      setupCraftHook();
    }

  }

  public int AddFuel(int id, int fuel) {
    if (id == EEItem.alchemicalCoal.id) {
      if (fuel == 0) {
        return 6400;
      }
    }
    else if (id == EEItem.mobiusFuel.id) {
      return 25600;
    }

    return 0;
  }

  public static boolean isCurrentItem(Item item, EntityHuman player) {
    return player.inventory.getItemInHand() == null ? false : player.inventory.getItemInHand().getItem() == item;
  }

  public static boolean isOnQuickBar(Item item, EntityHuman player) {
    for (int itemSlot = 0; itemSlot < 9; ++itemSlot) {
      if (player.inventory.getItem(itemSlot) != null && player.inventory.getItem(itemSlot).getItem() == item) {
        return true;
      }
    }

    return false;
  }

  public static ItemStack[] quickBar(EntityHuman player) {
    ItemStack[] itemStacks = new ItemStack[9];

    for (int itemSlot = 0; itemSlot < 9; ++itemSlot) {
      itemStacks[itemSlot] = player.inventory.items[itemSlot];
    }

    return itemStacks;
  }

  public static boolean EntityHasItemStack(ItemStack itemStack, IInventory inventory) {
    boolean nullData = itemStack.getData() == -1;
    ItemStack[] itemStacks = new ItemStack[40];
    int itemSlot;
    for (itemSlot = 0; itemSlot < inventory.getSize(); ++itemSlot) {
      if (inventory.getItem(itemSlot) != null && (inventory.getItem(itemSlot).doMaterialsMatch(itemStack) || inventory.getItem(itemSlot).getItem() == itemStack.getItem() && nullData)) {
        if (inventory.getItem(itemSlot).count >= itemStack.count) {
          return true;
        }
        itemStacks[itemSlot] = inventory.getItem(itemSlot);
      }
    }
    for (int itemSlot2 = 0; itemSlot2 < inventory.getSize(); ++itemSlot2) {
      if (itemStacks[itemSlot2] != null && (itemStacks[itemSlot2].doMaterialsMatch(itemStack) || itemStacks[itemSlot2].getItem() == itemStack.getItem() && nullData)) {
        itemSlot += itemStacks[itemSlot2].count;
        if (itemSlot >= itemStack.count) {
          return true;
        }
      }
    }
    return false;
  }

  public static boolean HasItemStack(ItemStack itemStack, EntityHuman player) {
    boolean data = itemStack.getData() == -1;
    ItemStack[] itemStacks = new ItemStack[40];
    PlayerInventory playerInventory = player.inventory;
    int itemSlot;
    for (itemSlot = 0; itemSlot < playerInventory.items.length + playerInventory.armor.length; ++itemSlot) {
      if (playerInventory.getItem(itemSlot) != null && (playerInventory.getItem(itemSlot).doMaterialsMatch(itemStack) || playerInventory.getItem(itemSlot).getItem() == itemStack.getItem() && data)) {
        if (playerInventory.getItem(itemSlot).count >= itemStack.count) {
          return true;
        }
        itemStacks[itemSlot] = playerInventory.getItem(itemSlot);
      }
    }
    itemSlot = 0;
    for (int itemSlot2 = 0; itemSlot2 < playerInventory.items.length + playerInventory.armor.length; ++itemSlot2) {
      if (itemStacks[itemSlot2] != null && (itemStacks[itemSlot2].doMaterialsMatch(itemStack) || itemStacks[itemSlot2].getItem() == itemStack.getItem() && data)) {
        itemSlot += itemStacks[itemSlot2].count;
        if (itemSlot >= itemStack.count) {
          return true;
        }
      }
    }
    return false;
  }

  public static int getKleinEnergyForDisplay(ItemStack itemStack) {
    return itemStack == null ? 0 : (itemStack.getItem() instanceof ItemKleinStar ? ((ItemKleinStar) itemStack.getItem()).getKleinPoints(itemStack) : 0);
  }

  public static int getDisplayEnergy(ItemStack itemStack) {
    if (itemStack == null) {
      return 0;
    }
    else if (itemStack.getItem() instanceof ItemEECharged && itemStack.getItem() instanceof ItemTransTablet) {
      ItemEECharged itemEECharged = (ItemEECharged) itemStack.getItem();
      return itemEECharged.getInteger(itemStack, "displayEnergy");
    }
    else {
      return 0;
    }
  }

  public static void setDisplayEnergy(ItemStack itemStack, int displayEnergy) {
    if (itemStack != null && itemStack.getItem() instanceof ItemEECharged && itemStack.getItem() instanceof ItemTransTablet) {
      ItemEECharged var2 = (ItemEECharged) itemStack.getItem();
      var2.setInteger(itemStack, "displayEnergy", displayEnergy);
    }

  }

  public static int getLatentEnergy(ItemStack itemStack) {
    if (itemStack == null) {
      return 0;
    }
    else if (itemStack.getItem() instanceof ItemEECharged && itemStack.getItem() instanceof ItemTransTablet) {
      ItemEECharged var1 = (ItemEECharged) itemStack.getItem();
      return var1.getInteger(itemStack, "latentEnergy");
    }
    else {
      return 0;
    }
  }

  public static void setLatentEnergy(ItemStack itemStack, int latentEnergy) {
    if (itemStack != null && itemStack.getItem() instanceof ItemEECharged && itemStack.getItem() instanceof ItemTransTablet) {
      ItemEECharged var2 = (ItemEECharged) itemStack.getItem();
      var2.setInteger(itemStack, "latentEnergy", latentEnergy);
    }

  }

  public static boolean canIncreaseKleinStarPoints(ItemStack itemStack, World world) {
    if (EEProxy.isClient(world)) {
      return false;
    }
    else {
      byte var2 = 1;
      return itemStack == null ? false : (isKleinStar(itemStack.id) ? itemStack.getData() - var2 != 0 : false);
    }
  }

  public static boolean isKleinStar(int id) {
    return id == EEItem.kleinStar1.id || id == EEItem.kleinStar2.id || id == EEItem.kleinStar3.id || id == EEItem.kleinStar4.id || id == EEItem.kleinStar5.id || id == EEItem.kleinStar6.id;
  }

  public static int getKleinLevel(int id) {
    return id == EEItem.kleinStar1.id ? 1 : (id == EEItem.kleinStar2.id ? 2 : (id == EEItem.kleinStar3.id ? 3 : (id == EEItem.kleinStar4.id ? 4 : (id == EEItem.kleinStar5.id ? 5 : (id == EEItem.kleinStar6.id ? 6 : 0)))));
  }

  public static boolean addKleinStarPoints(ItemStack itemStack, int points, World world) {
    if (EEProxy.isClient(world)) {
      return false;
    }
    else if (itemStack == null) {
      return false;
    }
    else if (!isKleinStar(itemStack.id)) {
      return false;
    }
    else {
      ItemKleinStar var3 = (ItemKleinStar) itemStack.getItem();
      if (var3.getKleinPoints(itemStack) <= var3.getMaxPoints(itemStack) - points) {
        var3.setKleinPoints(itemStack, var3.getKleinPoints(itemStack) + points);
        var3.onUpdate(itemStack);
        return true;
      }
      else {
        return false;
      }
    }
  }

  public static boolean addKleinStarPoints(ItemStack itemStack, int points) {
    if (itemStack == null) {
      return false;
    }
    else if (!isKleinStar(itemStack.id)) {
      return false;
    }
    else {
      ItemKleinStar var2 = (ItemKleinStar) itemStack.getItem();
      if (var2.getKleinPoints(itemStack) <= var2.getMaxPoints(itemStack) - points) {
        var2.setKleinPoints(itemStack, var2.getKleinPoints(itemStack) + points);
        var2.onUpdate(itemStack);
        return true;
      }
      else {
        return false;
      }
    }
  }

  public static boolean takeKleinStarPoints(ItemStack itemStack, int points, World world) {
    if (EEProxy.isClient(world)) {
      return false;
    }
    else if (itemStack == null) {
      return false;
    }
    else if (!isKleinStar(itemStack.id)) {
      return false;
    }
    else {
      ItemKleinStar itemKleinStar = (ItemKleinStar) itemStack.getItem();
      if (itemKleinStar.getKleinPoints(itemStack) >= points) {
        itemKleinStar.setKleinPoints(itemStack, itemKleinStar.getKleinPoints(itemStack) - points);
        itemKleinStar.onUpdate(itemStack);
        return true;
      }
      else {
        return false;
      }
    }
  }

  public static boolean consumeKleinStarPoint(EntityHuman player, int points) {
    if (player == null) {
      return false;
    }
    else if (EEProxy.isClient(player.world)) {
      return false;
    }
    else {
      PlayerInventory playerInventory = player.inventory;
      for (int itemSlot = 0; itemSlot < playerInventory.items.length; ++itemSlot) {
        if (playerInventory.getItem(itemSlot) != null) {
          ItemStack itemStack = playerInventory.getItem(itemSlot);
          if (isKleinStar(itemStack.id) && takeKleinStarPoints(itemStack, points, player.world)) {
            return true;
          }
        }
      }
      return false;
    }
  }

  public static boolean Consume(ItemStack itemStack, EntityHuman player, boolean noFuel) {
    if (player == null) {
      return false;
    }
    else if (EEProxy.isClient(player.world)) {
      return false;
    }
    else {
      int stackSize = itemStack.count;
      int totalItems = 0;
      boolean data = false;
      if (itemStack.getData() == -1) {
        data = true;
      }
      ItemStack[] itemStacks = player.inventory.items;

      int itemSlot;
      for (itemSlot = 0; itemSlot < itemStacks.length; ++itemSlot) {
        if (itemStacks[itemSlot] != null) {
          if (stackSize <= totalItems) {
            break;
          }
          if (itemStacks[itemSlot].doMaterialsMatch(itemStack) || data && itemStacks[itemSlot].id == itemStack.id) {
            totalItems += itemStacks[itemSlot].count;
          }
        }
      }
      if (totalItems < stackSize) {
        return false;
      }
      else {
        totalItems = 0;
        for (itemSlot = 0; itemSlot < itemStacks.length; ++itemSlot) {
          if (itemStacks[itemSlot] != null && (itemStacks[itemSlot].doMaterialsMatch(itemStack) || data && itemStacks[itemSlot].id == itemStack.id)) {
            for (int var8 = itemStacks[itemSlot].count; var8 > 0; --var8) {
              --itemStacks[itemSlot].count;
              if (itemStacks[itemSlot].count == 0) {
                itemStacks[itemSlot] = null;
              }
              ++totalItems;
              if (totalItems >= stackSize) {
                return true;
              }
            }
          }
        }
        if (noFuel) {
          player.a("You don't have enough fuel/klein power to do that.");
        }
        return false;
      }
    }
  }

  public static double direction(EntityHuman player) {
    return player.pitch > -55.0F && player.pitch < 55.0F ? (double) ((MathHelper.floor((double) (player.yaw * 4.0F / 360.0F) + 0.5D) & 3) + 2) : (player.pitch <= -55.0F ? 1.0D : 0.0D);
  }

  public static double heading(EntityHuman player) {
    return (double) ((MathHelper.floor((double) (player.yaw * 4.0F / 360.0F) + 0.5D) & 3) + 2);
  }

  public static double playerX(EntityHuman player) {
    return (double) MathHelper.floor(player.locX);
  }

  public static double playerY(EntityHuman player) {
    return (double) MathHelper.floor(player.locY);
  }

  public static double playerZ(EntityHuman player) {
    return (double) MathHelper.floor(player.locZ);
  }

  public static void doLeftClick(World world, EntityHuman player) {
    if (player.U() != null && player.U().getItem() instanceof ItemEECharged) {
      ((ItemEECharged) player.U().getItem()).doLeftClick(player.U(), world, player);
    }

  }

  public static void doAlternate(World world, EntityHuman player) {
    ItemStack itemStack = player.U();
    if (itemStack == null) {
      armorCheck(player);
    }
    else if (player.U().getItem() instanceof ItemEECharged) {
      ((ItemEECharged) player.U().getItem()).doAlternate(player.U(), world, player);
    }
    else {
      armorCheck(player);
    }

  }

  private static void armorCheck(EntityHuman player) {
    if (hasRedArmor(player) && getPlayerArmorOffensive(player)) {
      Combustion combustion = new Combustion(player.world, player, player.locX, player.locY, player.locZ, 4.0F);
      combustion.doExplosionA();
      combustion.doExplosionB(true);
    }

  }

  private static boolean hasRedArmor(EntityHuman player) {
    return player.inventory.armor[2] != null && player.inventory.armor[2].getItem() instanceof ItemRedArmorPlus;
  }

  public static void doToggle(World world, EntityHuman player) {
    ItemStack itemStack = player.U();
    if (itemStack == null) {
      if (hasMovementArmor(player) && getPlayerToggleCooldown(player) <= 0) {
        updatePlayerArmorMovement(player, true);
        setPlayerToggleCooldown(player, 20);
      }
    }
    else if (player.U().getItem() instanceof ItemEECharged) {
      ((ItemEECharged) player.U().getItem()).doToggle(player.U(), world, player);
    }
    else if (hasMovementArmor(player) && getPlayerToggleCooldown(player) <= 0) {
      updatePlayerArmorMovement(player, true);
      setPlayerToggleCooldown(player, 20);
    }

  }

  public static void doJumpTick(World world, EntityPlayer player) {
    bootsCheck(player);
  }

  private static void bootsCheck(EntityHuman player) {
    if (hasRedBoots(player) && getPlayerArmorMovement(player)) {
      player.motY += 0.1D;
    }

  }

  private static boolean hasRedBoots(EntityHuman player) {
    return player.inventory.armor[0] != null && player.inventory.armor[0].getItem() instanceof ItemRedArmorPlus;
  }

  public static void doSneakTick(World world, EntityPlayer player) {
    greavesCheck(player);
  }

  private static void greavesCheck(EntityHuman player) {
    if (hasRedGreaves(player) && getPlayerArmorOffensive(player)) {
      player.motY -= 0.97D;
      doShockwave(player);
    }

  }

  private static void doShockwave(EntityHuman player) {
    List nearbyLiving = player.world.a(EntityLiving.class, AxisAlignedBB.b(player.locX - 7.0D, player.locY - 7.0D, player.locZ - 7.0D, player.locX + 7.0D, player.locY + 7.0D, player.locZ + 7.0D));
    for (int livingSlot = 0; livingSlot < nearbyLiving.size(); ++livingSlot) {
      Entity entity = (Entity) nearbyLiving.get(livingSlot);
      if (!(entity instanceof EntityHuman)) {
        entity.motX += 0.2D / (entity.locX - player.locX);
        entity.motY += 0.05999999865889549D;
        entity.motZ += 0.2D / (entity.locZ - player.locZ);
      }
    }
    List nearbyArrow = player.world.a(EntityArrow.class, AxisAlignedBB.b((double) ((float) player.locX - 5.0F), player.locY - 5.0D, (double) ((float) player.locZ - 5.0F), (double) ((float) player.locX + 5.0F), player.locY + 5.0D, (double) ((float) player.locZ + 5.0F)));
    for (int arrowSlot = 0; arrowSlot < nearbyArrow.size(); ++arrowSlot) {
      Entity arrow = (Entity) nearbyArrow.get(arrowSlot);
      arrow.motX += 0.2D / (arrow.locX - player.locX);
      arrow.motY += 0.05999999865889549D;
      arrow.motZ += 0.2D / (arrow.locZ - player.locZ);
    }
    List nearbyFireball = player.world.a(EntityFireball.class, AxisAlignedBB.b((double) ((float) player.locX - 5.0F), player.locY - 5.0D, (double) ((float) player.locZ - 5.0F), (double) ((float) player.locX + 5.0F), player.locY + 5.0D, (double) ((float) player.locZ + 5.0F)));
    for (int fireballSlot = 0; fireballSlot < nearbyFireball.size(); ++fireballSlot) {
      Entity fireball = (Entity) nearbyFireball.get(fireballSlot);
      fireball.motX += 0.2D / (fireball.locX - player.locX);
      fireball.motY += 0.05999999865889549D;
      fireball.motZ += 0.2D / (fireball.locZ - player.locZ);
    }
  }

  private static boolean hasRedGreaves(EntityHuman player) {
    return player.inventory.armor[1] != null && player.inventory.armor[1].getItem() instanceof ItemRedArmorPlus;
  }

  public static void doRelease(World world, EntityHuman player) {
    ItemStack itemStack = player.U();
    if (itemStack == null) {
      helmetCheck(player);
    }
    else if (itemStack.getItem() instanceof ItemEECharged) {
      ((ItemEECharged) player.U().getItem()).doRelease(player.U(), world, player);
    }
    else {
      helmetCheck(player);
    }
  }

  private static void helmetCheck(EntityHuman player) {
    if (hasRedHelmet(player) && getPlayerArmorOffensive(player)) {
      float v = 1.0F;
      float pitch = player.lastPitch + (player.pitch - player.lastPitch) * v;
      float yaw = player.lastYaw + (player.yaw - player.lastYaw) * v;
      double x = player.lastX + (player.locX - player.lastX) * (double) v;
      double y = player.lastY + (player.locY - player.lastY) * (double) v + 1.62D - (double) player.height;
      double z = player.lastZ + (player.locZ - player.lastZ) * (double) v;
      Vec3D vec3D = Vec3D.create(x, y, z);
      float var11 = MathHelper.cos(-yaw * 0.01745329F - 3.1415927F);
      float var12 = MathHelper.sin(-yaw * 0.01745329F - 3.1415927F);
      float var13 = -MathHelper.cos(-pitch * 0.01745329F);
      float var14 = MathHelper.sin(-pitch * 0.01745329F);
      float var15 = var12 * var13;
      float var17 = var11 * var13;
      double var18 = 150.0D;
      Vec3D vec3D2 = vec3D.add((double) var15 * var18, (double) var14 * var18, (double) var17 * var18);
      MovingObjectPosition movingObjectPosition = player.world.rayTrace(vec3D, vec3D2, true);
      if (movingObjectPosition == null) {
        return;
      }
      if (movingObjectPosition.type == EnumMovingObjectType.TILE) {
        int movingObjectX = movingObjectPosition.b;
        int movingObjectY = movingObjectPosition.c;
        int movingObjectZ = movingObjectPosition.d;
        player.world.strikeLightning(new EntityWeatherLighting(player.world, (double) movingObjectX, (double) movingObjectY, (double) movingObjectZ));
      }
    }

  }

  private static boolean hasRedHelmet(EntityHuman player) {
    return player.inventory.armor[3] != null && player.inventory.armor[3].getItem() instanceof ItemRedArmorPlus;
  }

  public static void doCharge(World world, EntityHuman player) {
    ItemStack itemStack = player.U();
    if (itemStack == null) {
      if (hasOffensiveArmor(player) && getPlayerToggleCooldown(player) <= 0) {
        updatePlayerArmorOffensive(player, true);
        setPlayerToggleCooldown(player, 20);
      }
    }
    else if (itemStack.getItem() instanceof ItemEECharged) {
      ItemEECharged itemEECharged = (ItemEECharged) itemStack.getItem();
      if (!player.isSneaking()) {
        if (itemEECharged.getMaxCharge() > 0 && itemEECharged.chargeLevel(itemStack) < itemEECharged.getMaxCharge() && itemEECharged.chargeGoal(itemStack) < itemEECharged.getMaxCharge()) {
          itemEECharged.setShort(itemStack, "chargeGoal", itemEECharged.chargeGoal(itemStack) + 1);
        }
      }
      else {
        itemEECharged.doUncharge(itemStack, world, player);
      }
    }
    else if (hasOffensiveArmor(player) && getPlayerToggleCooldown(player) <= 0) {
      updatePlayerArmorOffensive(player, true);
      setPlayerToggleCooldown(player, 20);
    }

  }

  private static boolean hasOffensiveArmor(EntityHuman player) {
    return player.inventory.armor[2] != null && player.inventory.armor[2].getItem() instanceof ItemRedArmorPlus || player.inventory.armor[1] != null && player.inventory.armor[1].getItem() instanceof ItemRedArmorPlus || player.inventory.armor[3] != null && player.inventory.armor[3].getItem() instanceof ItemRedArmorPlus;
  }

  private static boolean hasMovementArmor(EntityHuman player) {
    return player.inventory.armor[0] != null && player.inventory.armor[0].getItem() instanceof ItemRedArmorPlus;
  }

  static boolean isPlayerCharging(EntityHuman player, Item item) {
    return playerItemCharging.get(player) == null ? false : (((HashMap) playerItemCharging.get(player)).get(item) == null ? false : (Boolean) ((HashMap) playerItemCharging.get(player)).get(item));
  }

  public static void updatePlayerEffect(Item item, int effect, EntityHuman player) {
    HashMap effects = new HashMap();
    if (playerEffectDurations.get(player) != null) {
      effects = (HashMap) playerEffectDurations.get(player);
    }

    effects.put(item, effect);
    playerEffectDurations.put(player, effects);
  }

  public static int getPlayerEffect(Item item, EntityHuman player) {
    return playerEffectDurations.get(player) == null ? 0 : (((HashMap) playerEffectDurations.get(player)).get(item) == null ? 0 : (Integer) ((HashMap) playerEffectDurations.get(player)).get(item));
  }

  public static int getPlayerToggleCooldown(EntityHuman player) {
    if (playerToggleCooldown.get(player) == null) {
      playerToggleCooldown.put(player, 0);
    }
    return (Integer) playerToggleCooldown.get(player);
  }

  public static void setPlayerToggleCooldown(EntityHuman player, int cooldown) {
    if (playerToggleCooldown.get(player) == null) {
      playerToggleCooldown.put(player, 0);
    }
    else {
      playerToggleCooldown.put(player, cooldown);
    }
  }

  public static void updatePlayerToggleCooldown(EntityHuman player) {
    if (playerToggleCooldown.get(player) == null) {
      playerToggleCooldown.put(player, 0);
    }
    else {
      playerToggleCooldown.put(player, (Integer) playerToggleCooldown.get(player) - 1);
    }
  }

  public static int getBuildMode(EntityHuman player) {
    if (playerBuildMode.get(player) == null) {
      playerBuildMode.put(player, 0);
    }
    return (Integer) playerBuildMode.get(player);
  }

  public static void updateBuildMode(EntityHuman player) {
    if (playerBuildMode.get(player) == null) {
      playerBuildMode.put(player, 0);
    }
    else if ((Integer) playerBuildMode.get(player) == 3) {
      playerBuildMode.put(player, 0);
    }
    else {
      playerBuildMode.put(player, (Integer) playerBuildMode.get(player) + 1);
    }

    if ((Integer) playerBuildMode.get(player) == 0) {
      player.a("Mercurial Extension mode.");
    }
    else if ((Integer) playerBuildMode.get(player) == 1) {
      player.a("Mercurial Creation mode.");
    }
    else if ((Integer) playerBuildMode.get(player) == 2) {
      player.a("Mercurial Transmute mode.");
    }
    else if ((Integer) playerBuildMode.get(player) == 3) {
      player.a("Mercurial Pillar mode. [Careful!]");
    }

  }

  public static boolean getPlayerArmorOffensive(EntityHuman player) {
    if (playerArmorOffensiveToggle.get(player) == null) {
      playerArmorOffensiveToggle.put(player, false);
    }
    return (Boolean) playerArmorOffensiveToggle.get(player);
  }

  public static void updatePlayerArmorOffensive(EntityHuman player, boolean offensive) {
    if (playerArmorOffensiveToggle.get(player) == null) {
      playerArmorOffensiveToggle.put(player, false);
    }
    else {
      playerArmorOffensiveToggle.put(player, !(Boolean) playerArmorOffensiveToggle.get(player));
    }

    if ((Boolean) playerArmorOffensiveToggle.get(player)) {
      if (offensive) {
        player.a("Armor offensive powers on.");
      }
    }
    else if (offensive) {
      player.a("Armor offensive powers off.");
    }
  }

  public static boolean getPlayerArmorMovement(EntityHuman player) {
    if (playerArmorMovementToggle.get(player) == null) {
      playerArmorMovementToggle.put(player, false);
    }
    return (Boolean) playerArmorMovementToggle.get(player);
  }

  public static void updatePlayerArmorMovement(EntityHuman player, boolean movement) {
    if (playerArmorMovementToggle.get(player) == null) {
      playerArmorMovementToggle.put(player, false);
    }
    else {
      playerArmorMovementToggle.put(player, !(Boolean) playerArmorMovementToggle.get(player));
    }

    if ((Boolean) playerArmorMovementToggle.get(player)) {
      if (movement) {
        player.a("Armor movement powers on.");
      }
    }
    else if (movement) {
      player.a("Armor movement powers off.");
    }

  }

  public static boolean getHammerMode(EntityHuman player) {
    if (playerHammerMode.get(player) == null) {
      playerHammerMode.put(player, false);
      return false;
    }
    else {
      return (Boolean) playerHammerMode.get(player);
    }
  }

  public static void updateHammerMode(EntityHuman player, boolean hammerMode) {
    if (playerHammerMode.get(player) == null) {
      playerHammerMode.put(player, false);
    }
    else {
      playerHammerMode.put(player, !(Boolean) playerHammerMode.get(player));
    }
    if ((Boolean) playerHammerMode.get(player)) {
      if (hammerMode) {
        player.a("Hammer mega-impact mode.");
      }
    }
    else if (hammerMode) {
      player.a("Hammer normal-impact mode.");
    }
  }

  public static boolean getSwordMode(EntityHuman player) {
    if (playerSwordMode.get(player) == null) {
      playerSwordMode.put(player, false);
      return false;
    }
    else {
      return (Boolean) playerSwordMode.get(player);
    }
  }

  public static void updateSwordMode(EntityHuman player) {
    if (playerSwordMode.get(player) == null) {
      playerSwordMode.put(player, false);
    }
    else {
      playerSwordMode.put(player, !(Boolean) playerSwordMode.get(player));
    }

    if ((Boolean) playerSwordMode.get(player)) {
      player.a("Sword AoE will harm peaceful/aggressive.");
    }
    else {
      player.a("Sword AoE will harm aggressive only.");
    }

  }

  public static int getWatchCycle(EntityHuman player) {
    if (playerWatchCycle.get(player) == null) {
      playerWatchCycle.put(player, 0);
      return 0;
    }
    else {
      return (Integer) playerWatchCycle.get(player);
    }
  }

  public static void updateWatchCycle(EntityHuman player) {
    if (playerWatchCycle.get(player) == null) {
      playerWatchCycle.put(player, 0);
    }
    else if ((Integer) playerWatchCycle.get(player) == 2) {
      playerWatchCycle.put(player, 0);
    }
    else {
      playerWatchCycle.put(player, (Integer) playerWatchCycle.get(player) + 1);
    }

    if ((Integer) playerWatchCycle.get(player) == 0) {
      player.a("Sun-scroll is off.");
    }

    if ((Integer) playerWatchCycle.get(player) == 1) {
      player.a("Sun-scrolling forward.");
    }

    if ((Integer) playerWatchCycle.get(player) == 2) {
      player.a("Sun-scrolling backwards.");
    }

  }

  public static int getToolMode(EntityHuman player) {
    if (playerToolMode.get(player) == null) {
      playerToolMode.put(player, 0);
      return 0;
    }
    else {
      return (Integer) playerToolMode.get(player);
    }
  }

  public static void updateToolMode(EntityHuman player) {
    if (playerToolMode.get(player) == null) {
      playerToolMode.put(player, 0);
    }
    else if ((Integer) playerToolMode.get(player) == 3) {
      playerToolMode.put(player, 0);
    }
    else {
      playerToolMode.put(player, (Integer) playerToolMode.get(player) + 1);
    }

    if ((Integer) playerToolMode.get(player) == 0) {
      player.a("Tool set to normal.");
    }

    if ((Integer) playerToolMode.get(player) == 1) {
      player.a("Tool set to tall-shot.");
    }

    if ((Integer) playerToolMode.get(player) == 2) {
      player.a("Tool set to wide-shot.");
    }

    if ((Integer) playerToolMode.get(player) == 3) {
      player.a("Tool set to long-shot.");
    }

  }

  public static boolean isPlayerInLava(EntityHuman player) {
    if (playerInLava.get(player) == null) {
      playerInLava.put(player, false);
      return false;
    }
    else {
      return (Boolean) playerInLava.get(player);
    }
  }

  public static void updatePlayerInLava(EntityHuman player, boolean inLava) {
    playerInLava.put(player, inLava);
  }

  public static boolean isPlayerInWater(EntityHuman player) {
    if (playerInWater.get(player) == null) {
      playerInWater.put(player, false);
      return false;
    }
    else {
      return (Boolean) playerInWater.get(player);
    }
  }

  public static void updatePlayerInWater(EntityHuman player, boolean inWater) {
    playerInWater.put(player, inWater);
  }

  private static void setupCraftHook() {
    ICraftingHandler iCraftingHandler = new ICraftingHandler() {
      public void onTakenFromCrafting(EntityHuman player, ItemStack itemStack, IInventory iInventory) {
        int kleinStarPoints = 0;
        int itemSlot;
        if (itemStack != null && EEMergeLib.mergeOnCraft.contains(itemStack.id)) {
          for (itemSlot = 0; itemSlot < iInventory.getSize(); ++itemSlot) {
            ItemStack item = iInventory.getItem(itemSlot);
            if (item != null && item.getItem() instanceof ItemKleinStar && ((ItemKleinStar) item.getItem()).getKleinPoints(item) > 0) {
              kleinStarPoints += ((ItemKleinStar) item.getItem()).getKleinPoints(item);
            }
          }
          ((ItemKleinStar) itemStack.getItem()).setKleinPoints(itemStack, kleinStarPoints);
        }
        else if (itemStack != null && EEMergeLib.destroyOnCraft.contains(itemStack.id) && itemStack.id == EEItem.arcaneRing.id) {
          for (itemSlot = 0; itemSlot < iInventory.getSize(); ++itemSlot) {
            iInventory.setItem(itemSlot, (ItemStack) null);
          }
        }
      }
    };
    MinecraftForge.registerCraftingHandler(iCraftingHandler);
  }

  public static EEBase getInstance() {
    return eeBaseInstance;
  }

  public static float getPedestalFactor(World world) {
    float f = 1.0F;
    validatePedestalCoords(world);
    for (int i = 0; i < pedestalCoords.size(); ++i) {
      if (pedestalCoords.get(i) != null) {
        f = (float) ((double) f * 0.9D);
      }
    }

    return f < 0.1F ? 0.1F : f;
  }

  public static void addPedestalCoords(TilePedestal tilePedestal) { //2019-04-12 - Unsure of var1 and var2 meanings.
    int var1 = 0;
    Integer[] var2;
    for (var2 = new Integer[]{tilePedestal.x, tilePedestal.y, tilePedestal.z}; pedestalCoords.get(var1) != null; ++var1) {
    }
    pedestalCoords.put(var1, var2);
    validatePedestalCoords(tilePedestal.world);
  }

  public static void validatePedestalCoords(World world) {
    for (int i = 0; i < pedestalCoords.size(); ++i) {
      if (pedestalCoords.get(i) != null) {
        Integer[] coords = (Integer[]) pedestalCoords.get(i);
        if (EEProxy.getTileEntity(world, coords[0], coords[1], coords[2], TilePedestal.class) == null) {
          removePedestalCoord(i);
        }
        else if (!((TilePedestal) EEProxy.getTileEntity(world, coords[0], coords[1], coords[2], TilePedestal.class)).isActivated()) {
          removePedestalCoord(i);
        }
        else {
          for (int a = 0; a < pedestalCoords.size(); ++a) {
            if (i != a && pedestalCoords.get(a) != null) {
              Integer[] coords2 = (Integer[]) pedestalCoords.get(a);
              if (coordsEqual(coords, coords2)) {
                removePedestalCoord(a);
              }
            }
          }
        }
      }
    }

  }

  private static boolean coordsEqual(Integer[] coords, Integer[] coords2) {
    return coords[0].equals(coords2[0]) && coords[1].equals(coords2[1]) && coords[2].equals(coords2[2]);
  }

  private static void removePedestalCoord(int coord) {
    pedestalCoords.remove(coord);
  }

  public static float getPlayerWatchFactor() {
    float watchFactor = 1.0F;
    for (int watch = 0; watch < playerWoftFactor; ++watch) {
      watchFactor = (float) ((double) watchFactor * 0.9D);
    }
    return watchFactor;
  }

  public static void ConsumeReagentForDuration(ItemStack itemStack, EntityHuman player, boolean noFuel) { //2019-04-12 - noFuel may be improperly named.
    if (!EEProxy.isClient(player.world)) {
      if (consumeKleinStarPoint(player, 32)) {
        updatePlayerEffect(itemStack.getItem(), 64, player);
      }
      else if (Consume(new ItemStack(EEItem.aeternalisFuel), player, noFuel)) {
        updatePlayerEffect(itemStack.getItem(), 16384, player);
      }
      else if (Consume(new ItemStack(EEItem.mobiusFuel), player, noFuel)) {
        updatePlayerEffect(itemStack.getItem(), 4096, player);
      }
      else if (Consume(new ItemStack(Block.GLOWSTONE), player, false)) {
        updatePlayerEffect(itemStack.getItem(), 3072, player);
      }
      else if (Consume(new ItemStack(EEItem.alchemicalCoal), player, false)) {
        updatePlayerEffect(itemStack.getItem(), 1024, player);
      }
      else if (Consume(new ItemStack(Item.GLOWSTONE_DUST), player, false)) {
        updatePlayerEffect(itemStack.getItem(), 768, player);
      }
      else if (Consume(new ItemStack(Item.SULPHUR), player, false)) {
        updatePlayerEffect(itemStack.getItem(), 384, player);
      }
      else if (Consume(new ItemStack(Item.COAL, 1, 0), player, false)) {
        updatePlayerEffect(itemStack.getItem(), 256, player);
      }
      else if (Consume(new ItemStack(Item.REDSTONE), player, false)) {
        updatePlayerEffect(itemStack.getItem(), 128, player);
      }
      else if (Consume(new ItemStack(Item.COAL, 1, 1), player, false)) {
        updatePlayerEffect(itemStack.getItem(), 64, player);
      }
    }
  }

  public static void ConsumeReagent(ItemStack itemStack, EntityHuman player, boolean noFuel) { //2019-04-12 - noFuel may be improperly named.
    if (consumeKleinStarPoint(player, 32)) {
      ((ItemEECharged) itemStack.getItem()).setShort(itemStack, "fuelRemaining", ((ItemEECharged) itemStack.getItem()).getShort(itemStack, "fuelRemaining") + 4);
    }
    else if (Consume(new ItemStack(EEItem.aeternalisFuel, 1), player, false)) {
      ((ItemEECharged) itemStack.getItem()).setShort(itemStack, "fuelRemaining", ((ItemEECharged) itemStack.getItem()).getShort(itemStack, "fuelRemaining") + 1024);
    }
    else if (Consume(new ItemStack(EEItem.mobiusFuel, 1), player, false)) {
      ((ItemEECharged) itemStack.getItem()).setShort(itemStack, "fuelRemaining", ((ItemEECharged) itemStack.getItem()).getShort(itemStack, "fuelRemaining") + 256);
    }
    else if (Consume(new ItemStack(Block.GLOWSTONE, 1), player, false)) {
      ((ItemEECharged) itemStack.getItem()).setShort(itemStack, "fuelRemaining", ((ItemEECharged) itemStack.getItem()).getShort(itemStack, "fuelRemaining") + 192);
    }
    else if (Consume(new ItemStack(EEItem.alchemicalCoal, 1), player, false)) {
      ((ItemEECharged) itemStack.getItem()).setShort(itemStack, "fuelRemaining", ((ItemEECharged) itemStack.getItem()).getShort(itemStack, "fuelRemaining") + 64);
    }
    else if (Consume(new ItemStack(Item.GLOWSTONE_DUST, 1), player, false)) {
      ((ItemEECharged) itemStack.getItem()).setShort(itemStack, "fuelRemaining", ((ItemEECharged) itemStack.getItem()).getShort(itemStack, "fuelRemaining") + 48);
    }
    else if (Consume(new ItemStack(Item.SULPHUR, 1), player, false)) {
      ((ItemEECharged) itemStack.getItem()).setShort(itemStack, "fuelRemaining", ((ItemEECharged) itemStack.getItem()).getShort(itemStack, "fuelRemaining") + 24);
    }
    else if (Consume(new ItemStack(Item.COAL, 1, 0), player, noFuel)) {
      ((ItemEECharged) itemStack.getItem()).setShort(itemStack, "fuelRemaining", ((ItemEECharged) itemStack.getItem()).getShort(itemStack, "fuelRemaining") + 16);
    }
    else if (Consume(new ItemStack(Item.REDSTONE, 1), player, noFuel)) {
      ((ItemEECharged) itemStack.getItem()).setShort(itemStack, "fuelRemaining", ((ItemEECharged) itemStack.getItem()).getShort(itemStack, "fuelRemaining") + 8);
    }
    else if (Consume(new ItemStack(Item.COAL, 1, 1), player, noFuel)) {
      ((ItemEECharged) itemStack.getItem()).setShort(itemStack, "fuelRemaining", ((ItemEECharged) itemStack.getItem()).getShort(itemStack, "fuelRemaining") + 4);
    }
  }

  public static boolean isLeftClickDown(EntityHuman player, MinecraftServer minecraftServer) {
    if (playerLeftClick.get(player) == null) {
      resetLeftClick(player);
    }
    return (Boolean) playerLeftClick.get(player);
  }

  public static void resetLeftClick(EntityHuman player) {
    playerLeftClick.put(player, false);
  }

  public static void watchTransGrid(EntityHuman player) {
    playerTransGridOpen.put(player, true);
  }

  public static void closeTransGrid(EntityHuman player) {
    playerTransGridOpen.put(player, false);
  }

  public static Boolean getTransGridOpen(EntityHuman player) {
    if (playerTransGridOpen.get(player) == null) {
      playerTransGridOpen.put(player, false);
    }
    return (Boolean) playerTransGridOpen.get(player);
  }

  public static int getMachineFactor() {
    return machineFactor < 1 ? 1 : (machineFactor > 16 ? 16 : machineFactor);
  }

  public static boolean isNeutralEntity(Entity entity) {
    return entity instanceof EntitySheep || entity instanceof EntityCow || entity instanceof EntityPig || entity instanceof EntityChicken || entity instanceof EntityMushroomCow || entity instanceof EntityVillager || entity instanceof EntityOcelot || entity instanceof EntityWolf || entity instanceof EntitySnowman || entity instanceof EntityIronGolem;
  }

  public static boolean isHostileEntity(Entity entity) {
    return entity instanceof EntityCreeper || entity instanceof EntityZombie || entity instanceof EntitySkeleton || entity instanceof EntitySpider || entity instanceof EntityCaveSpider || entity instanceof EntityEnderman || entity instanceof EntitySilverfish || entity instanceof EntitySlime || entity instanceof EntityGhast || entity instanceof EntityMagmaCube || entity instanceof EntityPigZombie || entity instanceof EntityBlaze;
  }
}
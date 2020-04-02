package net.minecraft.server;

import ee.*;
import ee.core.PickupHandler;
import ee.network.PacketHandler;
import forge.DimensionManager;
import forge.MinecraftForge;
import forge.NetworkMod;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class mod_EE extends NetworkMod {
  public static final String MOD_NAME = "Equivalent Exchange 2";
  public static final String CHANNEL_NAME = "EE2";
  public static final String SOUND_RESOURCE_LOCATION = "/ee/sound/";
  public static final String SOUND_PREFIX = "ee.sound.";
  private static int tickCounter = 0;
  private static int ticksPerTabletUpdate = 5;
  private static mod_EE instance;
  private int blackListTimer;
  private int rmArmorClearSlot;
  public static int pedestalModelID;
  public static int chestModelID;
  public static HashMap playerShockwave = new HashMap();

  public void load() {
  }

  public mod_EE() {
    instance = this;
    MinecraftForge.versionDetect("Equivalent Exchange 2", 3, 3, 7);
    ModLoader.setInGameHook(this, true, true);
    MinecraftForge.registerConnectionHandler(new PacketHandler());
    MinecraftForge.setGuiHandler(this, new EEGuiHandler());
    EEBase.init(this);
    MinecraftForge.registerEntity(EntityPhilosopherEssence.class, this, 143, 300, 2, true);
    MinecraftForge.registerEntity(EntityWaterEssence.class, this, 144, 300, 2, true);
    MinecraftForge.registerEntity(EntityLavaEssence.class, this, 145, 300, 2, true);
    MinecraftForge.registerEntity(EntityWindEssence.class, this, 146, 300, 2, true);
    MinecraftForge.registerEntity(EntityPyrokinesis.class, this, 147, 300, 2, true);
    MinecraftForge.registerEntity(EntityGrimArrow.class, this, 148, 300, 2, true);
    MinecraftForge.registerEntity(EntityLootBall.class, this, 149, 300, 2, true);
    MinecraftForge.registerEntity(EntityNovaPrimed.class, this, 150, 300, 2, true);
    MinecraftForge.registerEntity(EntityHyperkinesis.class, this, 151, 300, 2, true);
    EEItem.init();
    EEBlock.init();
    EEMaps.InitAlchemicalValues();
    EEMaps.InitFlyingItems();
    EEMaps.InitFuelItems();
    EEMaps.InitFireImmunities();
    EEMaps.InitDurationEffectItems();
    EEMaps.InitEERecipes();
    EEMaps.InitRepairRecipes();
    EEMaps.InitChestItems();
    EEMaps.InitChargeditems();
    EEMaps.InitWoodAndLeafBlocks();
    EEMaps.InitPedestalItems();
    EEMaps.InitModItems();
    EEMaps.InitOreBlocks();
    EEMaps.InitBlacklist();
    EEMaps.InitMetaData();
    pedestalModelID = ModLoader.getUniqueBlockModelID(this, true);
    chestModelID = ModLoader.getUniqueBlockModelID(this, true);
    this.blackListTimer = 100;
    MinecraftForge.registerPickupHandler(new PickupHandler());
  }

  public String getVersion() {
    return String.format("%d.%d.%d.%d", 1, 4, 6, 5);
  }

  public boolean onTickInGame(MinecraftServer var1) {
    EEProxy.Init(var1, this);
    World[] worlds = DimensionManager.getWorlds();
    int worldcount = worlds.length;

    for (int i = 0; i < worldcount; ++i) {
      World world = worlds[i];
      this.onTickInGame(var1, world.players, world);
    }

    return true;
  }

  public boolean onTickInGame(MinecraftServer minecraftServer, List players, World world) {
    if (!EEBase.externalModsInitialized) {
      for (int i = 0; i < ModLoader.getLoadedMods().size(); ++i) {
        if (((BaseMod) ModLoader.getLoadedMods().get(i)).toString().contains("mod_IC2")) {
          EEAddonIC2.initialize();
        }
        else if (((BaseMod) ModLoader.getLoadedMods().get(i)).toString().contains("mod_RedPowerCore")) {
          EEAddonRP2.initBase();
        }
        else if (((BaseMod) ModLoader.getLoadedMods().get(i)).toString().contains("mod_RedPowerWorld")) {
          EEAddonRP2.initBase();
          EEAddonRP2.initWorld();
        }
        else if (((BaseMod) ModLoader.getLoadedMods().get(i)).toString().contains("mod_BuildCraftEnergy")) {
          EEAddonBC.initialize();
        }
        else if (((BaseMod) ModLoader.getLoadedMods().get(i)).toString().contains("mod_Forestry")) {
          EEAddonForestry.initialize();
        }
      }

      EEBase.externalModsInitialized = true;
    }

    EEBase.validatePedestalCoords(world);
    if (tickCounter % ticksPerTabletUpdate == 0) {
      this.doTransGridUpdates(players);
      tickCounter = 0;
    }

    this.doWatchCheck(players, world);
    this.doFlightCheck(players, world);
    Iterator playerIterator = players.iterator();

    while (playerIterator.hasNext()) {
      EntityHuman player = (EntityHuman) playerIterator.next();
      if (this.blackListTimer <= 0) {
        this.blackListTimer = 100;
        if (EEMaps.isBlacklisted(player.name)) {
          player.world.strikeLightning(new EntityWeatherLighting(player.world, player.locX, player.locY, player.locZ));
        }
      }

      this.doGemPowers(player);
      this.doEquipCheck(player, world);
      this.doFireImmuneCheck(player);
    }

    if (this.blackListTimer > 0) {
      --this.blackListTimer;
    }

    ++tickCounter;
    return true;
  }

  private void doTransGridUpdates(List players) {
    Iterator playerIterator = players.iterator();

    while (playerIterator.hasNext()) {
      EntityHuman player = (EntityHuman) playerIterator.next();
      if (EEBase.getTransGridOpen(player)) {
        EEProxy.getTransData(player).onUpdate(player.world, player);
      }
    }

  }

  private void doGemPowers(EntityHuman player) {
    EEBase.updatePlayerToggleCooldown(player);

    for (int i = 0; i <= 3; ++i) {
      if (player.inventory.armor[i] != null) {
        ItemStack var3 = player.inventory.armor[i];
        if (i == 2 && var3.getItem() instanceof ItemRedArmorPlus && player.hasEffect(MobEffectList.POISON)) {
          player.d(player.getEffect(MobEffectList.POISON));
        }

        if (i == 1 && var3.getItem() instanceof ItemRedArmorPlus) {
          List nearbyEntities = player.world.a(EntityLiving.class, AxisAlignedBB.b(player.locX - 5.0D, player.locY - 5.0D, player.locZ - 5.0D, player.locX + 5.0D, player.locY + 5.0D, player.locZ + 5.0D));

          for (int a = 0; a < nearbyEntities.size(); ++a) {
            Entity Entity = (Entity) nearbyEntities.get(a);
            if (!(Entity instanceof EntityHuman) && (Entity.motX > 0.0D || Entity.motZ > 0.0D)) {
              Entity.motX *= 0.10000000149011612D;
              Entity.motZ *= 0.10000000149011612D;
            }
          }
        }

        if (i == 0 && var3.getItem() instanceof ItemRedArmorPlus) {
          if (!player.isSprinting() && EEBase.getPlayerArmorMovement(player)) {
            player.setSprinting(true);
          }

          if (EEBase.getPlayerArmorMovement(player)) {
            player.am = (float) ((double) player.am + 0.04000000000000001D);
            if (player.am > 0.3F) {
              player.am = 0.3F;
            }

            if (player.motY > 1.0D) {
              player.motY = 1.0D;
            }

            if (player.motY < 0.0D && !player.isSneaking()) {
              player.motY *= 0.88D;
            }
          }

          if (player.fallDistance > 0.0F) {
            player.fallDistance = 0.0F;
          }
        }

        if (i == 3 && var3.getItem() instanceof ItemRedArmorPlus && player.getAirTicks() < 20) {
          player.setAirTicks(20);
        }
      }
    }

  }

  private void doShockwave(EntityHuman player) {
    List nearbyEntities = player.world.a(EntityLiving.class, AxisAlignedBB.b(player.locX - 7.0D, player.locY - 7.0D, player.locZ - 7.0D, player.locX + 7.0D, player.locY + 7.0D, player.locZ + 7.0D));

    for (int i = 0; i < nearbyEntities.size(); ++i) {
      Entity entity = (Entity) nearbyEntities.get(i);
      if (!(entity instanceof EntityHuman)) {
        entity.motX += 0.2D / (entity.locX - player.locX);
        entity.motY += 0.05999999865889549D;
        entity.motZ += 0.2D / (entity.locZ - player.locZ);
      }
    }

    List nearbyArrows = player.world.a(EntityArrow.class, AxisAlignedBB.b((double) ((float) player.locX - 5.0F), player.locY - 5.0D, (double) ((float) player.locZ - 5.0F), (double) ((float) player.locX + 5.0F), player.locY + 5.0D, (double) ((float) player.locZ + 5.0F)));

    for (int i = 0; i < nearbyArrows.size(); ++i) {
      Entity arrow = (Entity) nearbyArrows.get(i);
      arrow.motX += 0.2D / (arrow.locX - player.locX);
      arrow.motY += 0.05999999865889549D;
      arrow.motZ += 0.2D / (arrow.locZ - player.locZ);
    }

    List nearbyFireballs = player.world.a(EntityFireball.class, AxisAlignedBB.b((double) ((float) player.locX - 5.0F), player.locY - 5.0D, (double) ((float) player.locZ - 5.0F), (double) ((float) player.locX + 5.0F), player.locY + 5.0D, (double) ((float) player.locZ + 5.0F)));

    for (int i = 0; i < nearbyFireballs.size(); ++i) {
      Entity fireball = (Entity) nearbyFireballs.get(i);
      fireball.motX += 0.2D / (fireball.locX - player.locX);
      fireball.motY += 0.05999999865889549D;
      fireball.motZ += 0.2D / (fireball.locZ - player.locZ);
    }

  }

  private void doWatchCheck(List players, World world) {
    int watchCount = 0;
    Iterator playerIterator = players.iterator();

    while (playerIterator.hasNext()) {
      EntityHuman player = (EntityHuman) playerIterator.next();
      ItemStack[] hotbarItems = EEBase.quickBar(player);
      int itemCount = hotbarItems.length;

      for (int i = 0; i < itemCount; ++i) {
        ItemStack item = hotbarItems[i];
        if (item != null && item.getItem() instanceof ItemEECharged && item.getItem() instanceof ItemWatchOfTime && (item.getData() & 1) == 1 && EEBase.getPlayerEffect(item.getItem(), player) > 0) {
          watchCount += ((ItemEECharged) item.getItem()).chargeLevel(item) + 1;
          EEBase.playerWatchMagnitude.put(player, ((ItemEECharged) item.getItem()).chargeLevel(item) + 1);
        }
      }
    }

    EEBase.playerWoftFactor = watchCount;
  }

  private void doFlightCheck(List players, World world) {
    Iterator playerIterator = players.iterator();

    while (true) {
      while (playerIterator.hasNext()) {
        EntityHuman player = (EntityHuman) playerIterator.next();
        if (((EntityPlayer) player).itemInWorldManager.isCreative()) {
          return;
        }

        boolean arcaneRing = false;
        boolean volcaniteAmulet = false;
        boolean evertideAmulet = false;
        boolean swiftwolfRing = false;
        ItemStack[] hotbarItems = EEBase.quickBar(player);
        int hotbarItemCount = hotbarItems.length;

        for (int i = 0; i < hotbarItemCount; ++i) {
          ItemStack item = hotbarItems[i];
          if (item != null && EEMaps.isFlyingItem(item.id)) {
            if (item.getItem() == EEItem.arcaneRing) {
              arcaneRing = true;
              player.abilities.canFly = true;
            }
            else if (item.getItem() == EEItem.evertide && EEBase.isPlayerInWater(player)) {
              evertideAmulet = true;
              player.abilities.isFlying = true;
            }
            else if (item.getItem() == EEItem.volcanite && EEBase.isPlayerInLava(player)) {
              volcaniteAmulet = true;
              player.abilities.isFlying = true;
            }
            else if (item.getItem() == EEItem.swiftWolfRing) {
              swiftwolfRing = true;
              player.abilities.canFly = true;
            }
          }
        }

        if (swiftwolfRing && (evertideAmulet || volcaniteAmulet || arcaneRing)) {
          swiftwolfRing = false;
          this.disableSWRG(player);
        }
        else if (swiftwolfRing && !volcaniteAmulet && !evertideAmulet && !arcaneRing) {
          if (player.abilities.isFlying) {
            this.forceEnableSWRG(player);
          }

          if (!player.abilities.isFlying) {
            this.disableSWRG(player);
          }
        }

        if (!swiftwolfRing && !volcaniteAmulet && !evertideAmulet && !arcaneRing) {
          player.abilities.canFly = false;
          player.abilities.isFlying = false;
          player.updateAbilities();
        }
        else {
          player.abilities.canFly = true;
        }
      }

      return;
    }
  }

  private void forceEnableSWRG(EntityHuman player) {
    ItemStack[] hotbarItems = EEBase.quickBar(player);
    int hotbarItemCount = hotbarItems.length;

    for (int i = 0; i < hotbarItemCount; ++i) {
      ItemStack item = hotbarItems[i];
      if (item != null && item.getItem() == EEItem.swiftWolfRing) {
        if (!((ItemEECharged) item.getItem()).isActivated(item)) {
          ((ItemEECharged) item.getItem()).doToggle(item, player.world, player);
          return;
        }

        return;
      }
    }

  }

  private void disableSWRG(EntityHuman player) {
    ItemStack[] hotbarItems = EEBase.quickBar(player);
    int hotbarItemCount = hotbarItems.length;

    for (int i = 0; i < hotbarItemCount; ++i) {
      ItemStack item = hotbarItems[i];
      if (item != null && item.getItem() == EEItem.swiftWolfRing) {
        if (player.abilities.isFlying) {
          if (((ItemEECharged) item.getItem()).isActivated(item)) {
            if (EEBase.getPlayerEffect(item.getItem(), player) <= 0) {
              ((ItemEECharged) item.getItem()).ConsumeReagent(item, player, false);
            }

            if (EEBase.getPlayerEffect(item.getItem(), player) <= 0) {
              ((ItemEECharged) item.getItem()).doToggle(item, player.world, player);
            }
          }
        }
        else if (((ItemEECharged) item.getItem()).isActivated(item)) {
          ((ItemEECharged) item.getItem()).doToggle(item, player.world, player);
        }
      }
    }
  }

  private void doEquipCheck(EntityHuman player, World world) {
    ItemStack[] hotbarItems = EEBase.quickBar(player);
    int hotbarItemCount = hotbarItems.length;

    for (int i = 0; i < hotbarItemCount; ++i) {
      ItemStack item = hotbarItems[i];
      if (item != null && item.getItem() instanceof ItemEECharged) {
        if (item == player.U()) {
          ((ItemEECharged) item.getItem()).doHeld(item, world, player);
        }

        ((ItemEECharged) item.getItem()).doPassive(item, world, player);
        if ((item.getData() & 1) == 1 && EEMaps.hasDurationEffect(item.getItem())) {
          if (item.getItem() instanceof ItemWatchOfTime) {
            if (EEBase.getPlayerEffect(item.getItem(), player) > 0) {
              EEBase.updatePlayerEffect(item.getItem(), EEBase.getPlayerEffect(item.getItem(), player) - (((ItemEECharged) item.getItem()).chargeLevel(item) + 1) * (((ItemEECharged) item.getItem()).chargeLevel(item) + 1), player);
            }
          }
          else if (EEBase.getPlayerEffect(item.getItem(), player) > 0) {
            EEBase.updatePlayerEffect(item.getItem(), EEBase.getPlayerEffect(item.getItem(), player) - 1, player);
          }

          if (EEBase.getPlayerEffect(item.getItem(), player) <= 0) {
            ((ItemEECharged) item.getItem()).ConsumeReagent(item, player, false);
          }

          if (EEBase.getPlayerEffect(item.getItem(), player) <= 0) {
            ((ItemEECharged) item.getItem()).doToggle(item, world, player);
          }
          else {
            ((ItemEECharged) item.getItem()).doActive(item, world, player);
          }
        }

        if ((item.getData() & 2) == 2 && item.getItem() instanceof ItemSwiftWolfRing) {
          if (EEBase.getPlayerEffect(item.getItem(), player) > 0) {
            EEBase.updatePlayerEffect(item.getItem(), EEBase.getPlayerEffect(item.getItem(), player) - 2, player);
          }

          if (EEBase.getPlayerEffect(item.getItem(), player) <= 0) {
            ((ItemEECharged) item.getItem()).ConsumeReagent(item, player, false);
          }

          if (EEBase.getPlayerEffect(item.getItem(), player) <= 0) {
            ((ItemEECharged) item.getItem()).doToggle(item, world, player);
          }
          else {
            ((ItemEECharged) item.getItem()).doActive(item, world, player);
          }
        }
      }
    }

  }

  private void doFireImmuneCheck(EntityHuman player) {
    boolean fireImmune = false;


    for (int i = 0; i < 9; ++i) {
      if (player.inventory.items[i] != null && EEMaps.isFireImmuneItem(player.inventory.items[i].id)) {
        fireImmune = true;
      }
    }

    for (int i = 0; i < 4; ++i) {
      if (player.inventory.armor[i] != null && EEMaps.isFireImmuneArmor(player.inventory.armor[i].id)) {
        fireImmune = true;
      }
    }

    player.fireProof = fireImmune;
  }

  public boolean clientSideRequired() {
    return true;
  }

  public boolean serverSideRequired() {
    return false;
  }

  public static BaseMod getInstance() {
    return instance;
  }
}

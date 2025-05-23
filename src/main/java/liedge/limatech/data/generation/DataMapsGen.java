package liedge.limatech.data.generation;

import liedge.limatech.LimaTechTags;
import liedge.limatech.lib.CompoundValueOperation;
import liedge.limatech.lib.weapons.GlobalWeaponDamageModifiers;
import liedge.limatech.registry.game.LimaTechGameEvents;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.DataMapProvider;
import net.neoforged.neoforge.registries.datamaps.builtin.NeoForgeDataMaps;
import net.neoforged.neoforge.registries.datamaps.builtin.VibrationFrequency;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Stream;

import static liedge.limatech.lib.weapons.GlobalWeaponDamageModifiers.WeaponDamageModifier.modifier;
import static liedge.limatech.registry.game.LimaTechItems.*;

class DataMapsGen extends DataMapProvider
{
    DataMapsGen(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> lookupProvider)
    {
        super(packOutput, lookupProvider);
    }

    private List<GlobalWeaponDamageModifiers.WeaponDamageModifier> makeModifierList(GlobalWeaponDamageModifiers.WeaponDamageModifier.Builder... builders)
    {
        return Stream.of(builders).map(GlobalWeaponDamageModifiers.WeaponDamageModifier.Builder::build).toList();
    }

    @Override
    protected void gather(HolderLookup.Provider registries)
    {
        // Vibration frequencies
        builder(NeoForgeDataMaps.VIBRATION_FREQUENCIES)
                .add(LimaTechGameEvents.WEAPON_FIRED, new VibrationFrequency(3), false)
                .add(LimaTechGameEvents.PROJECTILE_EXPLODED, new VibrationFrequency(15), false);

        // Weapon damage modifiers
        builder(GlobalWeaponDamageModifiers.DATA_MAP_TYPE)
                .add(LimaTechTags.EntityTypes.HIGH_THREAT_TARGETS, makeModifierList(
                        modifier(CompoundValueOperation.ADD_MULTIPLIED_TOTAL).forWeapon(SUBMACHINE_GUN).withConstantAmount(-0.8f),
                        modifier(CompoundValueOperation.ADD_MULTIPLIED_TOTAL).forWeapon(SHOTGUN).withConstantAmount(-0.45f),
                        modifier(CompoundValueOperation.MULTIPLY).forWeapon(ROCKET_LAUNCHER).withConstantAmount(2f)
                ), false)
                .add(LimaTechTags.EntityTypes.MEDIUM_THREAT_TARGETS, makeModifierList(
                        modifier(CompoundValueOperation.ADD_MULTIPLIED_TOTAL).forWeapon(SUBMACHINE_GUN).withConstantAmount(-0.5f),
                        modifier(CompoundValueOperation.ADD_MULTIPLIED_TOTAL).forWeapon(SHOTGUN).withConstantAmount(-0.4f)
                ), false);
    }
}
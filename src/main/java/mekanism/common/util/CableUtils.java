package mekanism.common.util;

import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import java.util.EnumSet;
import java.util.Set;
import mekanism.api.Action;
import mekanism.api.energy.IEnergyContainer;
import mekanism.api.energy.IMekanismStrictEnergyHandler;
import mekanism.api.energy.IStrictEnergyHandler;
import mekanism.api.inventory.AutomationType;
import mekanism.api.transmitters.TransmissionType;
import mekanism.common.base.target.EnergyAcceptorTarget;
import mekanism.common.capabilities.Capabilities;
import mekanism.common.integration.EnergyCompatUtils;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public final class CableUtils {

    public static boolean isValidAcceptorOnSide(TileEntity tile, Direction side) {
        if (CapabilityUtils.getCapability(tile, Capabilities.GRID_TRANSMITTER_CAPABILITY, null).filter(transmitter ->
              TransmissionType.checkTransmissionType(transmitter, TransmissionType.ENERGY)).isPresent()) {
            return false;
        }
        return EnergyCompatUtils.hasStrictEnergyHandler(tile, side.getOpposite());
    }

    public static IStrictEnergyHandler[] getConnectedAcceptors(BlockPos pos, World world, Set<Direction> sides) {
        IStrictEnergyHandler[] acceptors = new IStrictEnergyHandler[EnumUtils.DIRECTIONS.length];
        EmitUtils.forEachSide(world, pos, sides, (tile, side) -> acceptors[side.ordinal()] = EnergyCompatUtils.getStrictEnergyHandler(tile, side.getOpposite()));
        return acceptors;
    }

    @Deprecated//TODO: Remove and replace with the other ones
    public static void emit(IMekanismStrictEnergyHandler emitter) {
        TileEntity tileEntity = (TileEntity) emitter;
        if (!tileEntity.getWorld().isRemote() && MekanismUtils.canFunction(tileEntity)) {
            for (int container = 0; container < emitter.getEnergyContainerCount(); container++) {
                emit(emitter.getEnergyContainer(container, null), tileEntity);
            }
        }
    }

    public static void emit(IEnergyContainer energyContainer, TileEntity from) {
        emit(EnumSet.allOf(Direction.class), energyContainer, from);
    }

    public static void emit(Set<Direction> outputSides, IEnergyContainer energyContainer, TileEntity from) {
        emit(outputSides, energyContainer, from, energyContainer.getMaxEnergy());
    }

    public static void emit(Set<Direction> outputSides, IEnergyContainer energyContainer, TileEntity from, double maxOutput) {
        if (!energyContainer.isEmpty() && maxOutput > 0) {
            energyContainer.extract(emit(outputSides, energyContainer.extract(maxOutput, Action.SIMULATE, AutomationType.INTERNAL), from), Action.EXECUTE, AutomationType.INTERNAL);
        }
    }

    /**
     * Emits energy from a central block by splitting the received stack among the sides given.
     *
     * @param sides - the list of sides to output from
     * @param energyToSend - the energy to output
     * @param from  - the TileEntity to output from
     *
     * @return the amount of gas emitted
     */
    public static double emit(Set<Direction> sides, double energyToSend, TileEntity from) {
        if (energyToSend <= 0 || sides.isEmpty()) {
            return 0;
        }
        //Fake that we have one target given we know that no sides will overlap This allows us to have slightly better performance
        EnergyAcceptorTarget target = new EnergyAcceptorTarget();
        EmitUtils.forEachSide(from.getWorld(), from.getPos(), sides, (acceptor, side) -> {
            //Insert to access side
            final Direction accessSide = side.getOpposite();
            //Collect cap
            IStrictEnergyHandler strictEnergyHandler = EnergyCompatUtils.getStrictEnergyHandler(acceptor, accessSide);
            if (strictEnergyHandler != null) {
                target.addHandler(accessSide, strictEnergyHandler);
            }
        });

        int curHandlers = target.getHandlers().size();
        if (curHandlers > 0) {
            Set<EnergyAcceptorTarget> targets = new ObjectOpenHashSet<>();
            targets.add(target);
            return EmitUtils.sendToAcceptors(targets, curHandlers, energyToSend);
        }
        return 0;
    }
}
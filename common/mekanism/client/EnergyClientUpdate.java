package mekanism.client;

import java.util.List;

import mekanism.api.transmitters.ITransmitter;
import mekanism.api.transmitters.TransmissionType;
import mekanism.api.transmitters.DynamicNetwork.NetworkFinder;
import mekanism.api.Object3D;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class EnergyClientUpdate
{	
	public NetworkFinder finder;
	
	public World worldObj;
	
	public double energyScale;
	
	public EnergyClientUpdate(TileEntity head, double power)
	{
		worldObj = head.worldObj;
		energyScale = power;
		finder = new NetworkFinder(head.worldObj, TransmissionType.ENERGY, Object3D.get(head));
	}
	
	public void clientUpdate()
	{
		List<Object3D> found = finder.exploreNetwork();
		System.out.println(energyScale);
		for(Object3D object : found)
		{
			TileEntity tileEntity = object.getTileEntity(worldObj);
			
			if(tileEntity instanceof ITransmitter && ((ITransmitter<?, ?>)tileEntity).getTransmissionType() == TransmissionType.ENERGY)
			{
				((ITransmitter<?, Double>)tileEntity).clientUpdate(energyScale);
			}
		}
	}
}

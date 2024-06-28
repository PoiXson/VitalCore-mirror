package com.poixson.tools;

import static com.poixson.utils.MathUtils.Distance3D;
import static com.poixson.utils.NumberUtils.CastDouble;
import static com.poixson.utils.Utils.IsEmpty;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.util.NumberConversions;
import org.bukkit.util.Vector;

import com.poixson.tools.abstractions.AtomicDouble;
import com.poixson.tools.dao.Dabc;
import com.poixson.tools.dao.Iabc;
import com.poixson.utils.BukkitUtils;


public class LocationSafe extends Location {

	public final AtomicReference<String> w = new AtomicReference<String>(null);
	public final AtomicReference<Dabc> value = new AtomicReference<Dabc>(null);

	public final AtomicDouble pitch = new AtomicDouble(0.0);
	public final AtomicDouble yaw   = new AtomicDouble(0.0);



	public LocationSafe(final Location location) {
		this(
			location.getWorld(),
			location.getX(),
			location.getY(),
			location.getZ()
		);
	}
	public LocationSafe(final World world, final double x, final double y, final double z) {
		this();
		this.w.set(world.getName());
		this.value.set(new Dabc(x, y, z));
	}
	public LocationSafe(final String world_name, final Iabc loc) {
		this();
		this.w.set(world_name);
		this.value.set(new Dabc(loc.a, loc.b, loc.c));
	}
	public LocationSafe() {
		super(null, 0.0, 0.0, 0.0);
	}



	public static LocationSafe ToSafe(final Location loc) {
		if (loc instanceof LocationSafe)
			return (LocationSafe) loc;
		return new LocationSafe(loc);
	}

	@Override
	public Location clone() {
		return new LocationSafe(this);
	}



	@Override
	public World getWorld() {
		final String world_name = this.w.get();
		if (IsEmpty(world_name)) return null;
		return Bukkit.getWorld(world_name);
	}
	public String getWorldName() {
		return this.w.get();
	}

	@Override
	public void setWorld(final World world) {
		if (world == null) this.w.set(null);
		else               this.w.set(world.getName());
	}

	@Override
	public boolean isWorldLoaded() {
		final String world_name = this.getWorldName();
		if (IsEmpty(world_name)) return false;
		final World world = Bukkit.getWorld(world_name);
		return (world != null);
	}



	@Override
	public Chunk getChunk() {
		throw new UnsupportedOperationException();
	}
	@Override
	public Block getBlock() {
		final Location loc = this.getUnsafeLocation();
		return loc.getBlock();
	}
	public Location getUnsafeLocation() {
		final World world = this.getWorld();
		if (world == null) return null;
		final Dabc loc = this.value.get();
		final Block block =
			world.getBlockAt(
				(int) loc.a,
				(int) loc.b,
				(int) loc.c
			);
		return block.getLocation();
	}



	@Override
	public int getBlockX() {
		final Dabc loc = this.value.get();
		return (int) Math.round(loc.a);
	}
	@Override
	public int getBlockY() {
		final Dabc loc = this.value.get();
		return (int) Math.round(loc.b);
	}
	@Override
	public int getBlockZ() {
		final Dabc loc = this.value.get();
		return (int) Math.round(loc.c);
	}

	@Override
	public double getX() {
		final Dabc loc = this.value.get();
		return loc.a;
	}
	@Override
	public double getY() {
		final Dabc loc = this.value.get();
		return loc.b;
	}
	@Override
	public double getZ() {
		final Dabc loc = this.value.get();
		return loc.c;
	}

	@Override
	public void setX(final double x) {
		final Dabc loc = this.value.get();
		this.value.set(new Dabc(x, loc.b, loc.c));
	}
	@Override
	public void setY(final double y) {
		final Dabc loc = this.value.get();
		this.value.set(new Dabc(loc.a, y, loc.c));
	}
	@Override
	public void setZ(final double z) {
		final Dabc loc = this.value.get();
		this.value.set(new Dabc(loc.a, loc.b, z));
	}

	public void setXYZ(final double x, final double y, final double z) {
		this.value.set(new Dabc(x, y, z));
	}
	public void setXYZ(final int x, final int y, final int z) {
		this.value.set(new Dabc(x, y, z));
	}

	public void setXYZ(final Dabc loc) {
		this.value.set(new Dabc(loc.a, loc.b, loc.c));
	}
	public void setXYZ(final Iabc loc) {
		this.value.set(new Dabc(loc.a, loc.b, loc.c));
	}

	public void setXYZ(final Location loc) {
		this.setWorld(loc.getWorld());
		this.setXYZ(loc.getX(), loc.getY(), loc.getZ());
	}

	@Override
	public float getPitch() {
		return (float) this.pitch.get();
	}
	@Override
	public void setPitch(float pitch) {
		this.pitch.set(pitch);
	}

	@Override
	public float getYaw() {
		return (float) this.yaw.get();
	}
	@Override
	public void setYaw(float yaw) {
		this.yaw.set(yaw);
	}



	@Override
	public Vector getDirection() {
		final Vector vector = new Vector();
		final double pitch = this.getPitch();
		final double yaw   = this.getYaw();
		final double pitch_rad = Math.toRadians(pitch);
		final double yaw_rad   = Math.toRadians(yaw);
		vector.setY(0.0 - Math.sin(pitch_rad));
		final double xz = Math.cos(pitch_rad);
		vector.setX(0.0 - (xz * Math.sin(yaw_rad)));
		vector.setZ(       xz * Math.cos(yaw_rad));
		return vector;
	}
	@Override
	public Location setDirection(final Vector vector) {
		final double pi2 = Math.PI * 2.0;
		final double x = vector.getX();
		final double z = vector.getZ();
		if (x == 0.0 && z == 0.0) {
			this.setPitch(vector.getY()>0.0 ? -90.0f : 90.0f);
		} else {
			final double theta = Math.atan2(0.0-x, z);
			this.setYaw((float)Math.toDegrees( (theta+pi2)%pi2 ));
			final double xz = Math.sqrt( (x*x) + (z*z) );
			this.setPitch( (float)Math.toDegrees(Math.atan(0.0-vector.getY()/xz)) );
		}
		return this;
	}



	@Override
	public Location add(final Location loc) {
		final Dabc tuple = this.value.get();
		this.value.set(new Dabc(
			tuple.a + loc.getX(),
			tuple.b + loc.getY(),
			tuple.c + loc.getZ()
		));
		return this;
	}
	@Override
	public Location add(final double x, final double y, final double z) {
		final Dabc tuple = this.value.get();
		this.value.set(new Dabc(
			tuple.a + x,
			tuple.b + y,
			tuple.c + z
		));
		return this;
	}

	@Override
	public Location subtract(final Location loc) {
		final Dabc tuple = this.value.get();
		this.value.set(new Dabc(
			tuple.a - loc.getX(),
			tuple.b - loc.getY(),
			tuple.c - loc.getZ()
		));
		return this;
	}
	@Override
	public Location subtract(final double x, final double y, final double z) {
		final Dabc tuple = this.value.get();
		this.value.set(new Dabc(
			tuple.a - x,
			tuple.b - y,
			tuple.c - z
		));
		return this;
	}

	@Override
	public Location multiply(final double m) {
		final Dabc tuple = this.value.get();
		this.value.set(new Dabc(
			tuple.a * m,
			tuple.b * m,
			tuple.c * m
		));
		return this;
	}

	@Override
	public Location zero() {
		this.value.set(new Dabc(0.0, 0.0, 0.0));
		return this;
	}



	@Override
	public boolean equals(final Object obj) {
		if (obj instanceof Location) {
			final Location loc = (Location) obj;
			return BukkitUtils.EqualsLocation(this, loc);
		}
		return false;
	}



	@Override
	public double length() {
		return Distance3D(0.0, 0.0, 0.0, this.getX(), this.getY(), this.getZ());
	}
	@Override
	public double lengthSquared() {
		final double distance = this.length();
		return distance * distance;
	}

	@Override
	public double distance(final Location loc) {
		return Distance3D(
			this.getX(), this.getY(), this.getZ(),
			loc .getX(), loc .getY(), loc .getZ()
		);
	}
	@Override
	public double distanceSquared(final Location loc) {
		final double distance = this.distance(loc);
		return distance * distance;
	}



	@Override
	public int hashCode() {
		final World world = this.getWorld();
		final long x_bits = Double.doubleToLongBits(this.getX());
		final long y_bits = Double.doubleToLongBits(this.getY());
		final long z_bits = Double.doubleToLongBits(this.getZ());
		int hash = 3;
		hash = (hash * 19) + (world==null ? 0 : world.hashCode());
		hash = (hash * 19) + ((int)(x_bits ^ (x_bits >>> 32)));
		hash = (hash * 19) + ((int)(y_bits ^ (y_bits >>> 32)));
		hash = (hash * 19) + ((int)(z_bits ^ (z_bits >>> 32)));
		hash = (hash * 19) + Float.floatToIntBits(this.getPitch());
		hash = (hash * 19) + Float.floatToIntBits(this.getYaw());
		return hash;
	}

	@Override
	public String toString() {
		return (new StringBuilder())
			.append("Location{world=")
			.append(this.getWorldName())
			.append(",x=").append(this.getX())
			.append(",y=").append(this.getY())
			.append(",z=").append(this.getZ())
			.append(",pitch=").append(this.getPitch())
			.append(",yaw="  ).append(this.getYaw()  )
			.append('}')
			.toString();
	}



	@Override
	public Map<String, Object> serialize() {
		final HashMap<String, Object> map = new HashMap<String, Object>();
		final String world_name = this.getWorldName();
		if (!IsEmpty(world_name))
			map.put("world", world_name);
		map.put("x", Double.valueOf(this.getX()));
		map.put("y", Double.valueOf(this.getY()));
		map.put("z", Double.valueOf(this.getZ()));
		map.put("pitch", this.getPitch());
		map.put("yaw",   this.getYaw());
		return map;
	}
	public static LocationSafe deserialize(final Map<String, Object> map) {
		World world = null;
		if (map.containsKey("world")) {
			world = Bukkit.getWorld((String)map.get("world"));
			if (world == null) throw new IllegalArgumentException("unknown world");
		}
		final LocationSafe loc = new LocationSafe(
			world,
			CastDouble(map.get("x")),
			CastDouble(map.get("y")),
			CastDouble(map.get("z"))
		);
		if (map.containsKey("pitch")) loc.setPitch((float)CastDouble(map.get("pitch")).doubleValue());
		if (map.containsKey("yaw"))   loc.setYaw  ((float)CastDouble(map.get("yaw"  )).doubleValue());
		return loc;
	}



	@Override
	public Vector toVector() {
		return new Vector(this.getX(), this.getY(), this.getZ());
	}



	@Override
	public void checkFinite() throws IllegalArgumentException {
		NumberConversions.checkFinite(this.getX(),     "x not finite"    );
		NumberConversions.checkFinite(this.getY(),     "y not finite"    );
		NumberConversions.checkFinite(this.getZ(),     "z not finite"    );
		NumberConversions.checkFinite(this.getPitch(), "pitch not finite");
		NumberConversions.checkFinite(this.getYaw(),   "yaw not finite"  );
	}



}

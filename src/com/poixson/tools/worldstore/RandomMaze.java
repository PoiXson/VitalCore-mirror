package com.poixson.tools.worldstore;

import static com.poixson.tools.gson.GsonProvider.GSON;
import static com.poixson.utils.MathUtils.ToDouble;
import static com.poixson.utils.Utils.SafeClose;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;

import com.google.common.util.concurrent.AtomicDoubleArray;
import com.poixson.tools.xJavaPlugin;
import com.poixson.tools.xRand;
import com.poixson.tools.dao.Iab;
import com.poixson.utils.MathUtils;


public class RandomMaze extends WorldStore_HashMap<Iab, AtomicDoubleArray> {

	protected final xRand random = (new xRand()).seed_time();

	protected final double path_chance;



	public RandomMaze(final xJavaPlugin plugin,
			final String world, final String type,
			final double chance) {
		this(plugin, world, type, chance, 32);
	}
	public RandomMaze(final xJavaPlugin plugin,
			final String world, final String type,
			final double chance, final int group_size) {
		super(plugin, world, type, group_size);
		this.path_chance = MathUtils.MinMax(chance, 0.0, 99.0);
	}



	@Override
	public AtomicDoubleArray create(final Iab key) {
		this.init();
		final int array_size = this.group_size * this.group_size;
		final AtomicDoubleArray result = new AtomicDoubleArray(array_size);
		final LinkedList<String> lines = new LinkedList<String>();
		for (int iz=0; iz<this.group_size; iz++) {
			final StringBuilder line = new StringBuilder();
			for (int ix=0; ix<this.group_size; ix++) {
				final int index = (iz * this.group_size) + ix;
				final double rnd = this.random.nextDouble(0.0, this.path_chance);
				final double val = MathUtils.FloorNormal(rnd, 0.01);
				result.set(index, val);
				if (ix > 0)
					line.append(' ');
				line.append(MathUtils.FormatDecimal("00.00", val));
			}
			lines.addLast(line.toString());
		}
		// save immediately
		final File file = this.getFile(key);
		final String json = GSON().toJson(lines);
		BufferedWriter writer = null;
		try {
			writer = new BufferedWriter(new FileWriter(file));
			writer.write(json);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			SafeClose(writer);
		}
		return result;
	}

	@Override
	protected AtomicDoubleArray load_decode(final String json) {
		final int array_size = this.group_size * this.group_size;
		final AtomicDoubleArray result = new AtomicDoubleArray(array_size);
		final String[] lines = GSON().fromJson(json, String[].class);
		if (lines == null) throw new NullPointerException("Failed to parse json");
		int iz = 0;
		for (final String line : lines) {
			final String[] parts = line.split(" ");
			int ix = 0;
			for (final String part : parts) {
				final int index = (iz * this.group_size) + ix;
				final double value = ToDouble(part);
				result.set(index, value);
				ix++;
			}
			iz++;
		}
		return result;
	}

	public void save(final Iab key) {
		// avoid saving, save only when generating
	}
	@Override
	protected String save_encode(final Iab key, final AtomicDoubleArray value) {
		return null;
	}

	@Override
	public File getFile(final Iab key) {
		String filename = DEFAULT_LOCATION_FILE;
		filename = filename.replace("<name>", this.type);
		filename = filename.replace("<x>", Integer.toString(key.a));
		filename = filename.replace("<z>", Integer.toString(key.b));
		return new File(this.path, filename);
	}



	public double getMazeEntry(final int maze_x, final int maze_z) {
		return this.getMazeEntry(maze_x, maze_z, false, true);
	}
	public double getMazeEntry(final int maze_x, final int maze_z,
			final boolean lazy, final boolean create) {
		final int group_x = this.loc_to_group(maze_x);
		final int group_z = this.loc_to_group(maze_z);
		final AtomicDoubleArray array = this.get(new Iab(group_x, group_z), lazy, create);
		if (array == null)
			return Double.MIN_VALUE;
		return array.get(this.loc_to_index(maze_x, maze_z));
	}



}

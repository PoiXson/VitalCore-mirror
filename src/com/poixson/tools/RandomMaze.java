package com.poixson.tools;

import static com.poixson.utils.MathUtils.ToDouble;
import static com.poixson.utils.Utils.SafeClose;
import static com.poixson.utils.gson.GsonUtils.GSON;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;

import org.bukkit.plugin.java.JavaPlugin;

import com.google.common.util.concurrent.AtomicDoubleArray;
import com.poixson.tools.dao.Iab;
import com.poixson.tools.worldstore.WorldStore_Map;
import com.poixson.utils.MathUtils;


public class RandomMaze extends WorldStore_Map<Iab, AtomicDoubleArray> {

	protected final xRand random = (new xRand()).seed_time();

	protected final double path_chance;
	protected final int size_x, size_z;



	public RandomMaze(final JavaPlugin plugin,
			final String world, final String type, final double chance,
			final int size_x, final int size_z) {
		super(plugin, world, type);
		this.path_chance = MathUtils.MinMax(chance, 0.0, 99.0) + 3;
		this.size_x = size_x;
		this.size_z = size_z;
	}



	@Override
	public AtomicDoubleArray load(final Iab key) {
		// load existing
		{
			final AtomicDoubleArray array = super.load(key);
			if (array != null)
				return array;
		}
		// generate maze
		{
			final int size_total = this.size_x * this.size_z;
			final AtomicDoubleArray array = new AtomicDoubleArray(size_total);
			final LinkedList<String> lines = new LinkedList<String>();
			for (int iz=0; iz<this.size_z; iz++) {
				final StringBuilder line = new StringBuilder();
				for (int ix=0; ix<this.size_x; ix++) {
					final int index = (iz * this.size_z) + ix;
					final double rnd = this.random.nextDbl(0.0, this.path_chance);
					final double val = MathUtils.Floor(rnd, 0.01);
					array.set(index, val);
					if (ix > 0)
						line.append(' ');
					line.append(MathUtils.FormatDecimal("00.00", val));
				}
				lines.addLast(line.toString());
			}
			// save
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
			return array;
		}
	}

	@Override
	protected AtomicDoubleArray load_decode(final String json) {
		final int size_total = this.size_x * this.size_z;
		final AtomicDoubleArray result = new AtomicDoubleArray(size_total);
		final String[] lines = GSON().fromJson(json, String[].class);
		int iz = 0;
		for (final String line : lines) {
			final String[] parts = line.split(" ");
			int ix = 0;
			for (final String part : parts) {
				final int index = (iz * this.size_z) + ix;
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



}

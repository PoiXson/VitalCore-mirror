package com.poixson.tools.plotter.generation;

import static com.poixson.utils.MathUtils.Distance3D;
import static com.poixson.utils.MathUtils.DistanceFast3D;
import static com.poixson.utils.MathUtils.IsMinMax;
import static com.poixson.utils.MathUtils.MinMax;
import static com.poixson.utils.MathUtils.Rotate3D;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.data.BlockData;
import org.bukkit.generator.ChunkGenerator.ChunkData;
import org.bukkit.generator.LimitedRegion;

import com.poixson.tools.xRand;
import com.poixson.tools.abstractions.Tuple;
import com.poixson.tools.dao.Dabc;
import com.poixson.tools.plotter.BlockPlotter;
import com.poixson.tools.plotter.placer.BlockPlacer;
import com.poixson.utils.MathUtils;


public class TreeBuilder {
	public static final double DEFAULT_HEIGHT_MIN               =  5.0;
	public static final double DEFAULT_HEIGHT_MAX               = 65.0;
	public static final double DEFAULT_HEIGHT_WEIGHT            =  1.0;
	public static final double DEFAULT_TRUNK_SIZE_MIN           =  1.0;
	public static final double DEFAULT_TRUNK_SIZE_MAX           =  4.5;
	public static final double DEFAULT_TRUNK_SIZE_FACTOR        =  0.075;
	public static final double DEFAULT_TRUNK_SIZE_MODIFY_MIN    = -0.8;
	public static final double DEFAULT_TRUNK_SIZE_MODIFY_MAX    =  0.8;
	public static final double DEFAULT_TRUNK_SIZE_MODIFY_WEIGHT =  0.6;
	public static final double DEFAULT_BRANCHES_FROM_TOP        =  3.5;
	public static final double DEFAULT_BRANCH_ZONE_PERCENT      =  0.6;
	public static final double DEFAULT_BRANCH_LENGTH_MIN        =  3.8;
	public static final double DEFAULT_BRANCH_LENGTH_MAX        =  7.7;
	public static final double DEFAULT_BRANCH_LENGTH_WEIGHT     =  0.7;
	public static final double DEFAULT_BRANCH_ATTEN_MIN         =  0.6;
	public static final double DEFAULT_BRANCH_ATTEN_MAX         =  0.75;
	public static final double DEFAULT_BRANCH_TIER_LEN_ADD_MIN  =  1.05;
	public static final double DEFAULT_BRANCH_TIER_LEN_ADD_MAX  =  1.15;
	public static final double DEFAULT_BRANCH_TIER_SPACE_MIN    =  0.5;
	public static final double DEFAULT_BRANCH_TIER_SPACE_MAX    =  0.8;
	public static final double DEFAULT_BRANCH_YAW_ADD_MIN       =  0.2;
	public static final double DEFAULT_BRANCH_YAW_ADD_MAX       =  0.7;
	public static final double DEFAULT_BRANCH_PITCH_MIN         = -0.05;
	public static final double DEFAULT_BRANCH_PITCH_MAX         =  0.15;
	public static final double DEFAULT_BRANCH_PITCH_WEIGHT      =  0.5;
	public static final double DEFAULT_BRANCH_PITCH_MODIFY_MIN  = -0.06;
	public static final double DEFAULT_BRANCH_PITCH_MODIFY_MAX  =  0.04;
	public static final double DEFAULT_BRANCH_SPLIT_MIN_LENGTH  =  6.0;
	public static final double DEFAULT_BRANCH_NUM_SPLITS_MIN    =  1;
	public static final double DEFAULT_BRANCH_NUM_SPLITS_MAX    =  3;
	public static final double DEFAULT_BRANCH_NUM_SPLITS_WEIGHT =  1.3;
	public static final double DEFAULT_LEAVES_THICKNESS         =  2.0;
	public static final boolean DEFAULT_IS_DEAD                 = false;

	public final TreeStyle style;
	public final int y_min;
	public final int y_max;

	protected double height_min              = DEFAULT_HEIGHT_MIN;
	protected double height_max              = DEFAULT_HEIGHT_MAX;
	protected double trunk_size_min          = DEFAULT_TRUNK_SIZE_MIN;
	protected double trunk_size_max          = DEFAULT_TRUNK_SIZE_MAX;
	protected double trunk_size_factor       = DEFAULT_TRUNK_SIZE_FACTOR;
	protected double trunk_size_modify_min   = DEFAULT_TRUNK_SIZE_MODIFY_MIN;
	protected double trunk_size_modify_max   = DEFAULT_TRUNK_SIZE_MODIFY_MAX;
	protected double branches_from_top       = DEFAULT_BRANCHES_FROM_TOP;
	protected double branch_zone_percent     = DEFAULT_BRANCH_ZONE_PERCENT;
	protected double branch_length_min       = DEFAULT_BRANCH_LENGTH_MIN;
	protected double branch_length_max       = DEFAULT_BRANCH_LENGTH_MAX;
	protected double branch_length_weight    = DEFAULT_BRANCH_LENGTH_WEIGHT;
	protected double branch_attenuation_min  = DEFAULT_BRANCH_ATTEN_MIN;
	protected double branch_attenuation_max  = DEFAULT_BRANCH_ATTEN_MAX;
	protected double branch_tier_len_add_min = DEFAULT_BRANCH_TIER_LEN_ADD_MIN;
	protected double branch_tier_len_add_max = DEFAULT_BRANCH_TIER_LEN_ADD_MAX;
	protected double branch_tier_space_min   = DEFAULT_BRANCH_TIER_SPACE_MIN;
	protected double branch_tier_space_max   = DEFAULT_BRANCH_TIER_SPACE_MAX;
	protected double branch_yaw_add_min      = DEFAULT_BRANCH_YAW_ADD_MIN;
	protected double branch_yaw_add_max      = DEFAULT_BRANCH_YAW_ADD_MAX;
	protected double branch_pitch_min        = DEFAULT_BRANCH_PITCH_MIN;
	protected double branch_pitch_max        = DEFAULT_BRANCH_PITCH_MAX;
	protected double branch_pitch_modify_min = DEFAULT_BRANCH_PITCH_MODIFY_MIN;
	protected double branch_pitch_modify_max = DEFAULT_BRANCH_PITCH_MODIFY_MAX;
	protected double branch_split_min_length = DEFAULT_BRANCH_SPLIT_MIN_LENGTH;
	protected double branch_num_splits_min   = DEFAULT_BRANCH_NUM_SPLITS_MIN;
	protected double branch_num_splits_max   = DEFAULT_BRANCH_NUM_SPLITS_MAX;
	protected double leaves_thickness        = DEFAULT_LEAVES_THICKNESS;
	protected boolean is_dead                = DEFAULT_IS_DEAD;

	protected final xRand rnd_height          = (new xRand()).seed_time().weight(DEFAULT_HEIGHT_WEIGHT);
	protected final xRand rnd_yaw             = (new xRand()).seed_time();
	protected final xRand rnd_pitch_major     = (new xRand()).seed_time().weight(DEFAULT_BRANCH_PITCH_WEIGHT);
	protected final xRand rnd_pitch_minor     = (new xRand()).seed_time();
	protected final xRand rnd_branch_len      = (new xRand()).seed_time();
	protected final xRand rnd_branch_tier_len = (new xRand()).seed_time();
	protected final xRand rnd_branch_tier     = (new xRand()).seed_time();
	protected final xRand rnd_branch_splits   = (new xRand()).seed_time().weight(DEFAULT_BRANCH_NUM_SPLITS_WEIGHT);
	protected final xRand rnd_trunk_size      = (new xRand()).seed_time().weight(DEFAULT_TRUNK_SIZE_MODIFY_WEIGHT);



	public TreeBuilder(final String style, final int y_min, final int y_max) {
		this(TreeStyle.valueOf(style), y_min, y_max);
	}
	public TreeBuilder(final TreeStyle style, final int y_min, final int y_max) {
		if (style == null)              throw new IllegalArgumentException("Tree style not set");
		if (!IsMinMax(y_min, -64, 320)) throw new IllegalArgumentException("Invalid y_min");
		if (!IsMinMax(y_max, -64, 320)) throw new IllegalArgumentException("Invalid y_max");
		this.style = style;
		this.y_min = y_min;
		this.y_max = y_max;
	}



	public boolean run(final BlockPlotter plot, final World world) {
		return this.run(plot, new BlockPlacer(world));
	}
	public boolean run(final BlockPlotter plot, final ChunkData chunk) {
		return this.run(plot, new BlockPlacer(chunk));
	}
	public boolean run(final BlockPlotter plot, final LimitedRegion region) {
		return this.run(plot, new BlockPlacer(region));
	}

	public boolean run(final BlockPlotter plot, final World world,
			final int height, final int trunk_size) {
		return this.run(plot, new BlockPlacer(world), height, trunk_size);
	}
	public boolean run(final BlockPlotter plot, final ChunkData chunk,
			final int height, final int trunk_size) {
		return this.run(plot, new BlockPlacer(chunk), height, trunk_size);
	}
	public boolean run(final BlockPlotter plot, final LimitedRegion region,
			final int height, final int trunk_size) {
		return this.run(plot, new BlockPlacer(region), height, trunk_size);
	}

	protected Tuple<Double, Double> newRandomTree() {
		final double height = this.rnd_height.nextDouble(this.height_min, this.height_max);
		final double trunk_size_tmp = height * this.trunk_size_factor;
		final double trunk_size_rnd = this.rnd_trunk_size.nextDouble(this.trunk_size_modify_min, this.trunk_size_modify_max);
		final double trunk_size = MinMax(trunk_size_tmp+trunk_size_rnd, this.trunk_size_min, this.trunk_size_max);
		return new Tuple<Double, Double>(Double.valueOf(height), Double.valueOf(trunk_size));
	}

	public boolean run(final BlockPlotter plot, final BlockPlacer placer) {
		this.validate();
		final int chunk_edge =
			Math.min(
				8 - Math.abs(Math.abs(plot.x % 16)-8),
				8 - Math.abs(Math.abs(plot.z % 16)-8)
			);
		double height     = 0.0;
		double trunk_size = 0.0;
		LOOP_TRIES:
		for (int i=0; i<5; i++) {
			final Tuple<Double, Double> tup = this.newRandomTree();
			height     = tup.key.doubleValue();
			trunk_size = tup.val.doubleValue();
			if (trunk_size > 2
			&&  trunk_size * 2.0 > (double)chunk_edge)
				continue LOOP_TRIES;
			break;
		}
		return this.run(plot, placer, height, trunk_size);
	}
	public boolean run(final BlockPlotter plot, final BlockPlacer placer,
			final double height, final double trunk_size) {
		this.validate();
		// find ground surface
		plot.y(this.y_min);
		{
			final int y_min  = 0;
			final int y_max  = this.y_max - this.y_min;
			final int search = (int) Math.floor(trunk_size / 2.0);
			final int surface_y = plot.findSurfaceY(placer, y_min, y_max, search);
			if (surface_y == Integer.MIN_VALUE)
				return false;
			plot.y += surface_y;
			LOOP_SURFACE:
			for (int i=0; i<=2; i++) {
				final BlockData block_under = plot.getBlock(placer, 0, 0-i, 0);
				if (block_under != null) {
					switch (block_under.getMaterial()) {
					case AIR:
						continue LOOP_SURFACE;
					case DIRT:
					case GRASS_BLOCK:
						break LOOP_SURFACE;
					default:
						return false;
					}
				}
			}
		}
		// trunk
		this.build_trunk(plot, placer, height, trunk_size);
		// branches
		final double branch_highest = height - this.branches_from_top;
		final double branch_lowest  = height * (1.0 - this.branch_zone_percent);
		plot.y += (int)Math.round(branch_highest);
		double branch_len = MathUtils.Remap(
			this.height_min, this.height_max,
			this.branch_length_min, this.branch_length_max,
			height, this.branch_length_weight
		);
		double down = 0.0;
		for (double iy=branch_highest; iy>branch_lowest; iy-=down) {
			branch_len *= this.rnd_branch_tier_len.nextDouble(this.branch_tier_len_add_min, this.branch_tier_len_add_max);
			this.build_branches(plot, placer, branch_len);
			final double down_percent = this.rnd_branch_tier.nextDouble(this.branch_tier_space_min, this.branch_tier_space_max);
			down = down_percent * ((height - iy) + 1);
			plot.y -= down;
		}
		return true;
	}



	public void build_trunk(final BlockPlotter plot, final BlockPlacer placer,
			final double height, final double trunk_size) {
		final int h = (int) Math.round(height);
		final double trunk_size_half = trunk_size / 2.0;
		final int half_floor = (int) Math.floor(trunk_size_half);
		final int half_ceil  = (int) Math.ceil( trunk_size_half);
		final int top_leaves_h = 2;
		for (int iz=0-half_ceil; iz<=half_floor; iz++) {
			for (int ix=0-half_ceil; ix<=half_floor; ix++) {
				if (MathUtils.Distance2D(0, 0, ix, iz) <= trunk_size_half) {
					for (int iy=0; iy<h; iy++) {
						plot.setBlock(placer, ix, iy, iz, '|');
						if (top_leaves_h+iy >= h)
							this.build_leaves(plot, placer, ix, iy, iz);
					}
				}
			}
		}
	}



	public void build_branches(final BlockPlotter plot, final BlockPlacer placer,
			final double len) {
		double yaw = this.rnd_yaw.nextDouble(0.0, 2.0);
		double yaw_total = 0.0;
		while (true) {
			final double yaw_add = this.rnd_yaw.nextDouble(this.branch_yaw_add_min, this.branch_yaw_add_max);
			yaw       += yaw_add;
			yaw_total += yaw_add;
			if (yaw_total >= 2.0) break;
			final double pitch_major = this.rnd_pitch_major.nextDouble(this.branch_pitch_min, this.branch_pitch_max);
			this.build_branch(plot, placer, yaw, pitch_major, len);
		}
	}
	public void build_branch(final BlockPlotter plot, final BlockPlacer placer,
			final double yaw, final double pitch_major, final double len) {
		final double pitch_minor = this.rnd_pitch_minor.nextDouble(this.branch_pitch_modify_min, this.branch_pitch_modify_max);
		final Dabc dir = Rotate3D(1, 0, 0, yaw, pitch_major+pitch_minor);
		final int ln = (int) Math.round(len);
		for (int i=1; i<ln; i++) {
			final int xx = (int) Math.round(dir.a * i);
			final int yy = (int) Math.round(dir.b * i);
			final int zz = (int) Math.round(dir.c * i);
			plot.setBlock(placer, xx, yy, zz, '-');
			this.build_leaves(plot, placer, xx, yy, zz);
		}
		// branch split node
		if (len >= this.branch_split_min_length) {
			final double ln_next = len * this.rnd_branch_len.nextDouble(this.branch_attenuation_min, this.branch_attenuation_max);
			int last_x = plot.x;
			int last_y = plot.y;
			int last_z = plot.z;
			plot.x += (int)Math.round(dir.a * ln_next);
			plot.y += (int)Math.round(dir.b * ln_next);
			plot.z += (int)Math.round(dir.c * ln_next);
			final int splits =
				MinMax((int)Math.round(MinMax(
					this.rnd_branch_splits.nextDouble(this.branch_num_splits_min, this.branch_num_splits_max),
					this.branch_num_splits_min, this.branch_num_splits_max
				)), 0, 5);
			final double yaw_per = (splits>1 ? 0.6/(((double)splits)-1) : 0.0);
			double yaws = 0.0 - ((((double)splits)/2.0) * yaw_per);
			for (int i=0; i<splits; i++) {
				this.build_branch(plot, placer, yaw+yaws, pitch_major, ln_next);
				yaws += yaw_per;
			}
			plot.x = last_x;
			plot.y = last_y;
			plot.z = last_z;
		}
	}



	public void build_leaves(final BlockPlotter plot, final BlockPlacer placer,
			final int x, final int y, final int z) {
		if (!this.is_dead) {
			final int thick = (int) Math.ceil(this.leaves_thickness);
			for (int iz=0-thick; iz<=thick; iz++) {
				for (int ix=0-thick; ix<=thick; ix++) {
					for (int iy=0-thick; iy<=thick; iy++) {
						if (ix != 0 || iy != 0 || iz != 0) {
							final double dist = (
									Distance3D(    0, 0, 0, ix, iy, iz) +
									DistanceFast3D(0, 0, 0, ix, iy, iz)
								) / 2.0;
							if (dist <= this.leaves_thickness)
								plot.replaceBlock(placer, x+ix, y+iy, z+iz, Material.AIR, '#');
						}
					}
				}
			}
		}
	}



	// -------------------------------------------------------------------------------
	// parameters



	// tree height
	public TreeBuilder setHeightMin(final double height) {
		this.height_min = height;
		return this;
	}
	public TreeBuilder setHeightMax(final double height) {
		this.height_max = height;
		return this;
	}
	public TreeBuilder setHeightWeight(final double weight) {
		this.rnd_height.weight(weight);
		return this;
	}



	// trunk size
	public TreeBuilder setTrunkSizeMin(final double size) {
		this.trunk_size_min = size;
		return this;
	}
	public TreeBuilder setTrunkSizeMax(final double size) {
		this.trunk_size_max = size;
		return this;
	}

	// tree size by height
	public TreeBuilder setTrunkSizeFactor(final double factor) {
		this.trunk_size_factor = factor;
		return this;
	}

	// modified tree size
	public TreeBuilder setTrunkSizeModifyMin(final double value) {
		this.trunk_size_modify_min = value;
		return this;
	}
	public TreeBuilder setTrunkSizeModifyMax(final double value) {
		this.trunk_size_modify_max = value;
		return this;
	}
	public TreeBuilder setTrunkSizeModifyWeight(final double weight) {
		this.rnd_trunk_size.weight(weight);
		return this;
	}



	// distance from top to start branches
	public TreeBuilder setBranchesFromTop(final double dist) {
		this.branches_from_top = dist;
		return this;
	}
	// percent of tree height from top to place branches
	public TreeBuilder setBranchZonePercent(final double percent) {
		this.branch_zone_percent = percent;
		return this;
	}



	// branch length
	public TreeBuilder setBranchLengthMin(final double length) {
		this.branch_length_min = length;
		return this;
	}
	public TreeBuilder setBranchLengthMax(final double length) {
		this.branch_length_max = length;
		return this;
	}
	public TreeBuilder setBranchLengthWeight(final double weight) {
		this.branch_length_weight = weight;
		return this;
	}

	// shrinking spacing between branches
	public TreeBuilder setBranchAttenuationMin(final double factor) {
		this.branch_attenuation_min = factor;
		return this;
	}
	public TreeBuilder setBranchAttenuationMax(final double factor) {
		this.branch_attenuation_max = factor;
		return this;
	}



	// branch length add per tier
	public TreeBuilder setBranchTierLenAddMin(final double factor) {
		this.branch_tier_len_add_min = factor;
		return this;
	}
	public TreeBuilder setBranchTierLenAddMax(final double factor) {
		this.branch_tier_len_add_max = factor;
		return this;
	}



	// spacing between branch tiers
	public TreeBuilder setBranchTierSpaceMin(final double space) {
		this.branch_tier_space_min = space;
		return this;
	}
	public TreeBuilder setBranchTierSpaceMax(final double space) {
		this.branch_tier_space_max = space;
		return this;
	}



	// add yaw for each branch from trunk
	public TreeBuilder setBranchYawAddMin(final double yaw) {
		this.branch_yaw_add_min = yaw;
		return this;
	}
	public TreeBuilder setBranchYawAddMax(final double yaw) {
		this.branch_yaw_add_max = yaw;
		return this;
	}



	// branch pitch
	public TreeBuilder setBranchPitchMin(final double pitch) {
		this.branch_pitch_min = pitch;
		return this;
	}
	public TreeBuilder setBranchPitchMax(final double pitch) {
		this.branch_pitch_max = pitch;
		return this;
	}
	public TreeBuilder setBranchPitchWeight(final double weight) {
		this.rnd_pitch_major.weight(weight);
		return this;
	}

	public TreeBuilder setBranchPitchModifyMin(final double pitch) {
		this.branch_pitch_modify_min = pitch;
		return this;
	}
	public TreeBuilder setBranchPitchModifyMax(final double pitch) {
		this.branch_pitch_modify_max = pitch;
		return this;
	}



	// branch split min length
	public TreeBuilder setBranchSplitMinLength(final double length) {
		this.branch_split_min_length = length;
		return this;
	}



	// number of child branches at a node
	public TreeBuilder setBranchNumSplitsMin(final double num) {
		this.branch_num_splits_min = num;
		return this;
	}
	public TreeBuilder setBranchNumSplitsMax(final double num) {
		this.branch_num_splits_max = num;
		return this;
	}
	public TreeBuilder setBranchNumSplitsWeight(final double weight) {
		this.rnd_branch_splits.weight(weight);
		return this;
	}



	// leaves thickness
	public TreeBuilder setLeavesThickness(final double thick) {
		this.leaves_thickness = thick;
		return this;
	}



	// living/dead tree
	public TreeBuilder setLiving() {
		this.setDead(false);
		return this;
	}
	public TreeBuilder setDead() {
		this.setDead(true);
		return this;
	}
	public TreeBuilder setDead(final boolean dead) {
		this.is_dead = dead;
		return this;
	}



	// -------------------------------------------------------------------------------
	// validate parameters



	public void validate() {
		if (!IsMinMax(this.height_min,                 1.0, 500.0)) throw new IllegalArgumentException("Invalid height_min: "             +Double.toString(this.height_min             ));
		if (!IsMinMax(this.height_max,                 1.0, 500.0)) throw new IllegalArgumentException("Invalid height_max: "             +Double.toString(this.height_max             ));
		if (!IsMinMax(this.trunk_size_min,             1.0, 100.0)) throw new IllegalArgumentException("Invalid trunk_size_min: "         +Double.toString(this.trunk_size_min         ));
		if (!IsMinMax(this.trunk_size_max,             1.0, 100.0)) throw new IllegalArgumentException("Invalid trunk_size_max: "         +Double.toString(this.trunk_size_max         ));
		if (!IsMinMax(this.trunk_size_factor,          0.0,   1.0)) throw new IllegalArgumentException("Invalid trunk_size_factor: "      +Double.toString(this.trunk_size_factor      ));
		if (!IsMinMax(this.trunk_size_modify_min,   -100.0, 100.0)) throw new IllegalArgumentException("Invalid trunk_size_modify_min: "  +Double.toString(this.trunk_size_modify_min  ));
		if (!IsMinMax(this.trunk_size_modify_max,   -100.0, 100.0)) throw new IllegalArgumentException("Invalid trunk_size_modify_max: "  +Double.toString(this.trunk_size_modify_max  ));
		if (!IsMinMax(this.branches_from_top,          0.0, 100.0)) throw new IllegalArgumentException("Invalid branches_from_top: "      +Double.toString(this.branches_from_top      ));
		if (!IsMinMax(this.branch_zone_percent,        0.0,   1.0)) throw new IllegalArgumentException("Invalid branch_zone_percent: "    +Double.toString(this.branch_zone_percent    ));
		if (!IsMinMax(this.branch_length_min,          0.0, 100.0)) throw new IllegalArgumentException("Invalid branch_length_min: "      +Double.toString(this.branch_length_min      ));
		if (!IsMinMax(this.branch_length_max,          0.0, 100.0)) throw new IllegalArgumentException("Invalid branch_length_max: "      +Double.toString(this.branch_length_max      ));
		if (!IsMinMax(this.branch_length_weight,    -100.0, 100.0)) throw new IllegalArgumentException("Invalid branch_length_weight: "   +Double.toString(this.branch_length_weight   ));
		if (!IsMinMax(this.branch_attenuation_min,  -100.0, 100.0)) throw new IllegalArgumentException("Invalid branch_attenuation_min: " +Double.toString(this.branch_attenuation_min ));
		if (!IsMinMax(this.branch_attenuation_max,  -100.0, 100.0)) throw new IllegalArgumentException("Invalid branch_attenuation_max: " +Double.toString(this.branch_attenuation_max ));
		if (!IsMinMax(this.branch_tier_len_add_min, -100.0, 100.0)) throw new IllegalArgumentException("Invalid branch_tier_len_add_min: "+Double.toString(this.branch_tier_len_add_min));
		if (!IsMinMax(this.branch_tier_len_add_max, -100.0, 100.0)) throw new IllegalArgumentException("Invalid branch_tier_len_add_max: "+Double.toString(this.branch_tier_len_add_max));
		if (!IsMinMax(this.branch_tier_space_min,   -100.0, 100.0)) throw new IllegalArgumentException("Invalid branch_tier_space_min: "  +Double.toString(this.branch_tier_space_min  ));
		if (!IsMinMax(this.branch_tier_space_max,   -100.0, 100.0)) throw new IllegalArgumentException("Invalid branch_tier_space_max: "  +Double.toString(this.branch_tier_space_max  ));
		if (!IsMinMax(this.branch_yaw_add_min,      -100.0, 100.0)) throw new IllegalArgumentException("Invalid branch_yaw_add_min: "     +Double.toString(this.branch_yaw_add_min     ));
		if (!IsMinMax(this.branch_yaw_add_max,      -100.0, 100.0)) throw new IllegalArgumentException("Invalid branch_yaw_add_max: "     +Double.toString(this.branch_yaw_add_max     ));
		if (!IsMinMax(this.branch_pitch_min,        -100.0, 100.0)) throw new IllegalArgumentException("Invalid branch_pitch_min: "       +Double.toString(this.branch_pitch_min       ));
		if (!IsMinMax(this.branch_pitch_max,        -100.0, 100.0)) throw new IllegalArgumentException("Invalid branch_pitch_max: "       +Double.toString(this.branch_pitch_max       ));
		if (!IsMinMax(this.branch_pitch_modify_min, -100.0, 100.0)) throw new IllegalArgumentException("Invalid branch_pitch_modify_min: "+Double.toString(this.branch_pitch_modify_min));
		if (!IsMinMax(this.branch_pitch_modify_max, -100.0, 100.0)) throw new IllegalArgumentException("Invalid branch_pitch_modify_max: "+Double.toString(this.branch_pitch_modify_max));
		if (!IsMinMax(this.branch_split_min_length,    0.0, 100.0)) throw new IllegalArgumentException("Invalid branch_split_min_length: "+Double.toString(this.branch_split_min_length));
		if (!IsMinMax(this.branch_num_splits_min,      0.0,  10.0)) throw new IllegalArgumentException("Invalid branch_num_splits_min: "  +Double.toString(this.branch_num_splits_min  ));
		if (!IsMinMax(this.branch_num_splits_max,      0.0,  10.0)) throw new IllegalArgumentException("Invalid branch_num_splits_max: "  +Double.toString(this.branch_num_splits_max  ));
		if (!IsMinMax(this.leaves_thickness,           1.0,   5.0)) throw new IllegalArgumentException("Invalid leaves_thickness: "       +Double.toString(this.leaves_thickness       ));
	}



}

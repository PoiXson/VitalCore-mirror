package com.poixson.commonmc.utils;

import static com.poixson.commonmc.utils.LocationUtils.AxToFace;
import static com.poixson.commonmc.utils.LocationUtils.AxToIxyz;
import static com.poixson.commonmc.utils.LocationUtils.AxToIxz;
import static com.poixson.commonmc.utils.LocationUtils.FaceToAxChar;
import static com.poixson.commonmc.utils.LocationUtils.FaceToAxString;
import static com.poixson.commonmc.utils.LocationUtils.FaceToAxis;
import static com.poixson.commonmc.utils.LocationUtils.FaceToIxyz;
import static com.poixson.commonmc.utils.LocationUtils.FaceToIxz;
import static com.poixson.commonmc.utils.LocationUtils.Rotate;
import static com.poixson.commonmc.utils.LocationUtils.ValueToFaceQuarter;

import org.bukkit.Axis;
import org.bukkit.block.BlockFace;
import org.junit.Assert;
import org.junit.Test;

import com.poixson.tools.dao.Iab;
import com.poixson.tools.dao.Iabc;
import com.poixson.tools.dao.Iabcd;


public class Test_LocationUtils {



	@Test
	public void testRotate() {
		// x, z, w, d
		Assert.assertEquals(new Iabcd(-3, -7,  2, -3), Rotate(new Iabcd(5, 10, 2, 3), BlockFace.NORTH));
		Assert.assertEquals(new Iabcd( 5, 10,  2,  3), Rotate(new Iabcd(5, 10, 2, 3), BlockFace.SOUTH));
		Assert.assertEquals(new Iabcd(10,  5,  3,  2), Rotate(new Iabcd(5, 10, 2, 3), BlockFace.EAST ));
		Assert.assertEquals(new Iabcd(-7, -3, -3,  2), Rotate(new Iabcd(5, 10, 2, 3), BlockFace.WEST ));
		// rotate BlockFace by double
		Assert.assertEquals(BlockFace.NORTH, Rotate(BlockFace.NORTH, 0.0 ));
		Assert.assertEquals(BlockFace.EAST,  Rotate(BlockFace.NORTH, 0.25));
		Assert.assertEquals(BlockFace.SOUTH, Rotate(BlockFace.NORTH, 0.5 ));
		Assert.assertEquals(BlockFace.WEST,  Rotate(BlockFace.NORTH, 0.75));
		Assert.assertEquals(BlockFace.NORTH, Rotate(BlockFace.NORTH, 1.0 ));
		Assert.assertEquals(BlockFace.NORTH, Rotate(BlockFace.SOUTH, 0.5 ));
		// rotate ax by double
		Assert.assertEquals('n', Rotate('n', 0.0 ));
		Assert.assertEquals('e', Rotate('n', 0.25));
		Assert.assertEquals('s', Rotate('n', 0.5 ));
		Assert.assertEquals('w', Rotate('n', 0.75));
		Assert.assertEquals('n', Rotate('n', 1.0 ));
		Assert.assertEquals('n', Rotate('s', 0.5 ));
		// rotate axis by BlockFace
		Assert.assertEquals("udnsew", Rotate("udnsew", BlockFace.SOUTH));
		Assert.assertEquals("udewsn", Rotate("udnsew", BlockFace.WEST ));
		Assert.assertEquals("udsnwe", Rotate("udnsew", BlockFace.NORTH));
		Assert.assertEquals("udwens", Rotate("udnsew", BlockFace.EAST ));
	}



	@Test
	public void testConversions() {
		// BlockFace to Axis
		Assert.assertEquals(Axis.X, FaceToAxis(BlockFace.EAST ));
		Assert.assertEquals(Axis.X, FaceToAxis(BlockFace.WEST ));
		Assert.assertEquals(Axis.Y, FaceToAxis(BlockFace.UP   ));
		Assert.assertEquals(Axis.Y, FaceToAxis(BlockFace.DOWN ));
		Assert.assertEquals(Axis.Z, FaceToAxis(BlockFace.NORTH));
		Assert.assertEquals(Axis.Z, FaceToAxis(BlockFace.SOUTH));
		// BlockFace to axis char
		Assert.assertEquals('n', FaceToAxChar(BlockFace.NORTH));
		Assert.assertEquals('s', FaceToAxChar(BlockFace.SOUTH));
		Assert.assertEquals('e', FaceToAxChar(BlockFace.EAST ));
		Assert.assertEquals('w', FaceToAxChar(BlockFace.WEST ));
		// BlockFace to axis string
		Assert.assertEquals("n",  FaceToAxString(BlockFace.NORTH));
		Assert.assertEquals("s",  FaceToAxString(BlockFace.SOUTH));
		Assert.assertEquals("e",  FaceToAxString(BlockFace.EAST ));
		Assert.assertEquals("w",  FaceToAxString(BlockFace.WEST ));
		Assert.assertEquals("ne", FaceToAxString(BlockFace.NORTH_EAST));
		Assert.assertEquals("nw", FaceToAxString(BlockFace.NORTH_WEST));
		Assert.assertEquals("se", FaceToAxString(BlockFace.SOUTH_EAST));
		Assert.assertEquals("sw", FaceToAxString(BlockFace.SOUTH_WEST));
		// axis char to BlockFace
		Assert.assertEquals(BlockFace.NORTH, AxToFace('n'));
		Assert.assertEquals(BlockFace.NORTH, AxToFace('z'));
		Assert.assertEquals(BlockFace.SOUTH, AxToFace('s'));
		Assert.assertEquals(BlockFace.SOUTH, AxToFace('Z'));
		Assert.assertEquals(BlockFace.EAST,  AxToFace('e'));
		Assert.assertEquals(BlockFace.EAST,  AxToFace('X'));
		Assert.assertEquals(BlockFace.WEST,  AxToFace('w'));
		Assert.assertEquals(BlockFace.WEST,  AxToFace('x'));
		// axis string to BlockFace
		Assert.assertEquals(BlockFace.NORTH, AxToFace("n"));
		Assert.assertEquals(BlockFace.NORTH, AxToFace("z"));
		Assert.assertEquals(BlockFace.SOUTH, AxToFace("s"));
		Assert.assertEquals(BlockFace.SOUTH, AxToFace("Z"));
		Assert.assertEquals(BlockFace.EAST,  AxToFace("e"));
		Assert.assertEquals(BlockFace.EAST,  AxToFace("X"));
		Assert.assertEquals(BlockFace.WEST,  AxToFace("w"));
		Assert.assertEquals(BlockFace.WEST,  AxToFace("x"));
		Assert.assertEquals(BlockFace.NORTH_EAST, AxToFace("ne"));
		Assert.assertEquals(BlockFace.NORTH_WEST, AxToFace("nw"));
		Assert.assertEquals(BlockFace.SOUTH_EAST, AxToFace("se"));
		Assert.assertEquals(BlockFace.SOUTH_WEST, AxToFace("sw"));
		// BlockFace to x,y,z
		Assert.assertEquals(new Iabc( 0, 1, 0), FaceToIxyz(BlockFace.UP   ));
		Assert.assertEquals(new Iabc( 0,-1, 0), FaceToIxyz(BlockFace.DOWN ));
		Assert.assertEquals(new Iabc( 0, 0,-1), FaceToIxyz(BlockFace.NORTH));
		Assert.assertEquals(new Iabc( 0, 0, 1), FaceToIxyz(BlockFace.SOUTH));
		Assert.assertEquals(new Iabc( 1, 0, 0), FaceToIxyz(BlockFace.EAST ));
		Assert.assertEquals(new Iabc(-1, 0, 0), FaceToIxyz(BlockFace.WEST ));
		Assert.assertEquals(new Iabc( 1, 0,-1), FaceToIxyz(BlockFace.NORTH_EAST));
		Assert.assertEquals(new Iabc(-1, 0,-1), FaceToIxyz(BlockFace.NORTH_WEST));
		Assert.assertEquals(new Iabc( 1, 0, 1), FaceToIxyz(BlockFace.SOUTH_EAST));
		Assert.assertEquals(new Iabc(-1, 0, 1), FaceToIxyz(BlockFace.SOUTH_WEST));
		// BlockFace to x,z
		Assert.assertEquals(new Iab( 0,-1), FaceToIxz(BlockFace.NORTH));
		Assert.assertEquals(new Iab( 0, 1), FaceToIxz(BlockFace.SOUTH));
		Assert.assertEquals(new Iab( 1, 0), FaceToIxz(BlockFace.EAST ));
		Assert.assertEquals(new Iab(-1, 0), FaceToIxz(BlockFace.WEST ));
		Assert.assertEquals(new Iab( 1,-1), FaceToIxz(BlockFace.NORTH_EAST));
		Assert.assertEquals(new Iab(-1,-1), FaceToIxz(BlockFace.NORTH_WEST));
		Assert.assertEquals(new Iab( 1, 1), FaceToIxz(BlockFace.SOUTH_EAST));
		Assert.assertEquals(new Iab(-1, 1), FaceToIxz(BlockFace.SOUTH_WEST));
		// axis char to x,y,z
		Assert.assertEquals(new Iabc( 0, 0,-1), AxToIxyz('n'));
		Assert.assertEquals(new Iabc( 0, 0,-1), AxToIxyz('z'));
		Assert.assertEquals(new Iabc( 0, 0, 1), AxToIxyz('s'));
		Assert.assertEquals(new Iabc( 0, 0, 1), AxToIxyz('Z'));
		Assert.assertEquals(new Iabc( 1, 0, 0), AxToIxyz('e'));
		Assert.assertEquals(new Iabc( 1, 0, 0), AxToIxyz('X'));
		Assert.assertEquals(new Iabc(-1, 0, 0), AxToIxyz('w'));
		Assert.assertEquals(new Iabc(-1, 0, 0), AxToIxyz('x'));
		Assert.assertEquals(new Iabc( 0, 1, 0), AxToIxyz('u'));
		Assert.assertEquals(new Iabc( 0, 1, 0), AxToIxyz('Y'));
		Assert.assertEquals(new Iabc( 0,-1, 0), AxToIxyz('d'));
		Assert.assertEquals(new Iabc( 0,-1, 0), AxToIxyz('y'));
		// axis char to x,z
		Assert.assertEquals(new Iab( 0,-1), AxToIxz('n'));
		Assert.assertEquals(new Iab( 0,-1), AxToIxz('z'));
		Assert.assertEquals(new Iab( 0, 1), AxToIxz('s'));
		Assert.assertEquals(new Iab( 0, 1), AxToIxz('Z'));
		Assert.assertEquals(new Iab( 1, 0), AxToIxz('e'));
		Assert.assertEquals(new Iab( 1, 0), AxToIxz('X'));
		Assert.assertEquals(new Iab(-1, 0), AxToIxz('w'));
		Assert.assertEquals(new Iab(-1, 0), AxToIxz('x'));
		// x,z to BlockFace
		Assert.assertEquals(BlockFace.NORTH_EAST, ValueToFaceQuarter( 0,-1));
		Assert.assertEquals(BlockFace.NORTH_WEST, ValueToFaceQuarter(-1,-1));
		Assert.assertEquals(BlockFace.SOUTH_EAST, ValueToFaceQuarter( 0, 0));
		Assert.assertEquals(BlockFace.SOUTH_WEST, ValueToFaceQuarter(-1, 0));
	}



}

package com.poixson.utils;

import static com.poixson.utils.LocationUtils.AxToFace;
import static com.poixson.utils.LocationUtils.AxToIxyz;
import static com.poixson.utils.LocationUtils.AxToIxz;
import static com.poixson.utils.LocationUtils.AxisToFaces2x2;
import static com.poixson.utils.LocationUtils.AxisToIxyz;
import static com.poixson.utils.LocationUtils.AxisToIxz;
import static com.poixson.utils.LocationUtils.FaceToAxChar;
import static com.poixson.utils.LocationUtils.FaceToAxString;
import static com.poixson.utils.LocationUtils.FaceToAxis;
import static com.poixson.utils.LocationUtils.FaceToIxyz;
import static com.poixson.utils.LocationUtils.FaceToIxz;
import static com.poixson.utils.LocationUtils.FaceToNormAngle;
import static com.poixson.utils.LocationUtils.FaceToPillarAxChar;
import static com.poixson.utils.LocationUtils.FaceToRotation;
import static com.poixson.utils.LocationUtils.Rotate;
import static com.poixson.utils.LocationUtils.RotationToFace;
import static com.poixson.utils.LocationUtils.ValueToFaceQuarter;
import static com.poixson.utils.LocationUtils.YawToFace;
import static com.poixson.utils.LocationUtils.YawToRotation;
import static com.poixson.utils.LocationUtils.YawToRotation90;
import static com.poixson.utils.MathUtils.DELTA;

import org.bukkit.Axis;
import org.bukkit.Rotation;
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
		Assert.assertEquals(null,                      Rotate(new Iabcd(5, 10, 2, 3), BlockFace.UP   ));
		Assert.assertEquals(null,                      Rotate(new Iabcd(5, 10, 2, 3), BlockFace.DOWN ));
		// rotate BlockFace by double
		Assert.assertEquals(BlockFace.NORTH, Rotate(BlockFace.NORTH, 0.0 ));
		Assert.assertEquals(BlockFace.EAST,  Rotate(BlockFace.NORTH, 0.25));
		Assert.assertEquals(BlockFace.SOUTH, Rotate(BlockFace.NORTH, 0.5 ));
		Assert.assertEquals(BlockFace.WEST,  Rotate(BlockFace.NORTH, 0.75));
		Assert.assertEquals(BlockFace.NORTH, Rotate(BlockFace.NORTH, 1.0 ));
		Assert.assertEquals(BlockFace.WEST,  Rotate(BlockFace.SOUTH, 0.25));
		Assert.assertEquals(BlockFace.NORTH, Rotate(BlockFace.SOUTH, 0.5 ));
		Assert.assertEquals(BlockFace.NORTH, Rotate(BlockFace.NORTH, 5.9 ));
		Assert.assertEquals(BlockFace.NORTH, Rotate(BlockFace.NORTH,-5.1 ));
		// rotate ax by double
		Assert.assertEquals('n', Rotate('n', 0.0 ));
		Assert.assertEquals('e', Rotate('n', 0.25));
		Assert.assertEquals('s', Rotate('n', 0.5 ));
		Assert.assertEquals('w', Rotate('n', 0.75));
		Assert.assertEquals('n', Rotate('n', 1.0 ));
		Assert.assertEquals('w', Rotate('s', 0.25));
		Assert.assertEquals('n', Rotate('s', 0.5 ));
		Assert.assertEquals('e', Rotate('n', 5.1 ));
		Assert.assertEquals('n', Rotate('n',-5.1 ));
		// rotate axis by BlockFace
		Assert.assertEquals("udnsew", Rotate("udnsew", BlockFace.SOUTH));
		Assert.assertEquals("udewsn", Rotate("udnsew", BlockFace.WEST ));
		Assert.assertEquals("udsnwe", Rotate("udnsew", BlockFace.NORTH));
		Assert.assertEquals("udwens", Rotate("udnsew", BlockFace.EAST ));
		Assert.assertEquals("udnsew", Rotate("udnsew", BlockFace.UP   ));
		Assert.assertEquals("udnsew", Rotate("udnsew", BlockFace.DOWN ));
		// rotate ax by BlockFace
		Assert.assertEquals('n', Rotate('n', BlockFace.SOUTH));
		Assert.assertEquals('e', Rotate('n', BlockFace.WEST ));
		Assert.assertEquals('s', Rotate('n', BlockFace.NORTH));
		Assert.assertEquals('w', Rotate('n', BlockFace.EAST ));
		Assert.assertEquals('e', Rotate('s', BlockFace.EAST ));
		Assert.assertEquals('w', Rotate('s', BlockFace.WEST ));
		Assert.assertEquals('n', Rotate('n', BlockFace.UP   ));
		Assert.assertEquals('s', Rotate('s', BlockFace.DOWN ));
	}



	@Test
	public void testConversions() {
		// BlockFace to Normal Angle
		Assert.assertEquals(0.5,  FaceToNormAngle(BlockFace.NORTH), DELTA);
		Assert.assertEquals(0.0,  FaceToNormAngle(BlockFace.SOUTH), DELTA);
		Assert.assertEquals(0.75, FaceToNormAngle(BlockFace.EAST ), DELTA);
		Assert.assertEquals(0.25, FaceToNormAngle(BlockFace.WEST ), DELTA);
		Assert.assertEquals(0.0,  FaceToNormAngle(BlockFace.UP   ), DELTA);
		Assert.assertEquals(0.0,  FaceToNormAngle(BlockFace.DOWN ), DELTA);
		// Yaw to BlockFace
		Assert.assertEquals(BlockFace.SOUTH, YawToFace( -5.0f));
		Assert.assertEquals(BlockFace.SOUTH, YawToFace(  0.0f));
		Assert.assertEquals(BlockFace.SOUTH, YawToFace(  5.0f));
		Assert.assertEquals(BlockFace.WEST,  YawToFace( 85.0f));
		Assert.assertEquals(BlockFace.WEST,  YawToFace( 90.0f));
		Assert.assertEquals(BlockFace.WEST,  YawToFace( 95.0f));
		Assert.assertEquals(BlockFace.NORTH, YawToFace(175.0f));
		Assert.assertEquals(BlockFace.NORTH, YawToFace(180.0f));
		Assert.assertEquals(BlockFace.NORTH, YawToFace(185.0f));
		Assert.assertEquals(BlockFace.EAST,  YawToFace(265.0f));
		Assert.assertEquals(BlockFace.EAST,  YawToFace(270.0f));
		Assert.assertEquals(BlockFace.EAST,  YawToFace(275.0f));
		Assert.assertEquals(BlockFace.SOUTH, YawToFace(370.0f));
		Assert.assertEquals(BlockFace.EAST,  YawToFace( 1000.0f));
		Assert.assertEquals(BlockFace.WEST,  YawToFace(-1000.0f));
		// Yaw to Rotation
		Assert.assertEquals(Rotation.NONE,                 YawToRotation(  0.0f));
		Assert.assertEquals(Rotation.NONE,                 YawToRotation(  1.0f));
		Assert.assertEquals(Rotation.NONE,                 YawToRotation( -1.0f));
		Assert.assertEquals(Rotation.NONE,                 YawToRotation( 10.0f));
		Assert.assertEquals(Rotation.CLOCKWISE_45,         YawToRotation( 44.0f));
		Assert.assertEquals(Rotation.CLOCKWISE_45,         YawToRotation( 45.0f));
		Assert.assertEquals(Rotation.CLOCKWISE_45,         YawToRotation( 46.0f));
		Assert.assertEquals(Rotation.CLOCKWISE,            YawToRotation( 89.0f));
		Assert.assertEquals(Rotation.CLOCKWISE,            YawToRotation( 90.0f));
		Assert.assertEquals(Rotation.CLOCKWISE,            YawToRotation( 91.0f));
		Assert.assertEquals(Rotation.CLOCKWISE_135,        YawToRotation(135.0f));
		Assert.assertEquals(Rotation.FLIPPED,              YawToRotation(180.0f));
		Assert.assertEquals(Rotation.FLIPPED_45,           YawToRotation(225.0f));
		Assert.assertEquals(Rotation.COUNTER_CLOCKWISE,    YawToRotation(270.0f));
		Assert.assertEquals(Rotation.COUNTER_CLOCKWISE_45, YawToRotation(315.0f));
		Assert.assertEquals(Rotation.NONE,                 YawToRotation(359.0f));
		Assert.assertEquals(Rotation.COUNTER_CLOCKWISE,    YawToRotation( 1000.0f));
		Assert.assertEquals(Rotation.CLOCKWISE,            YawToRotation(-1000.0f));
		// Yaw to Rotation90
		Assert.assertEquals(Rotation.NONE,                 YawToRotation90(  0.0f));
		Assert.assertEquals(Rotation.NONE,                 YawToRotation90(  1.0f));
		Assert.assertEquals(Rotation.NONE,                 YawToRotation90( -1.0f));
		Assert.assertEquals(Rotation.NONE,                 YawToRotation90( 10.0f));
		Assert.assertEquals(Rotation.CLOCKWISE,            YawToRotation90( 44.0f));
		Assert.assertEquals(Rotation.CLOCKWISE,            YawToRotation90( 45.0f));
		Assert.assertEquals(Rotation.CLOCKWISE,            YawToRotation90( 46.0f));
		Assert.assertEquals(Rotation.CLOCKWISE,            YawToRotation90( 89.0f));
		Assert.assertEquals(Rotation.CLOCKWISE,            YawToRotation90( 90.0f));
		Assert.assertEquals(Rotation.CLOCKWISE,            YawToRotation90( 91.0f));
		Assert.assertEquals(Rotation.FLIPPED,              YawToRotation90(135.0f));
		Assert.assertEquals(Rotation.FLIPPED,              YawToRotation90(180.0f));
		Assert.assertEquals(Rotation.COUNTER_CLOCKWISE,    YawToRotation90(225.0f));
		Assert.assertEquals(Rotation.COUNTER_CLOCKWISE,    YawToRotation90(270.0f));
		Assert.assertEquals(Rotation.NONE,                 YawToRotation90(315.0f));
		Assert.assertEquals(Rotation.NONE,                 YawToRotation90(359.0f));
		Assert.assertEquals(Rotation.COUNTER_CLOCKWISE,    YawToRotation90( 1000.0f));
		Assert.assertEquals(Rotation.CLOCKWISE,            YawToRotation90(-1000.0f));
		// BlockFace to Rotation
		Assert.assertEquals(Rotation.FLIPPED,           FaceToRotation(BlockFace.NORTH));
		Assert.assertEquals(Rotation.NONE,              FaceToRotation(BlockFace.SOUTH));
		Assert.assertEquals(Rotation.COUNTER_CLOCKWISE, FaceToRotation(BlockFace.EAST ));
		Assert.assertEquals(Rotation.CLOCKWISE,         FaceToRotation(BlockFace.WEST ));
		Assert.assertEquals(null,                       FaceToRotation(BlockFace.UP   ));
		Assert.assertEquals(null,                       FaceToRotation(BlockFace.DOWN ));
		// Rotation to BlockFace
		Assert.assertEquals(BlockFace.NORTH, RotationToFace(Rotation.FLIPPED             ));
		Assert.assertEquals(BlockFace.SOUTH, RotationToFace(Rotation.NONE                ));
		Assert.assertEquals(BlockFace.EAST,  RotationToFace(Rotation.COUNTER_CLOCKWISE   ));
		Assert.assertEquals(BlockFace.WEST,  RotationToFace(Rotation.CLOCKWISE           ));
		Assert.assertEquals(null,            RotationToFace(Rotation.CLOCKWISE_45        ));
		Assert.assertEquals(null,            RotationToFace(Rotation.CLOCKWISE_135       ));
		Assert.assertEquals(null,            RotationToFace(Rotation.COUNTER_CLOCKWISE_45));
		// BlockFace to Axis
		Assert.assertEquals(Axis.X, FaceToAxis(BlockFace.EAST ));
		Assert.assertEquals(Axis.X, FaceToAxis(BlockFace.WEST ));
		Assert.assertEquals(Axis.Y, FaceToAxis(BlockFace.UP   ));
		Assert.assertEquals(Axis.Y, FaceToAxis(BlockFace.DOWN ));
		Assert.assertEquals(Axis.Z, FaceToAxis(BlockFace.NORTH));
		Assert.assertEquals(Axis.Z, FaceToAxis(BlockFace.SOUTH));
		Assert.assertEquals(null,   FaceToAxis(BlockFace.NORTH_EAST));
		// BlockFace to axis char
		Assert.assertEquals('n', FaceToAxChar(BlockFace.NORTH));
		Assert.assertEquals('s', FaceToAxChar(BlockFace.SOUTH));
		Assert.assertEquals('e', FaceToAxChar(BlockFace.EAST ));
		Assert.assertEquals('w', FaceToAxChar(BlockFace.WEST ));
		Assert.assertEquals('u', FaceToAxChar(BlockFace.UP   ));
		Assert.assertEquals('d', FaceToAxChar(BlockFace.DOWN ));
		Assert.assertEquals(0,   FaceToAxChar(BlockFace.NORTH_EAST));
		// BlockFace to ax string
		Assert.assertEquals("n",  FaceToAxString(BlockFace.NORTH));
		Assert.assertEquals("s",  FaceToAxString(BlockFace.SOUTH));
		Assert.assertEquals("e",  FaceToAxString(BlockFace.EAST ));
		Assert.assertEquals("w",  FaceToAxString(BlockFace.WEST ));
		Assert.assertEquals("u",  FaceToAxString(BlockFace.UP   ));
		Assert.assertEquals("d",  FaceToAxString(BlockFace.DOWN ));
		Assert.assertEquals("ne", FaceToAxString(BlockFace.NORTH_EAST));
		Assert.assertEquals("nw", FaceToAxString(BlockFace.NORTH_WEST));
		Assert.assertEquals("se", FaceToAxString(BlockFace.SOUTH_EAST));
		Assert.assertEquals("sw", FaceToAxString(BlockFace.SOUTH_WEST));
		// BlockFace to Pillar Axis
		Assert.assertEquals('z', FaceToPillarAxChar(BlockFace.NORTH));
		Assert.assertEquals('z', FaceToPillarAxChar(BlockFace.SOUTH));
		Assert.assertEquals('x', FaceToPillarAxChar(BlockFace.EAST ));
		Assert.assertEquals('x', FaceToPillarAxChar(BlockFace.WEST ));
		Assert.assertEquals('y', FaceToPillarAxChar(BlockFace.UP   ));
		Assert.assertEquals('y', FaceToPillarAxChar(BlockFace.DOWN ));
		Assert.assertEquals(0,   FaceToPillarAxChar(BlockFace.NORTH_EAST));
		// axis char to BlockFace
		Assert.assertEquals(BlockFace.NORTH, AxToFace('n'));
		Assert.assertEquals(BlockFace.NORTH, AxToFace('z'));
		Assert.assertEquals(BlockFace.SOUTH, AxToFace('s'));
		Assert.assertEquals(BlockFace.SOUTH, AxToFace('Z'));
		Assert.assertEquals(BlockFace.EAST,  AxToFace('e'));
		Assert.assertEquals(BlockFace.EAST,  AxToFace('X'));
		Assert.assertEquals(BlockFace.WEST,  AxToFace('w'));
		Assert.assertEquals(BlockFace.WEST,  AxToFace('x'));
		Assert.assertEquals(BlockFace.UP,    AxToFace('u'));
		Assert.assertEquals(BlockFace.DOWN,  AxToFace('d'));
		Assert.assertEquals(null,            AxToFace('a'));
		// axis to BlockFace rotated
		Assert.assertArrayEquals(null, AxisToFaces2x2(""    ));
		Assert.assertArrayEquals(null, AxisToFaces2x2("-"   ));
		Assert.assertArrayEquals(null, AxisToFaces2x2("none"));
		Assert.assertArrayEquals(null, AxisToFaces2x2("null"));
		Assert.assertArrayEquals(null, AxisToFaces2x2(null  ));
		Assert.assertArrayEquals(
			new BlockFace[] { BlockFace.NORTH, BlockFace.SOUTH, BlockFace.EAST, BlockFace.WEST },
			AxisToFaces2x2("nsew")
		);
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
		Assert.assertEquals(null,               FaceToIxyz(BlockFace.EAST_NORTH_EAST));
		// BlockFace to x,z
		Assert.assertEquals(new Iab( 0,-1), FaceToIxz(BlockFace.NORTH));
		Assert.assertEquals(new Iab( 0, 1), FaceToIxz(BlockFace.SOUTH));
		Assert.assertEquals(new Iab( 1, 0), FaceToIxz(BlockFace.EAST ));
		Assert.assertEquals(new Iab(-1, 0), FaceToIxz(BlockFace.WEST ));
		Assert.assertEquals(new Iab( 1,-1), FaceToIxz(BlockFace.NORTH_EAST));
		Assert.assertEquals(new Iab(-1,-1), FaceToIxz(BlockFace.NORTH_WEST));
		Assert.assertEquals(new Iab( 1, 1), FaceToIxz(BlockFace.SOUTH_EAST));
		Assert.assertEquals(new Iab(-1, 1), FaceToIxz(BlockFace.SOUTH_WEST));
		Assert.assertEquals(null,           FaceToIxz(BlockFace.EAST_NORTH_EAST));
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
		Assert.assertEquals(null,               AxToIxyz('a'));
		// axis char to x,z
		Assert.assertEquals(new Iab( 0,-1), AxToIxz('n'));
		Assert.assertEquals(new Iab( 0,-1), AxToIxz('z'));
		Assert.assertEquals(new Iab( 0, 1), AxToIxz('s'));
		Assert.assertEquals(new Iab( 0, 1), AxToIxz('Z'));
		Assert.assertEquals(new Iab( 1, 0), AxToIxz('e'));
		Assert.assertEquals(new Iab( 1, 0), AxToIxz('X'));
		Assert.assertEquals(new Iab(-1, 0), AxToIxz('w'));
		Assert.assertEquals(new Iab(-1, 0), AxToIxz('x'));
		Assert.assertEquals(null,           AxToIxz('a'));
		// axis to x, z
		Assert.assertEquals(new Iab( 1, -1), AxisToIxz("ne"));
		Assert.assertEquals(new Iab(-1, -1), AxisToIxz("nw"));
		Assert.assertEquals(new Iab(-1,  1), AxisToIxz("sw"));
		Assert.assertEquals(new Iab( 0,  1), AxisToIxz("s" ));
		// axis to x, y, z
		Assert.assertEquals(new Iabc( 1,  1, -1), AxisToIxyz("neu"));
		Assert.assertEquals(new Iabc(-1, -1, -1), AxisToIxyz("nwd"));
		Assert.assertEquals(new Iabc(-1,  1,  1), AxisToIxyz("swu"));
		Assert.assertEquals(new Iabc(-1, -1,  1), AxisToIxyz("swd"));
		Assert.assertEquals(new Iabc(-1,  0,  1), AxisToIxyz("sw" ));
		// x,z to BlockFace
		Assert.assertEquals(BlockFace.NORTH_EAST, ValueToFaceQuarter( 0,-1));
		Assert.assertEquals(BlockFace.NORTH_WEST, ValueToFaceQuarter(-1,-1));
		Assert.assertEquals(BlockFace.SOUTH_EAST, ValueToFaceQuarter( 0, 0));
		Assert.assertEquals(BlockFace.SOUTH_WEST, ValueToFaceQuarter(-1, 0));
	}



}

package com.poixson.utils;

import static com.poixson.tools.Assertions.AssertArrayEquals;
import static com.poixson.tools.Assertions.AssertEquals;
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
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import com.poixson.tools.Assertions;
import com.poixson.tools.dao.Iab;
import com.poixson.tools.dao.Iabc;
import com.poixson.tools.dao.Iabcd;


@ExtendWith(Assertions.class)
public class Test_LocationUtils {



	@Test
	public void testRotate() {
		// x, z, w, d
		AssertEquals(new Iabcd(-3, -7,  2, -3), Rotate(new Iabcd(5, 10, 2, 3), BlockFace.NORTH));
		AssertEquals(new Iabcd( 5, 10,  2,  3), Rotate(new Iabcd(5, 10, 2, 3), BlockFace.SOUTH));
		AssertEquals(new Iabcd(10,  5,  3,  2), Rotate(new Iabcd(5, 10, 2, 3), BlockFace.EAST ));
		AssertEquals(new Iabcd(-7, -3, -3,  2), Rotate(new Iabcd(5, 10, 2, 3), BlockFace.WEST ));
		AssertEquals(null,                      Rotate(new Iabcd(5, 10, 2, 3), BlockFace.UP   ));
		AssertEquals(null,                      Rotate(new Iabcd(5, 10, 2, 3), BlockFace.DOWN ));
		// rotate BlockFace by double
		AssertEquals(BlockFace.NORTH, Rotate(BlockFace.NORTH, 0.0 ));
		AssertEquals(BlockFace.EAST,  Rotate(BlockFace.NORTH, 0.25));
		AssertEquals(BlockFace.SOUTH, Rotate(BlockFace.NORTH, 0.5 ));
		AssertEquals(BlockFace.WEST,  Rotate(BlockFace.NORTH, 0.75));
		AssertEquals(BlockFace.NORTH, Rotate(BlockFace.NORTH, 1.0 ));
		AssertEquals(BlockFace.WEST,  Rotate(BlockFace.SOUTH, 0.25));
		AssertEquals(BlockFace.NORTH, Rotate(BlockFace.SOUTH, 0.5 ));
		AssertEquals(BlockFace.NORTH, Rotate(BlockFace.NORTH, 5.9 ));
		AssertEquals(BlockFace.NORTH, Rotate(BlockFace.NORTH,-5.1 ));
		// rotate ax by double
		AssertEquals('n', Rotate('n', 0.0 ));
		AssertEquals('e', Rotate('n', 0.25));
		AssertEquals('s', Rotate('n', 0.5 ));
		AssertEquals('w', Rotate('n', 0.75));
		AssertEquals('n', Rotate('n', 1.0 ));
		AssertEquals('w', Rotate('s', 0.25));
		AssertEquals('n', Rotate('s', 0.5 ));
		AssertEquals('e', Rotate('n', 5.1 ));
		AssertEquals('n', Rotate('n',-5.1 ));
		// rotate axis by BlockFace
		AssertEquals("udnsew", Rotate("udnsew", BlockFace.SOUTH));
		AssertEquals("udewsn", Rotate("udnsew", BlockFace.WEST ));
		AssertEquals("udsnwe", Rotate("udnsew", BlockFace.NORTH));
		AssertEquals("udwens", Rotate("udnsew", BlockFace.EAST ));
		AssertEquals("udnsew", Rotate("udnsew", BlockFace.UP   ));
		AssertEquals("udnsew", Rotate("udnsew", BlockFace.DOWN ));
		// rotate ax by BlockFace
		AssertEquals('n', Rotate('n', BlockFace.SOUTH));
		AssertEquals('e', Rotate('n', BlockFace.WEST ));
		AssertEquals('s', Rotate('n', BlockFace.NORTH));
		AssertEquals('w', Rotate('n', BlockFace.EAST ));
		AssertEquals('e', Rotate('s', BlockFace.EAST ));
		AssertEquals('w', Rotate('s', BlockFace.WEST ));
		AssertEquals('n', Rotate('n', BlockFace.UP   ));
		AssertEquals('s', Rotate('s', BlockFace.DOWN ));
	}



	@Test
	public void testConversions() {
		// BlockFace to Normal Angle
		AssertEquals(0.5,  FaceToNormAngle(BlockFace.NORTH), DELTA);
		AssertEquals(0.0,  FaceToNormAngle(BlockFace.SOUTH), DELTA);
		AssertEquals(0.75, FaceToNormAngle(BlockFace.EAST ), DELTA);
		AssertEquals(0.25, FaceToNormAngle(BlockFace.WEST ), DELTA);
		AssertEquals(0.0,  FaceToNormAngle(BlockFace.UP   ), DELTA);
		AssertEquals(0.0,  FaceToNormAngle(BlockFace.DOWN ), DELTA);
		// Yaw to BlockFace
		AssertEquals(BlockFace.SOUTH, YawToFace( -5.0f));
		AssertEquals(BlockFace.SOUTH, YawToFace(  0.0f));
		AssertEquals(BlockFace.SOUTH, YawToFace(  5.0f));
		AssertEquals(BlockFace.WEST,  YawToFace( 85.0f));
		AssertEquals(BlockFace.WEST,  YawToFace( 90.0f));
		AssertEquals(BlockFace.WEST,  YawToFace( 95.0f));
		AssertEquals(BlockFace.NORTH, YawToFace(175.0f));
		AssertEquals(BlockFace.NORTH, YawToFace(180.0f));
		AssertEquals(BlockFace.NORTH, YawToFace(185.0f));
		AssertEquals(BlockFace.EAST,  YawToFace(265.0f));
		AssertEquals(BlockFace.EAST,  YawToFace(270.0f));
		AssertEquals(BlockFace.EAST,  YawToFace(275.0f));
		AssertEquals(BlockFace.SOUTH, YawToFace(370.0f));
		AssertEquals(BlockFace.EAST,  YawToFace( 1000.0f));
		AssertEquals(BlockFace.WEST,  YawToFace(-1000.0f));
		// Yaw to Rotation
		AssertEquals(Rotation.NONE,                 YawToRotation(  0.0f));
		AssertEquals(Rotation.NONE,                 YawToRotation(  1.0f));
		AssertEquals(Rotation.NONE,                 YawToRotation( -1.0f));
		AssertEquals(Rotation.NONE,                 YawToRotation( 10.0f));
		AssertEquals(Rotation.CLOCKWISE_45,         YawToRotation( 44.0f));
		AssertEquals(Rotation.CLOCKWISE_45,         YawToRotation( 45.0f));
		AssertEquals(Rotation.CLOCKWISE_45,         YawToRotation( 46.0f));
		AssertEquals(Rotation.CLOCKWISE,            YawToRotation( 89.0f));
		AssertEquals(Rotation.CLOCKWISE,            YawToRotation( 90.0f));
		AssertEquals(Rotation.CLOCKWISE,            YawToRotation( 91.0f));
		AssertEquals(Rotation.CLOCKWISE_135,        YawToRotation(135.0f));
		AssertEquals(Rotation.FLIPPED,              YawToRotation(180.0f));
		AssertEquals(Rotation.FLIPPED_45,           YawToRotation(225.0f));
		AssertEquals(Rotation.COUNTER_CLOCKWISE,    YawToRotation(270.0f));
		AssertEquals(Rotation.COUNTER_CLOCKWISE_45, YawToRotation(315.0f));
		AssertEquals(Rotation.NONE,                 YawToRotation(359.0f));
		AssertEquals(Rotation.COUNTER_CLOCKWISE,    YawToRotation( 1000.0f));
		AssertEquals(Rotation.CLOCKWISE,            YawToRotation(-1000.0f));
		// Yaw to Rotation90
		AssertEquals(Rotation.NONE,                 YawToRotation90(  0.0f));
		AssertEquals(Rotation.NONE,                 YawToRotation90(  1.0f));
		AssertEquals(Rotation.NONE,                 YawToRotation90( -1.0f));
		AssertEquals(Rotation.NONE,                 YawToRotation90( 10.0f));
		AssertEquals(Rotation.CLOCKWISE,            YawToRotation90( 44.0f));
		AssertEquals(Rotation.CLOCKWISE,            YawToRotation90( 45.0f));
		AssertEquals(Rotation.CLOCKWISE,            YawToRotation90( 46.0f));
		AssertEquals(Rotation.CLOCKWISE,            YawToRotation90( 89.0f));
		AssertEquals(Rotation.CLOCKWISE,            YawToRotation90( 90.0f));
		AssertEquals(Rotation.CLOCKWISE,            YawToRotation90( 91.0f));
		AssertEquals(Rotation.FLIPPED,              YawToRotation90(135.0f));
		AssertEquals(Rotation.FLIPPED,              YawToRotation90(180.0f));
		AssertEquals(Rotation.COUNTER_CLOCKWISE,    YawToRotation90(225.0f));
		AssertEquals(Rotation.COUNTER_CLOCKWISE,    YawToRotation90(270.0f));
		AssertEquals(Rotation.NONE,                 YawToRotation90(315.0f));
		AssertEquals(Rotation.NONE,                 YawToRotation90(359.0f));
		AssertEquals(Rotation.COUNTER_CLOCKWISE,    YawToRotation90( 1000.0f));
		AssertEquals(Rotation.CLOCKWISE,            YawToRotation90(-1000.0f));
		// BlockFace to Rotation
		AssertEquals(Rotation.FLIPPED,           FaceToRotation(BlockFace.NORTH));
		AssertEquals(Rotation.NONE,              FaceToRotation(BlockFace.SOUTH));
		AssertEquals(Rotation.COUNTER_CLOCKWISE, FaceToRotation(BlockFace.EAST ));
		AssertEquals(Rotation.CLOCKWISE,         FaceToRotation(BlockFace.WEST ));
		AssertEquals(null,                       FaceToRotation(BlockFace.UP   ));
		AssertEquals(null,                       FaceToRotation(BlockFace.DOWN ));
		// Rotation to BlockFace
		AssertEquals(BlockFace.NORTH, RotationToFace(Rotation.FLIPPED             ));
		AssertEquals(BlockFace.SOUTH, RotationToFace(Rotation.NONE                ));
		AssertEquals(BlockFace.EAST,  RotationToFace(Rotation.COUNTER_CLOCKWISE   ));
		AssertEquals(BlockFace.WEST,  RotationToFace(Rotation.CLOCKWISE           ));
		AssertEquals(null,            RotationToFace(Rotation.CLOCKWISE_45        ));
		AssertEquals(null,            RotationToFace(Rotation.CLOCKWISE_135       ));
		AssertEquals(null,            RotationToFace(Rotation.COUNTER_CLOCKWISE_45));
		// BlockFace to Axis
		AssertEquals(Axis.X, FaceToAxis(BlockFace.EAST ));
		AssertEquals(Axis.X, FaceToAxis(BlockFace.WEST ));
		AssertEquals(Axis.Y, FaceToAxis(BlockFace.UP   ));
		AssertEquals(Axis.Y, FaceToAxis(BlockFace.DOWN ));
		AssertEquals(Axis.Z, FaceToAxis(BlockFace.NORTH));
		AssertEquals(Axis.Z, FaceToAxis(BlockFace.SOUTH));
		AssertEquals(null,   FaceToAxis(BlockFace.NORTH_EAST));
		// BlockFace to axis char
		AssertEquals('n', FaceToAxChar(BlockFace.NORTH));
		AssertEquals('s', FaceToAxChar(BlockFace.SOUTH));
		AssertEquals('e', FaceToAxChar(BlockFace.EAST ));
		AssertEquals('w', FaceToAxChar(BlockFace.WEST ));
		AssertEquals('u', FaceToAxChar(BlockFace.UP   ));
		AssertEquals('d', FaceToAxChar(BlockFace.DOWN ));
		AssertEquals(0,   FaceToAxChar(BlockFace.NORTH_EAST));
		// BlockFace to ax string
		AssertEquals("n",  FaceToAxString(BlockFace.NORTH));
		AssertEquals("s",  FaceToAxString(BlockFace.SOUTH));
		AssertEquals("e",  FaceToAxString(BlockFace.EAST ));
		AssertEquals("w",  FaceToAxString(BlockFace.WEST ));
		AssertEquals("u",  FaceToAxString(BlockFace.UP   ));
		AssertEquals("d",  FaceToAxString(BlockFace.DOWN ));
		AssertEquals("ne", FaceToAxString(BlockFace.NORTH_EAST));
		AssertEquals("nw", FaceToAxString(BlockFace.NORTH_WEST));
		AssertEquals("se", FaceToAxString(BlockFace.SOUTH_EAST));
		AssertEquals("sw", FaceToAxString(BlockFace.SOUTH_WEST));
		// BlockFace to Pillar Axis
		AssertEquals('z', FaceToPillarAxChar(BlockFace.NORTH));
		AssertEquals('z', FaceToPillarAxChar(BlockFace.SOUTH));
		AssertEquals('x', FaceToPillarAxChar(BlockFace.EAST ));
		AssertEquals('x', FaceToPillarAxChar(BlockFace.WEST ));
		AssertEquals('y', FaceToPillarAxChar(BlockFace.UP   ));
		AssertEquals('y', FaceToPillarAxChar(BlockFace.DOWN ));
		AssertEquals(0,   FaceToPillarAxChar(BlockFace.NORTH_EAST));
		// axis char to BlockFace
		AssertEquals(BlockFace.NORTH, AxToFace('n'));
		AssertEquals(BlockFace.NORTH, AxToFace('z'));
		AssertEquals(BlockFace.SOUTH, AxToFace('s'));
		AssertEquals(BlockFace.SOUTH, AxToFace('Z'));
		AssertEquals(BlockFace.EAST,  AxToFace('e'));
		AssertEquals(BlockFace.EAST,  AxToFace('X'));
		AssertEquals(BlockFace.WEST,  AxToFace('w'));
		AssertEquals(BlockFace.WEST,  AxToFace('x'));
		AssertEquals(BlockFace.UP,    AxToFace('u'));
		AssertEquals(BlockFace.DOWN,  AxToFace('d'));
		AssertEquals(null,            AxToFace('a'));
		// axis to BlockFace rotated
		AssertArrayEquals(null, AxisToFaces2x2(""    ));
		AssertArrayEquals(null, AxisToFaces2x2("-"   ));
		AssertArrayEquals(null, AxisToFaces2x2("none"));
		AssertArrayEquals(null, AxisToFaces2x2("null"));
		AssertArrayEquals(null, AxisToFaces2x2(null  ));
		AssertArrayEquals(
			new BlockFace[] { BlockFace.NORTH, BlockFace.SOUTH, BlockFace.EAST, BlockFace.WEST },
			AxisToFaces2x2("nsew")
		);
		// BlockFace to x,y,z
		AssertEquals(new Iabc( 0, 1, 0), FaceToIxyz(BlockFace.UP   ));
		AssertEquals(new Iabc( 0,-1, 0), FaceToIxyz(BlockFace.DOWN ));
		AssertEquals(new Iabc( 0, 0,-1), FaceToIxyz(BlockFace.NORTH));
		AssertEquals(new Iabc( 0, 0, 1), FaceToIxyz(BlockFace.SOUTH));
		AssertEquals(new Iabc( 1, 0, 0), FaceToIxyz(BlockFace.EAST ));
		AssertEquals(new Iabc(-1, 0, 0), FaceToIxyz(BlockFace.WEST ));
		AssertEquals(new Iabc( 1, 0,-1), FaceToIxyz(BlockFace.NORTH_EAST));
		AssertEquals(new Iabc(-1, 0,-1), FaceToIxyz(BlockFace.NORTH_WEST));
		AssertEquals(new Iabc( 1, 0, 1), FaceToIxyz(BlockFace.SOUTH_EAST));
		AssertEquals(new Iabc(-1, 0, 1), FaceToIxyz(BlockFace.SOUTH_WEST));
		AssertEquals(null,               FaceToIxyz(BlockFace.EAST_NORTH_EAST));
		// BlockFace to x,z
		AssertEquals(new Iab( 0,-1), FaceToIxz(BlockFace.NORTH));
		AssertEquals(new Iab( 0, 1), FaceToIxz(BlockFace.SOUTH));
		AssertEquals(new Iab( 1, 0), FaceToIxz(BlockFace.EAST ));
		AssertEquals(new Iab(-1, 0), FaceToIxz(BlockFace.WEST ));
		AssertEquals(new Iab( 1,-1), FaceToIxz(BlockFace.NORTH_EAST));
		AssertEquals(new Iab(-1,-1), FaceToIxz(BlockFace.NORTH_WEST));
		AssertEquals(new Iab( 1, 1), FaceToIxz(BlockFace.SOUTH_EAST));
		AssertEquals(new Iab(-1, 1), FaceToIxz(BlockFace.SOUTH_WEST));
		AssertEquals(null,           FaceToIxz(BlockFace.EAST_NORTH_EAST));
		// axis char to x,y,z
		AssertEquals(new Iabc( 0, 0,-1), AxToIxyz('n'));
		AssertEquals(new Iabc( 0, 0,-1), AxToIxyz('z'));
		AssertEquals(new Iabc( 0, 0, 1), AxToIxyz('s'));
		AssertEquals(new Iabc( 0, 0, 1), AxToIxyz('Z'));
		AssertEquals(new Iabc( 1, 0, 0), AxToIxyz('e'));
		AssertEquals(new Iabc( 1, 0, 0), AxToIxyz('X'));
		AssertEquals(new Iabc(-1, 0, 0), AxToIxyz('w'));
		AssertEquals(new Iabc(-1, 0, 0), AxToIxyz('x'));
		AssertEquals(new Iabc( 0, 1, 0), AxToIxyz('u'));
		AssertEquals(new Iabc( 0, 1, 0), AxToIxyz('Y'));
		AssertEquals(new Iabc( 0,-1, 0), AxToIxyz('d'));
		AssertEquals(new Iabc( 0,-1, 0), AxToIxyz('y'));
		AssertEquals(null,               AxToIxyz('a'));
		// axis char to x,z
		AssertEquals(new Iab( 0,-1), AxToIxz('n'));
		AssertEquals(new Iab( 0,-1), AxToIxz('z'));
		AssertEquals(new Iab( 0, 1), AxToIxz('s'));
		AssertEquals(new Iab( 0, 1), AxToIxz('Z'));
		AssertEquals(new Iab( 1, 0), AxToIxz('e'));
		AssertEquals(new Iab( 1, 0), AxToIxz('X'));
		AssertEquals(new Iab(-1, 0), AxToIxz('w'));
		AssertEquals(new Iab(-1, 0), AxToIxz('x'));
		AssertEquals(null,           AxToIxz('a'));
		// axis to x, z
		AssertEquals(new Iab( 1, -1), AxisToIxz("ne"));
		AssertEquals(new Iab(-1, -1), AxisToIxz("nw"));
		AssertEquals(new Iab(-1,  1), AxisToIxz("sw"));
		AssertEquals(new Iab( 0,  1), AxisToIxz("s" ));
		// axis to x, y, z
		AssertEquals(new Iabc( 1,  1, -1), AxisToIxyz("neu"));
		AssertEquals(new Iabc(-1, -1, -1), AxisToIxyz("nwd"));
		AssertEquals(new Iabc(-1,  1,  1), AxisToIxyz("swu"));
		AssertEquals(new Iabc(-1, -1,  1), AxisToIxyz("swd"));
		AssertEquals(new Iabc(-1,  0,  1), AxisToIxyz("sw" ));
		// x,z to BlockFace
		AssertEquals(BlockFace.NORTH_EAST, ValueToFaceQuarter( 0,-1));
		AssertEquals(BlockFace.NORTH_WEST, ValueToFaceQuarter(-1,-1));
		AssertEquals(BlockFace.SOUTH_EAST, ValueToFaceQuarter( 0, 0));
		AssertEquals(BlockFace.SOUTH_WEST, ValueToFaceQuarter(-1, 0));
	}



}

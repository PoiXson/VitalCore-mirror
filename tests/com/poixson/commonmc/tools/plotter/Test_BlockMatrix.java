package com.poixson.commonmc.tools.plotter;

import org.junit.Assert;
import org.junit.Test;


public class Test_BlockMatrix {



	@Test
	public void testBlockMatrix1D() {
		{
			final BlockMatrix matrix = new BlockMatrix(3);
			matrix.fill('x');
			Assert.assertNull(matrix.array);
			Assert.assertEquals(3, matrix.size);
			matrix.append('a');
			Assert.assertEquals("xxxa", matrix.row.toString());
		}
		{
			final BlockMatrix matrix = new BlockMatrix(3);
			matrix.append("abc");
			Assert.assertNull(matrix.array);
			Assert.assertEquals(3, matrix.size);
			Assert.assertEquals("abc", matrix.getRow());
		}
	}

	@Test
	public void testBlockMatrix2D() {
		{
			final BlockMatrix matrix = new BlockMatrix(2, 3);
			matrix.fill('x');
			Assert.assertNull(matrix.row);
			Assert.assertEquals(2, matrix.size);
			Assert.assertEquals(3, matrix.array[0].size);
			Assert.assertEquals(3, matrix.array[1].size);
			matrix.append('a', 0);
			matrix.append("b", 1);
			Assert.assertEquals("xxxa", matrix.getRow(0));
			Assert.assertEquals("xxxb", matrix.getRow(1));
		}
		{
			final BlockMatrix matrix = new BlockMatrix(2, 3);
			Assert.assertNull(matrix.row);
			Assert.assertEquals(2, matrix.size);
			Assert.assertEquals(3, matrix.array[0].size);
			Assert.assertEquals(3, matrix.array[1].size);
			matrix.append("abc", 0);
			matrix.append("def", 1);
			Assert.assertEquals("abc", matrix.getRow(0));
			Assert.assertEquals("def", matrix.getRow(1));
		}
	}

	@Test
	public void testBlockMatrix3D() {
		{
			final BlockMatrix matrix = new BlockMatrix(3, 4, 5);
			matrix.fill('x');
			Assert.assertNull(matrix.row);
			Assert.assertEquals(3, matrix.size);
			Assert.assertEquals(3, matrix.array.length);
			Assert.assertEquals(4, matrix.array[0].array.length);
			Assert.assertNull(matrix.array[0].array[0].array);
			Assert.assertEquals("xxxxx", matrix.array[0].array[0].row.toString());
			Assert.assertEquals(4, matrix.array[0].size);
			Assert.assertEquals(4, matrix.array[1].size);
			Assert.assertEquals(4, matrix.array[2].size);
			Assert.assertEquals(5, matrix.array[2].array[0].size);
			Assert.assertEquals(5, matrix.array[2].array[3].size);
			matrix.append('a', 0, 0);
			matrix.append("b", 1, 1);
			matrix.append("c", 2, 2);
			Assert.assertEquals("xxxxxa", matrix.getRow(0, 0));
			Assert.assertEquals("xxxxxb", matrix.getRow(1, 1));
			Assert.assertEquals("xxxxxc", matrix.getRow(2, 2));
		}
		{
			final BlockMatrix matrix = new BlockMatrix(1, 2, 3);
			Assert.assertNull(matrix.row);
			Assert.assertEquals(1, matrix.size);
			Assert.assertEquals(2, matrix.array[0].size);
			Assert.assertEquals(3, matrix.array[0].array[0].size);
			Assert.assertEquals(3, matrix.array[0].array[1].size);
			matrix.append("abc", 0, 0);
			matrix.append("def", 0, 1);
			Assert.assertEquals("abc", matrix.getRow(0, 0));
			Assert.assertEquals("def", matrix.getRow(0, 1));
		}
	}



}

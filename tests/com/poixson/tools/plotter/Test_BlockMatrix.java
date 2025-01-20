/*
package com.poixson.tools.plotter;

import static com.poixson.tools.Assertions.AssertEquals;
import static com.poixson.tools.Assertions.AssertNull;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import com.poixson.tools.Assertions;


@ExtendWith(Assertions.class)
public class Test_BlockMatrix {



	@Test
	public void testBlockMatrix1D() {
		{
			final BlockMatrix matrix = new BlockMatrix("u", new int[] {0}, new int[] {3});
			AssertEquals(1, matrix.getDimensions());
			matrix.fill('x');
			AssertNull(matrix.array);
			AssertEquals(3, matrix.size);
			matrix.append('a');
			AssertEquals("xxxa", matrix.row.toString());
		}
		{
			final BlockMatrix matrix = new BlockMatrix("u", new int[] {0}, new int[] {3});
			AssertEquals(1, matrix.getDimensions());
			matrix.append("abc");
			AssertNull(matrix.array);
			AssertEquals(3, matrix.size);
			AssertEquals("abc", matrix.getRow());
		}
	}

	@Test
	public void testBlockMatrix2D() {
		{
			final BlockMatrix matrix = new BlockMatrix("se", new int[] {0, 0}, new int[] {2, 3});
			AssertEquals(2, matrix.getDimensions());
			matrix.fill('x');
			AssertNull(matrix.row);
			AssertEquals(2, matrix.size);
			AssertEquals(3, matrix.array[0].size);
			AssertEquals(3, matrix.array[1].size);
			matrix.append('a', 0);
			matrix.append("b", 1);
			AssertEquals("xxxa", matrix.getRow(0));
			AssertEquals("xxxb", matrix.getRow(1));
		}
		{
			final BlockMatrix matrix = new BlockMatrix("se", new int[] {0, 0}, new int[] {2, 3});
			AssertEquals(2, matrix.getDimensions());
			AssertNull(matrix.row);
			AssertEquals(2, matrix.size);
			AssertEquals(3, matrix.array[0].size);
			AssertEquals(3, matrix.array[1].size);
			matrix.append("abc", 0);
			matrix.append("def", 1);
			AssertEquals("abc", matrix.getRow(0));
			AssertEquals("def", matrix.getRow(1));
		}
	}

	@Test
	public void testBlockMatrix3D() {
		{
			final BlockMatrix matrix = new BlockMatrix("use", new int[] {0, 0, 0}, new int[] {3, 4, 5});
			AssertEquals(3, matrix.getDimensions());
			matrix.fill('x');
			AssertNull(matrix.row);
			AssertEquals(3, matrix.size);
			AssertEquals(3, matrix.array.length);
			AssertEquals(4, matrix.array[0].array.length);
			AssertNull(matrix.array[0].array[0].array);
			AssertEquals("xxxxx", matrix.array[0].array[0].row.toString());
			AssertEquals(4, matrix.array[0].size);
			AssertEquals(4, matrix.array[1].size);
			AssertEquals(4, matrix.array[2].size);
			AssertEquals(5, matrix.array[2].array[0].size);
			AssertEquals(5, matrix.array[2].array[3].size);
			matrix.append('a', 0, 0);
			matrix.append("b", 1, 1);
			matrix.append("c", 2, 2);
			AssertEquals("xxxxxa", matrix.getRow(0, 0));
			AssertEquals("xxxxxb", matrix.getRow(1, 1));
			AssertEquals("xxxxxc", matrix.getRow(2, 2));
		}
		{
			final BlockMatrix matrix = new BlockMatrix("use", new int[] {0, 0, 0}, new int[] {1, 2, 3});
			AssertEquals(3, matrix.getDimensions());
			AssertNull(matrix.row);
			AssertEquals(1, matrix.size);
			AssertEquals(2, matrix.array[0].size);
			AssertEquals(3, matrix.array[0].array[0].size);
			AssertEquals(3, matrix.array[0].array[1].size);
			matrix.append("abc", 0, 0);
			matrix.append("def", 0, 1);
			AssertEquals("abc", matrix.getRow(0, 0));
			AssertEquals("def", matrix.getRow(0, 1));
		}
	}



}
*/

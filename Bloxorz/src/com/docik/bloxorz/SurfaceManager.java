package com.docik.bloxorz;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.view.KeyEvent;
import android.view.SurfaceHolder;

public class SurfaceManager extends Thread {

	private final int DOWN = 5;
	private final int SINGLE = 0;
	private final int DOUBLE = 1;
	
	private SurfaceHolder surfaceHolder;
	
	/**
	 * Variable for thread control. True - running, false - stop.
	 */
	private boolean threadState;
	
	/**
	 * Game resources
	 */
	private Resources res;
	
	/**
	 * Array of cell types
	 */
	private int[][][] map = null;
	
	/**
	 * Array of needed cells
	 */
	private Cell[][] cells = new Cell[15][10];
	
	/**
	 * Block
	 */
	private Block block;
	
	/**
	 * Level
	 */
	int lvl;
	
	/**
	 * Background image
	 */
	Drawable background;
	
	
	public SurfaceManager(SurfaceHolder surfaceHolder, Context context, int lvl)
	{
		this.surfaceHolder = surfaceHolder;
		this.lvl = lvl;
		threadState = false;
		
		res = context.getResources();
		
		background = res.getDrawable(R.drawable.background);
		background.setBounds(0, 0, 540, 320);
		
		initAll(lvl);
	}
	
	public void setThreadState(boolean state)
	{
		threadState = state;
	}
	
	public void run()
	{
		while(threadState){
			Canvas canvas = null;
			try
			{
				canvas = surfaceHolder.lockCanvas();
				
				synchronized (surfaceHolder)
				{
					if(block.getState()==SINGLE)
					{
						if(!block.levelCompleted())
						{
							if(block.getReset())
							{
								block.setReset(false);
								initAll(lvl);
							}
							else
							{
								refreshMap();
								refreshBlock();
							}
							
							background.draw(canvas);
							
							drawMap(canvas);
							if(block.getDirection()!=DOWN)
								block.draw(canvas);
							
							sleep(20);
						}else
						{
							lvl++;
							//sleep(1000);
							initAll(lvl);
						}
					}else
					{
						if(block.getReset())
						{
							block.setReset(false);
							initAll(lvl);
						}
						else
						{
							refreshMap();
							refreshSmallBlock();
						}
						
						background.draw(canvas);
						drawMap(canvas);
						block.drawSmallBlocks(canvas);
						
						sleep(20);
					}
				}
			}
			catch (Exception e) { }
			finally
			{
				if (canvas!=null)
				{
					surfaceHolder.unlockCanvasAndPost(canvas);
				}
			}
		}
	}

	public void initAll(int lvl)
	{
		map = setMap(lvl);
		
		int iStart = 0;
		int jStart = 0;
		for (int i=0; i<15; i++)
		{
			for (int j=0; j<10; j++)
			{
				if (map[i][j][0]>0)
				{
					if(map[i][j][0]!=6)
					{
						Cell cell = new Cell(res, map, i, j);
						cells[i][j] = cell;
					}else
					{
						Cell cell = new Cell(res, map, i, j);
						cells[i][j] = cell;
						iStart = i;
						jStart = j;
					}
				}else
				{
					cells[i][j] = null;
				}
			}
		}
		block = new Block(res, iStart, jStart, cells);
	}
	
	private void refreshBlock()
	{
		block.refresh();
	}
	
	private void refreshSmallBlock()
	{
		block.refreshSmallBlock();
	}
	
	public void refreshMap()	
	{
		for (int i=0; i<15; i++)
		{
			for (int j=0; j<10; j++)
			{
				if (map[i][j][0]==4||map[i][j][0]==8)
				{
					cells[i][j].refresh();
				}
			}
		}
	}
	
	public void drawMap(Canvas canvas)
	{
		for(int i=0; i<15; i++)
		{
			for(int j=0; j<10; j++)
			{
				if(cells[i][j]!=null)
					cells[i][j].draw(canvas);
				if(block.getState()==SINGLE)
				{
					if(block.getDirection()==DOWN && block.getI()==i && block.getJ()==j)
						block.draw(canvas);
				}
			}
		}
	}
	
	boolean keyUp(int keyCode)
	{
		if(!block.isLocked())
		{
			switch(keyCode)
			{
				case KeyEvent.KEYCODE_DPAD_LEFT:
					block.lockKeypad();
					block.moveSouth();
					return true;
				case KeyEvent.KEYCODE_DPAD_RIGHT:
					block.lockKeypad();
					block.moveNorth();
					return true;
				case KeyEvent.KEYCODE_DPAD_UP:
					block.lockKeypad();
					block.moveWest();
					return true;
				case KeyEvent.KEYCODE_DPAD_DOWN:
					block.lockKeypad();
					block.moveEast();
					return true;
				case KeyEvent.KEYCODE_DPAD_CENTER:
					block.lockKeypad();
					block.changeActiveBlock();
					return true;
				default: 
					return false;
			}
		}else
			return false;
	}
	
	/**
	 * Returns array of cell types
	 * @param lvl - current level
	 * @return array of cell types
	 */
	private int[][][] setMap(int lvl)
	{
		switch (lvl)
		{
			case 0:	//Template
			{
				map = new int[][][] {{{0}, {0}, {0}, {0}, {0}, {0}, {0}, {0}, {0}, {0}}, {{0}, {0}, {0}, {0}, {0}, {0}, {0}, {0}, {0}, {0}}, {{0}, {0}, {0}, {0}, {0}, {0}, {0}, {0}, {0}, {0}}, {{0}, {0}, {0}, {0}, {0}, {0}, {0}, {0}, {0}, {0}}, {{0}, {0}, {0}, {0}, {0}, {0}, {0}, {0}, {0}, {0}}, {{0}, {0}, {0}, {0}, {0}, {0}, {0}, {0}, {0}, {0}}, {{0}, {0}, {0}, {0}, {0}, {0}, {0}, {0}, {0}, {0}}, {{0}, {0}, {0}, {0}, {0}, {0}, {0}, {0}, {0}, {0}}, {{0}, {0}, {0}, {0}, {0}, {0}, {0}, {0}, {0}, {0}}, {{0}, {0}, {0}, {0}, {0}, {0}, {0}, {0}, {0}, {0}}, {{0}, {0}, {0}, {0}, {0}, {0}, {0}, {0}, {0}, {0}}, {{0}, {0}, {0}, {0}, {0}, {0}, {0}, {0}, {0}, {0}}, {{0}, {0}, {0}, {0}, {0}, {0}, {0}, {0}, {0}, {0}}, {{0}, {0}, {0}, {0}, {0}, {0}, {0}, {0}, {0}, {0}}, {{0}, {0}, {0}, {0}, {0}, {0}, {0}, {0}, {0}, {0}}};
				break;
			}
			case 1: 
			{
				map = new int[][][] {{{0}, {0}, {0}, {0}, {0}, {0}, {0}, {0}, {0}, {0}}, {{0}, {0}, {0}, {0}, {0}, {0}, {0}, {0}, {0}, {0}}, {{0}, {0}, {0}, {0}, {0}, {0}, {0}, {0}, {0}, {0}}, {{0}, {0}, {0}, {0}, {0}, {1}, {1}, {0}, {0}, {0}}, {{0}, {0}, {0}, {0}, {1}, {1}, {1}, {1}, {0}, {0}}, {{0}, {0}, {0}, {0}, {1}, {1}, {7}, {1}, {0}, {0}}, {{0}, {0}, {0}, {0}, {1}, {1}, {1}, {1}, {0}, {0}}, {{0}, {0}, {0}, {1}, {1}, {1}, {1}, {0}, {0}, {0}}, {{0}, {0}, {0}, {1}, {1}, {1}, {0}, {0}, {0}, {0}}, {{0}, {0}, {0}, {1}, {1}, {1}, {0}, {0}, {0}, {0}}, {{0}, {0}, {1}, {1}, {1}, {1}, {0}, {0}, {0}, {0}}, {{0}, {0}, {1}, {6}, {1}, {1}, {0}, {0}, {0}, {0}}, {{0}, {0}, {1}, {1}, {1}, {0}, {0}, {0}, {0}, {0}}, {{0}, {0}, {0}, {0}, {0}, {0}, {0}, {0}, {0}, {0}}, {{0}, {0}, {0}, {0}, {0}, {0}, {0}, {0}, {0}, {0}}};
				break;
			}
			case 2:
			{
				map = new int[][][] {{{0}, {0}, {1}, {1}, {1}, {1}, {1}, {0}, {0}, {0}}, {{0}, {0}, {1}, {7}, {1}, {1}, {1}, {0}, {0}, {0}}, {{0}, {0}, {1}, {1}, {1}, {1}, {1}, {0}, {0}, {0}}, {{0}, {0}, {0}, {0}, {0}, {0}, {4, 0, 1}, {0}, {0}, {0}}, {{0}, {0}, {0}, {0}, {0}, {0}, {4, 0, 0}, {0}, {0}, {0}}, {{0}, {0}, {1}, {1}, {1}, {1}, {1}, {1}, {0}, {0}}, {{0}, {0}, {1}, {3, 2, 3, 6, 2, 4, 6}, {1}, {1}, {1}, {1}, {0}, {0}}, {{0}, {0}, {1}, {1}, {1}, {1}, {1}, {1}, {0}, {0}}, {{0}, {0}, {1}, {1}, {1}, {1}, {1}, {1}, {0}, {0}}, {{0}, {0}, {0}, {0}, {0}, {0}, {4, 0, 1}, {0}, {0}, {0}}, {{0}, {0}, {0}, {0}, {0}, {0}, {4, 0, 0}, {0}, {0}, {0}}, {{0}, {0}, {0}, {1}, {1}, {1}, {1}, {1}, {0}, {0}}, {{0}, {0}, {0}, {1}, {2, 2, 9, 6, 2, 10, 6}, {1}, {1}, {1}, {0}, {0}}, {{0}, {0}, {0}, {1}, {1}, {1}, {6}, {1}, {0}, {0}}, {{0}, {0}, {0}, {1}, {1}, {1}, {1}, {1}, {0}, {0}}};
				break;
			}
			case 3:
			{
				map = new int[][][] {{{0}, {0}, {0}, {0}, {1}, {1}, {1}, {1}, {0}, {0}}, {{0}, {0}, {0}, {0}, {1}, {7}, {1}, {1}, {0}, {0}}, {{0}, {0}, {1}, {1}, {1}, {1}, {1}, {1}, {0}, {0}}, {{0}, {0}, {1}, {1}, {1}, {1}, {1}, {0}, {0}, {0}}, {{0}, {0}, {1}, {0}, {0}, {0}, {0}, {0}, {0}, {0}}, {{0}, {0}, {1}, {0}, {0}, {0}, {0}, {0}, {0}, {0}}, {{0}, {0}, {1}, {1}, {1}, {0}, {0}, {0}, {0}, {0}}, {{0}, {0}, {1}, {1}, {1}, {0}, {0}, {0}, {0}, {0}}, {{0}, {0}, {1}, {1}, {1}, {0}, {0}, {0}, {0}, {0}}, {{0}, {0}, {0}, {0}, {1}, {0}, {0}, {0}, {0}, {0}}, {{0}, {0}, {0}, {0}, {1}, {0}, {0}, {0}, {0}, {0}}, {{0}, {0}, {0}, {1}, {1}, {1}, {1}, {0}, {0}, {0}}, {{0}, {0}, {0}, {1}, {1}, {1}, {1}, {0}, {0}, {0}}, {{0}, {0}, {0}, {1}, {1}, {6}, {1}, {0}, {0}, {0}}, {{0}, {0}, {0}, {1}, {1}, {1}, {1}, {0}, {0}, {0}}};
				break;
			}
			case 4:
			{
				map = new int[][][] {{{0}, {0}, {0}, {0}, {0}, {0}, {0}, {0}, {0}, {0}}, {{0}, {0}, {0}, {0}, {0}, {0}, {8}, {8}, {8}, {8}}, {{0}, {0}, {0}, {0}, {0}, {0}, {8}, {8}, {1}, {8}}, {{0}, {0}, {0}, {1}, {1}, {1}, {8}, {8}, {8}, {8}}, {{0}, {0}, {0}, {1}, {1}, {1}, {8}, {8}, {8}, {8}}, {{0}, {8}, {8}, {1}, {0}, {0}, {8}, {8}, {0}, {0}}, {{0}, {8}, {8}, {0}, {0}, {0}, {1}, {1}, {0}, {0}}, {{0}, {8}, {8}, {0}, {0}, {0}, {1}, {1}, {1}, {1}}, {{0}, {8}, {8}, {0}, {0}, {0}, {1}, {1}, {7}, {1}}, {{0}, {8}, {8}, {0}, {0}, {0}, {1}, {1}, {1}, {1}}, {{0}, {8}, {8}, {0}, {0}, {0}, {0}, {0}, {0}, {0}}, {{0}, {8}, {8}, {1}, {0}, {0}, {0}, {0}, {0}, {0}}, {{0}, {0}, {0}, {1}, {1}, {1}, {1}, {1}, {0}, {0}}, {{0}, {0}, {0}, {1}, {1}, {1}, {6}, {1}, {0}, {0}}, {{0}, {0}, {0}, {1}, {1}, {1}, {1}, {1}, {0}, {0}}};
				break;
			}
			case 5:		//lvl 9
			{
				map = new int[][][] {{{0}, {0}, {0}, {1}, {1}, {1}, {0}, {0}, {0}, {0}}, {{0}, {0}, {0}, {1}, {6}, {1}, {0}, {0}, {0}, {0}}, {{0}, {0}, {0}, {1}, {1}, {1}, {0}, {0}, {0}, {0}}, {{0}, {0}, {0}, {1}, {1}, {1}, {0}, {0}, {0}, {0}}, {{0}, {0}, {0}, {0}, {0}, {1}, {0}, {0}, {0}, {0}}, {{0}, {0}, {0}, {0}, {0}, {1}, {0}, {0}, {0}, {0}}, {{0}, {0}, {0}, {0}, {0}, {1}, {1}, {1}, {0}, {0}}, {{0}, {0}, {0}, {1}, {1}, {1}, {7}, {1}, {0}, {0}}, {{0}, {0}, {0}, {0}, {0}, {1}, {1}, {1}, {0}, {0}}, {{0}, {0}, {0}, {0}, {0}, {1}, {0}, {0}, {0}, {0}}, {{0}, {0}, {0}, {0}, {0}, {1}, {0}, {0}, {0}, {0}}, {{0}, {0}, {0}, {1}, {1}, {1}, {0}, {0}, {0}, {0}}, {{0}, {0}, {0}, {1}, {1}, {1}, {0}, {0}, {0}, {0}}, {{0}, {0}, {0}, {1}, {5, 2, 4, 12, 4}, {1}, {0}, {0}, {0}, {0}}, {{0}, {0}, {0}, {1}, {1}, {1}, {0}, {0}, {0}, {0}}};
				break;
			}
			case 6:		//lvl 13
			{
				map = new int[][][] {{{0}, {0}, {0}, {0}, {0}, {0}, {0}, {0}, {0}, {0}}, {{0}, {0}, {1}, {1}, {1}, {0}, {0}, {0}, {0}, {0}}, {{1}, {1}, {1}, {6}, {1}, {0}, {0}, {0}, {0}, {0}}, {{1}, {1}, {1}, {1}, {1}, {1}, {1}, {0}, {0}, {0}}, {{1}, {1}, {0}, {0}, {0}, {0}, {1}, {8}, {8}, {0}}, {{1}, {0}, {0}, {0}, {0}, {0}, {8}, {8}, {8}, {1}}, {{8}, {0}, {0}, {1}, {1}, {1}, {8}, {8}, {8}, {1}}, {{1}, {0}, {0}, {1}, {7}, {1}, {8}, {1}, {8}, {0}}, {{1}, {0}, {0}, {1}, {1}, {1}, {8}, {8}, {8}, {0}}, {{1}, {0}, {0}, {0}, {8}, {8}, {8}, {8}, {8}, {1}}, {{1}, {0}, {0}, {0}, {8}, {0}, {0}, {1}, {1}, {1}}, {{8}, {0}, {0}, {0}, {8}, {0}, {0}, {1}, {1}, {1}}, {{1}, {0}, {0}, {1}, {1}, {1}, {1}, {1}, {0}, {0}}, {{1}, {1}, {1}, {1}, {1}, {1}, {0}, {0}, {0}, {0}}, {{1}, {1}, {1}, {1}, {1}, {1}, {0}, {0}, {0}, {0}}};
				break;
			}
			case 7:		//lvl 11
			{
				map = new int[][][] {{{0}, {0}, {0}, {0}, {0}, {0}, {0}, {0}, {0}, {0}}, {{0}, {0}, {0}, {0}, {0}, {0}, {0}, {0}, {0}, {0}}, {{0}, {0}, {0}, {0}, {0}, {1}, {1}, {1}, {1}, {0}}, {{0}, {0}, {0}, {1}, {1}, {1}, {0}, {0}, {1}, {1}}, {{0}, {0}, {0}, {1}, {1}, {1}, {0}, {0}, {1}, {1}}, {{0}, {0}, {0}, {1}, {0}, {0}, {0}, {1}, {1}, {1}}, {{0}, {0}, {0}, {1}, {0}, {0}, {0}, {1}, {1}, {0}}, {{0}, {0}, {0}, {1}, {1}, {1}, {2, 0, 9, 0, 0, 9, 1}, {1}, {1}, {0}}, {{0}, {0}, {0}, {1}, {1}, {1}, {1}, {1}, {1}, {0}}, {{4, 1, 0}, {4, 1, 0}, {0}, {0}, {0}, {1}, {0}, {0}, {0}, {0}}, {{1}, {1}, {1}, {0}, {0}, {1}, {0}, {0}, {0}, {0}}, {{1}, {7}, {1}, {0}, {0}, {1}, {0}, {0}, {0}, {0}}, {{1}, {1}, {1}, {1}, {1}, {1}, {0}, {0}, {0}, {0}}, {{0}, {0}, {0}, {0}, {0}, {6}, {0}, {0}, {0}, {0}}, {{0}, {0}, {0}, {0}, {0}, {0}, {0}, {0}, {0}, {0}}};
				break;
			}
			case 8:		//lvl 23
			{
				map = new int[][][] {{{1}, {1}, {1}, {2, 0, 0, 6, 0, 3, 2, 0, 4, 2}, {1}, {1}, {4, 1, 0}, {0}, {0}, {0}}, {{1}, {2, 1, 12, 6, 1, 13, 6, 2, 6, 9}, {1}, {1}, {0}, {0}, {1}, {1}, {1}, {0}}, {{1}, {1}, {1}, {1}, {0}, {0}, {1}, {5, 2, 7, 12, 2}, {1}, {0}}, {{0}, {0}, {4, 1, 1}, {0}, {0}, {0}, {1}, {1}, {1}, {0}}, {{0}, {0}, {4, 1, 0}, {0}, {0}, {0}, {8}, {8}, {8}, {0}}, {{0}, {0}, {1}, {1}, {1}, {8}, {8}, {8}, {8}, {0}}, {{0}, {0}, {1}, {7}, {1}, {8}, {8}, {8}, {8}, {4, 0, 0}}, {{0}, {0}, {1}, {1}, {1}, {8}, {8}, {8}, {8}, {1}}, {{0}, {0}, {0}, {0}, {0}, {0}, {8}, {8}, {8}, {1}}, {{0}, {0}, {0}, {0}, {0}, {0}, {1}, {1}, {1}, {1}}, {{0}, {0}, {0}, {4, 0, 0}, {1}, {1}, {1}, {6}, {1}, {1}}, {{1}, {1}, {1}, {1}, {0}, {0}, {1}, {1}, {1}, {1}}, {{1}, {3, 1, 10, 3}, {1}, {1}, {0}, {0}, {4, 0, 1}, {0}, {0}, {0}}, {{1}, {1}, {1}, {1}, {0}, {0}, {4, 0, 0}, {0}, {0}, {0}}, {{0}, {0}, {0}, {4, 0, 1}, {1}, {2, 1, 14, 3, 0, 12, 6, 0, 13, 6}, {1}, {0}, {0}, {0}}};
				break;
			}
			default:
			{
				
				break;
			}
		}
		return map;
	}
	
}
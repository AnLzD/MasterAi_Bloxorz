package com.docik.bloxorz;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.drawable.Drawable;

public class Block {
	/**
	 * Constants for block movement
	 */
	private final int NO_MOVE = 0;
	private final int MOVE_EAST = 1;
	private final int MOVE_WEST = 2;
	private final int MOVE_SOUTH = 3;
	private final int MOVE_NORTH = 4;
	private final int DOWN = 5;
	private final int UNLOCK = 6;
	private final int EXIT = 7;
	
	/**
	 * Constants for cell actions  
	 */
	private final int CLOSE = 0;
	private final int OPEN = 1;
	private final int TOGGLE = 2;
	private final int FALL = 3;
	
	/**
	 * Constants for block state  
	 */
	private final int SINGLE = 0;
	private final int DOUBLE = 1;
	private final int TELEPORT = 2;
	private final int ANIMATION = 3;
	
	int i, i1;
	
	int j, j1;
	
	int iStart;
	
	int jStart;
	
	boolean isHorizontal = false;
	
	boolean isNorth = false;
	
	Cell[][] cells = new Cell[15][10];
	
	/**
	 * image(s) to be drawn
	 */
	Drawable image, smallBlock1, smallBlock2;
	
	/**
	 * Block image width
	 */
	private int width;
	
	/**
	 * Block image height
	 */
	private int height;
	
	/**
	 * Image top left point
	 */
	private Point point, point1, point2;
	
	/**
	 * Resources
	 */
	Resources res;
	
	/**
	 * Direction of movement
	 */
	int direction;
	
	/**
	 * Animation frame number
	 */
	int frame = 0;
	
	/**
	 * Cell(s) under the block are checked
	 */
	private boolean checked = true;
	
	/**
	 * Flag for level reset
	 */
	private boolean resetLvl;
	
	/**
	 * Keypad locker
	 */
	boolean isLocked = false;

	boolean levelCompleted = false;
	
	/**
	 * Drawables
	 */
	Drawable image1, image2, image3, image4, image5, image6, image7, image8, image9, image10, image11, image12, image13, image14, image15, small1, small1_2, small2, small3, small4, small5, small6, small7, small8, small9, small10, small11, small12, small13, small14, small15, fall1, fall2, fall3, fall4, fall5, fall6;
	
	Drawable[] fall = new Drawable[6];
	
	private int action;
	
	private int fallingSpeed = 0;
	
	int state = SINGLE;
	
	int[] teleportCoordinates;
	
	private int animationFrame = 0;
	
	boolean draw = true;
	
	boolean firstBlockActive = true;
	
	
	Block(Resources res,  int i, int j, Cell[][] cells)
	{
		this.i = i;
		this.j = j;
		iStart = i;
		jStart = j;
		this.cells = cells;
		this.image = res.getDrawable(R.drawable.block_1);
		
		image1 = res.getDrawable(R.drawable.block_1);
		image2 = res.getDrawable(R.drawable.block_2);
		image3 = res.getDrawable(R.drawable.block_3);
		image4 = res.getDrawable(R.drawable.block_4);
		image5 = res.getDrawable(R.drawable.block_5);
		image6 = res.getDrawable(R.drawable.block_6);
		image7 = res.getDrawable(R.drawable.block_7);
		image8 = res.getDrawable(R.drawable.block_8);
		image9 = res.getDrawable(R.drawable.block_9);
		image10 = res.getDrawable(R.drawable.block_10);
		image11 = res.getDrawable(R.drawable.block_11);
		image12 = res.getDrawable(R.drawable.block_12);
		image13 = res.getDrawable(R.drawable.block_13);
		image14 = res.getDrawable(R.drawable.block_14);
		image15 = res.getDrawable(R.drawable.block_15);
		
		small1 = res.getDrawable(R.drawable.small1);
		small1_2 = res.getDrawable(R.drawable.small1);
		small2 = res.getDrawable(R.drawable.small2);
		small3 = res.getDrawable(R.drawable.small3);
		small4 = res.getDrawable(R.drawable.small4);
		small5 = res.getDrawable(R.drawable.small5);
		small6 = res.getDrawable(R.drawable.small6);
		small7 = res.getDrawable(R.drawable.small7);
		small8 = res.getDrawable(R.drawable.small8);
		small9 = res.getDrawable(R.drawable.small9);
		small10 = res.getDrawable(R.drawable.small10);
		small11 = res.getDrawable(R.drawable.small11);
		small12 = res.getDrawable(R.drawable.small12);
		small13 = res.getDrawable(R.drawable.small13);
		small14 = res.getDrawable(R.drawable.small14);
		small15 = res.getDrawable(R.drawable.small15);
		
		fall[0] = res.getDrawable(R.drawable.fall1);
		fall[1] = res.getDrawable(R.drawable.fall2);
		fall[2] = res.getDrawable(R.drawable.fall3);
		fall[3] = res.getDrawable(R.drawable.fall4);
		fall[4] = res.getDrawable(R.drawable.fall5);
		fall[5] = res.getDrawable(R.drawable.fall6);
		
		smallBlock1 = small1;
		smallBlock2 = small1_2;
		
		width = 129;
		height = 75;
		
		initPoint(i, j);
		
		setBounds();
	}
	
	public void moveNorth()
	{
		direction = MOVE_NORTH;
	}
	
	void moveSouth()
	{
		direction = MOVE_SOUTH;
	}
	
	void moveWest()
	{
		direction = MOVE_WEST;
	}
	
	void moveEast()
	{
		direction = MOVE_EAST;
	}

	void refresh()
	{
		switch(direction)
		{
			case DOWN:
				point.y+=fallingSpeed;
				setBounds();
				fallingSpeed+=5;
				
				if(point.y>600)
				{
					action = UNLOCK;
					setReset(true);
				}
				break;
			case MOVE_EAST:
				if(isHorizontal)
				{
					if(isNorth)
					{
						if(frame==0)
						{
							j+=1;
							initPoint(i+2, j);
							setImage(image6, 1);
						}else if(frame==1)
						{
							setImage(image7, 2);
						}	
						else
						{
							setImage(image2, 0);
							direction = UNLOCK;

							checked = false;
						}
					}else
					{
						if(frame==0)
						{
							j+=2;
							initPoint(i, j);
							setImage(image8, 1);
						}else if(frame==1)
						{
							setImage(image9, 2);
						}	
						else
						{
							setImage(image1, 0);
							direction = UNLOCK;
							isHorizontal = false;

							checked = false;
						}
					}
				}else
				{
					if(frame==0)
					{
						setImage(image14, 1);
					}else if(frame==1)
					{
						setImage(image15, 2);
					}	
					else
					{
						j+=1;
						initPoint(i, j+2);
						setImage(image3, 0);
						direction = UNLOCK;
						isHorizontal = true;
						isNorth = false;

						checked = false;
					}
				}
				break;
				
			case MOVE_NORTH:
				if(isHorizontal)
				{
					if(isNorth)
					{
						if(frame==0)
						{
							i-=1;
							initPoint(i, j);
							setImage(image13, 1);
						}else if(frame==1)
						{
							setImage(image12, 2);
						}else
						{
							setImage(image1, 0);
							direction = UNLOCK;
							isHorizontal = false;

							checked = false;
						}
					}else
					{
						if(frame==0)
						{
							setImage(image4, 1);
						}else if(frame==1)
						{
							setImage(image5, 2);
						}else
						{
							i-=1;
							initPoint(i, j+2);
							setImage(image3, 0);
							direction = UNLOCK;

							checked = false;
						}
					}
				}else
				{
					if(frame==0)
					{
						setImage(image10, 1);
					}else if(frame==1)
					{
						setImage(image11, 2);
					}	
					else
					{
						setImage(image2, 0);
						direction = UNLOCK;
						i-=2;
						isHorizontal = true;
						isNorth = true;

						checked = false;
					}
				}
				break;
				
			case MOVE_WEST:
				if(isHorizontal)
				{
					if(isNorth)
					{
						if(frame==0)
						{
							setImage(image7, 1);
						}else if(frame==1)
						{
							setImage(image6, 2);
						}else
						{
							j-=1;
							initPoint(i+2, j);
							setImage(image2, 0);
							direction = UNLOCK;

							checked = false;
						}
					}else
					{
						if(frame==0)
						{
							j-=1;
							initPoint(i, j);
							setImage(image15, 1);
						}else if(frame==1)
						{
							setImage(image14, 2);
						}else
						{
							setImage(image1, 0);
							direction = UNLOCK;
							isHorizontal = false;

							checked = false;
						}
					}
				}else
				{
					if(frame==0)
					{
						setImage(image9, 1);
					}else if(frame==1)
					{
						setImage(image8, 2);
					}	
					else
					{
						setImage(image3, 0);
						direction = UNLOCK;
						j-=2;
						isHorizontal = true;
						isNorth = false;

						checked = false;
					}
				}
				break;
				
			case MOVE_SOUTH:
				if(isHorizontal)
				{
					if(isNorth)
					{
						if(frame==0)
						{
							i+=2;
							initPoint(i, j);
							setImage(image11, 1);
						}else if(frame==1)
						{
							setImage(image10, 2);
						}	
						else
						{
							setImage(image1, 0);
							direction = UNLOCK;
							isHorizontal = false;

							checked = false;
						}
					}else
					{
						if(frame==0)
						{
							i+=1;
							initPoint(i, j+2);
							setImage(image5, 1);
						}else if(frame==1)
						{
							setImage(image4, 2);
						}else
						{
							setImage(image3, 0);
							direction = UNLOCK;
							
							checked = false;
						}
					}
				}else
				{
					if(frame==0)
					{
						initPoint(i, j);
						setImage(image12, 1);
					}else if(frame==1)
					{
						setImage(image13, 2);
					}	
					else
					{
						i+=1;
						initPoint(i+2, j);
						setImage(image2, 0);
						direction = UNLOCK;
						isHorizontal = true;
						isNorth = true;
						
						checked = false;
					}
				}
				break;
			case UNLOCK:
				direction = NO_MOVE;
				unlockKeypad();
				break;
			case EXIT:
				if(animationFrame<6)
				{
					image = fall[animationFrame];
					setBounds();
					animationFrame++;
				}else
				{
					levelCompleted = true;
				}
				break;
		}
		/*
		 * check cells under the block
		 */
		if(!checked)
		{
			if(isHorizontal)
			{
				if(isNorth)
				{
					checkCellHorizontal(i, j);
					checkCellHorizontal(i+1, j);
					checked = true;
				}else
				{
					checkCellHorizontal(i, j);
					checkCellHorizontal(i, j+1);
					checked = true;
				}
			}else
			{
				checkCellVertical(i, j);
				checked = true;
			}
		}
	}
	
	public void refreshSmallBlock()
	{
		if(state==TELEPORT)
		{
			initPoint1(this.i, this.j);
			setBounds1();
			
			initPoint2(i1, j1);
			setBounds2();
			
			firstBlockActive = true;
			
			state=ANIMATION;
		}
		else if(state==ANIMATION)
		{
			//someAnimation
			if(animationFrame<10)
			{
				draw = !draw;
				animationFrame++;
			}else
			{
				animationFrame = 0;
				state = DOUBLE;
				direction = UNLOCK;
			}
		}
		else
		{
			switch(direction)
			{
				case DOWN:
					
					break;
				case MOVE_EAST:
					if(firstBlockActive)
					{
						if(frame==0)
						{
							setSmallImage1(small14, 1);
						}else if(frame==1)
						{
							setSmallImage1(small15, 2);
						}	
						else
						{
							j+=1;
							initPoint1(i, j);
							setSmallImage1(small1, 0);
							direction = UNLOCK;
							
							checked = false;
						}
					}else
					{
						if(frame==0)
						{
							setSmallImage2(small14, 1);
						}else if(frame==1)
						{
							setSmallImage2(small15, 2);
						}	
						else
						{
							j1+=1;
							initPoint2(i1, j1);
							setSmallImage2(small1_2, 0);
							direction = UNLOCK;
							
							checked = false;
						}
					}
					break;
					
				case MOVE_NORTH:
					if(firstBlockActive)
					{
						if(frame==0)
						{
							setSmallImage1(small10, 1);
						}else if(frame==1)
						{
							setSmallImage1(small11, 2);
						}	
						else
						{
							i-=1;
							initPoint1(i, j);
							setSmallImage1(small1, 0);
							direction = UNLOCK;
							
							checked = false;
						}
					}else
					{
						if(frame==0)
						{
							setSmallImage2(small10, 1);
						}else if(frame==1)
						{
							setSmallImage2(small11, 2);
						}	
						else
						{
							i1-=1;
							initPoint2(i1, j1);
							setSmallImage2(small1_2, 0);
							direction = UNLOCK;
							
							checked = false;
						}
					}
					break;
					
				case MOVE_WEST:
					if(firstBlockActive)
					{
						if(frame==0)
						{
							setSmallImage1(small9, 1);
						}else if(frame==1)
						{
							setSmallImage1(small8, 2);
						}	
						else
						{
							j-=1;
							initPoint1(i, j);
							setSmallImage1(small1, 0);
							direction = UNLOCK;
							
							checked = false;
						}
					}else
					{
						if(frame==0)
						{
							setSmallImage2(small9, 1);
						}else if(frame==1)
						{
							setSmallImage2(small8, 2);
						}	
						else
						{
							j1-=1;
							initPoint2(i1, j1);
							setSmallImage2(small1_2, 0);
							direction = UNLOCK;
							
							checked = false;
						}
					}
					break;
					
				case MOVE_SOUTH:
					if(firstBlockActive)
					{
						if(frame==0)
						{
							i+=1;
							setSmallImage1(small12, 1);
						}else if(frame==1)
						{
							setSmallImage1(small13, 2);
						}	
						else
						{
							initPoint1(i, j);
							setSmallImage1(small1, 0);
							direction = UNLOCK;
							
							checked = false;
						}
					}else
					{
						if(frame==0)
						{
							i1+=1;
							setSmallImage2(small12, 1);
						}else if(frame==1)
						{
							setSmallImage2(small13, 2);
						}	
						else
						{
							initPoint2(i1, j1);
							setSmallImage2(small1_2, 0);
							direction = UNLOCK;
							
							checked = false;
						}
					}
					break;
				case UNLOCK:
					unlockKeypad();
					direction = NO_MOVE;
					break;
			}
			
			
			if(!checked)
			{
				checkCellHorizontal(i, j);
				checkCellHorizontal(i1, j1);
				checkMerge();
				checked = true;
			}
		}
	}
	
	void checkMerge()
	{
		if(i1==i+1 && j1==j || i1==i && j1==j+1)
		{
			isHorizontal = true;
			
			if(j1==j)
			{
				isNorth = true;
				image = image2;
				initPoint(i+2, j);
			}
			else
			{
				isNorth = false;
				image = image3;
				initPoint(i, j+2);
			}
			
			setBounds();
			
			state = SINGLE;
		}else if(i1==i && j1==j-1)
		{
			isHorizontal = true;
			isNorth = false;
			
			i = i1;
			j = j1;
			image = image3;
			initPoint(i, j+2);
			
			setBounds();
			
			state = SINGLE;
		}else if(i1==i-1 && j1==j)
		{
			isHorizontal = true;
			isNorth = true;
			
			i = i1;
			j = j1;
			image = image2;
			initPoint(i+2, j);
			
			setBounds();
			
			state = SINGLE;
		}
	}
	
	private void checkCellHorizontal(int i, int j)
	{
		if(i<0 || i>14 || j<0 || j>9 || cells[i][j]==null || !cells[i][j].isOpened())
		{
			setReset(true);
		}
		else if(cells[i][j].getType()==2)
		{
			onPush(i, j);
		}
	}
	
	private void checkCellVertical(int i, int j)
	{
		if(i<0 || i>14 || j<0 || j>9 || cells[i][j]==null || !cells[i][j].isOpened())
		{
			//overTheEdge
			setReset(true);
		}
		else if(cells[i][j].getType()==2||cells[i][j].getType()==3)
		{
			//buttons
			onPush(i, j);
		}
		else if(cells[i][j].getType()==8)
		{
			//red cell
			cells[i][j].setAction(FALL);
			setDirection(DOWN);
		}
		else if(cells[i][j].getType()==5)
		{
			//teleport
			state = TELEPORT;
			
			teleportCoordinates = cells[i][j].getTeleportCoordinates();
			
			this.i = teleportCoordinates[0];
			this.j = teleportCoordinates[1];
			i1 = teleportCoordinates[2];
			j1 = teleportCoordinates[3];
			
		}
		else if(cells[i][j].getType()==7)
		{
			//exit
			direction = EXIT;
		}
	}
	
	private void setDirection(int direction) {
		this.direction = direction;
	}

	void onPush(int i, int j)
	{
		int c;
		int[] bridgesCoordinates = cells[i][j].getBridgesCoordinates();
		for(c=0; c<bridgesCoordinates.length; c+=3)
		{
			switch(bridgesCoordinates[c])
			{
				case 0:
					cells[bridgesCoordinates[c+1]][bridgesCoordinates[c+2]].setAction(CLOSE);
					break;
				case 1:
					cells[bridgesCoordinates[c+1]][bridgesCoordinates[c+2]].setAction(OPEN);
					break;
				case 2:
					cells[bridgesCoordinates[c+1]][bridgesCoordinates[c+2]].setAction(TOGGLE);
					break;
				default:
					break;	
			}
		}
	}
	
	private void setImage(Drawable image, int frame)
	{
		this.image = image;
		setBounds();
		this.frame = frame;
	}
	
	private void setSmallImage1(Drawable image, int frame)
	{
		this.smallBlock1 = image;
		setBounds1();
		this.frame = frame;
	}
	
	private void setSmallImage2(Drawable image, int frame)
	{
		this.smallBlock2 = image;
		setBounds2();
		this.frame = frame;
	}
	
	void setBounds()
	{
		image.setBounds(point.x, point.y, point.x+width, point.y+height);
	}
	
	void draw(Canvas canvas)
	{
		image.draw(canvas);
	}
	
	void drawSmallBlocks(Canvas canvas)
	{
		if(firstBlockActive)
		{
			if(i<i1 || i==i1&&j<j1)
			{
				if(draw)
					smallBlock1.draw(canvas);
				smallBlock2.draw(canvas);
			}else
			{
				smallBlock2.draw(canvas);
				if(draw)
					smallBlock1.draw(canvas);
			}
		}else
		{
			if(i1<i || i==i1&&j1<j)
			{
				if(draw)
					smallBlock2.draw(canvas);
				smallBlock1.draw(canvas);
			}else
			{
				smallBlock1.draw(canvas);
				if(draw)
					smallBlock2.draw(canvas);
			}
		}
	}
	
	private void initPoint(int i, int j)
	{
		point = new Point(320-i*26+j*9, -4+j*14+i*5);
	}
	
	private void initPoint1(int i, int j)
	{
		point1 = new Point(320-i*26+j*9, -4+j*14+i*5);
	}
	
	private void initPoint2(int i1, int j1)
	{
		point2 = new Point(320-i1*26+j1*9, -4+j1*14+i1*5);
	}
	
	public void setReset(boolean reset)
	{
		resetLvl = reset;
	}
	
	public boolean getReset()
	{
		return resetLvl;
	}
	
	boolean levelCompleted()
	{
		return levelCompleted;
	}
	
	int getDirection()
	{
		return direction;
	}
	
	int getI()
	{
		return i;
	}
	
	int getJ()
	{
		return j;
	}
	
	void setBounds1()
	{
		smallBlock1.setBounds(point1.x, point1.y, point1.x+width, point1.y+height);
	}
	
	void setBounds2()
	{
		smallBlock2.setBounds(point2.x, point2.y, point2.x+width, point2.y+height);
	}
	
	int getState()
	{
		return state;
	}
	
	void changeActiveBlock()
	{
		if(state==DOUBLE)
		{
			firstBlockActive = !firstBlockActive;
			state = ANIMATION;
		}
	}
	
	boolean isLocked()
	{
		return isLocked;
	}
	
	void lockKeypad()
	{
		isLocked = true;
	}
	
	void unlockKeypad()
	{
		isLocked = false;
	}
}

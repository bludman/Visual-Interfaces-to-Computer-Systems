import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;

import javax.imageio.ImageIO;


public class JImage {
	private int mWidth, mHeight, mNumChannels, mStride;
	int mData[], mPixel[];
	
    protected JImage(JImage original) {
    	mWidth = original.mWidth;
    	mHeight = original.mHeight;
    	mNumChannels = original.mNumChannels;
    	mStride = original.mStride;
    	mData = Arrays.copyOf(original.mData, mWidth * mHeight * mNumChannels);
    	mPixel = Arrays.copyOf(original.mPixel, mNumChannels);
    }

    public JImage copy() {
        return new JImage(this);
    }

    static public JImage getBlackImage(int width, int height, int numChannels)
    {
    	JImage output = new JImage(width, height, numChannels);
    	int [] pixel = {0, 0, 0};
    	for(int y = 0; y < height; ++y)
    	{
    		for(int x = 0; x < width; ++x)
    		{
    			output.setPixel(x, y, pixel);
    		}
    	}
    	return output;
    }
    
	public JImage(int width, int height, int numChannels)
	{
		mWidth = width;
		mHeight = height;
		mNumChannels = numChannels;
		mData = new int[width * height * numChannels];
		mPixel = new int[numChannels];
		mStride = mWidth * mNumChannels;
	}

	public JImage(BufferedImage bi)
	{
		mWidth = bi.getWidth();
		mHeight = bi.getHeight();
		mNumChannels = 3;
		mData = new int[mWidth * mHeight * mNumChannels];
		mPixel = new int[mNumChannels];
		mStride = mWidth * mNumChannels;

		int index = 0;
		int col;
		for(int y = 0; y < mHeight; y++)
		{
			for(int x = 0; x < mWidth; x++)
			{
				col = bi.getRGB(x,y);
				Color lookup = new Color(col);
				mData[index] = lookup.getRed(); index ++;
				mData[index] = lookup.getGreen(); index ++;
				mData[index] = lookup.getBlue(); index ++;
			}
		}
	}

	public int getWidth()
	{
		return mWidth;
	}

	public int getHeight()
	{
		return mHeight;
	}

	public int getNumChannels()
	{
		return mNumChannels;
	}

	public int getStride()
	{
		return mStride;
	}

	public int [] getPixel(int horizontal, int vertical)
	{
		int offset = vertical*mStride + horizontal*mNumChannels;

		for (int i = 0; i < mNumChannels; i++)
		{
			mPixel[i] = mData[offset + i]; 
		}

		return mPixel;
	}

	public void setPixel(int horizontal, int vertical, int pixel[])
	{
		int offset = vertical*mStride + horizontal*mNumChannels;

		for (int i = 0; i < mNumChannels; i++)
		{
//			System.out.print(horizontal);
//			System.out.print(" ");
//			System.out.print(vertical);
//			System.out.print(" ");
//			System.out.print(mStride);
//			System.out.print(" ");
//			System.out.print(mNumChannels);
//			System.out.print(" ");
//			System.out.println(i);
			mData[offset + i] = pixel[i]; 
		}
	}

	public BufferedImage getBufferedImage()
	{
		BufferedImage bi = new BufferedImage(mWidth, mHeight, BufferedImage.TYPE_INT_RGB);
		int offset;
		int rgb;

		//		System.out.println(mHeight);
		//		System.out.println(mWidth);
		//synthesize the buffered image
		for (int y = 0; y < mHeight; y++)
		{
			offset = y*mStride;
			for (int x = 0; x < mWidth; x++)
			{
				if(mNumChannels == 3)
				{
//					System.out.print(mData[offset]);
//					System.out.print(" ");
//					System.out.print(mData[offset+1]);
//					System.out.print(" ");
//					System.out.println(mData[offset+2]);

					rgb = mData[offset] * 256 * 256 +
					mData[offset + 1] * 256 +
					mData[offset + 2];
					bi.setRGB(x, y, rgb);
					offset += 3;
				}
				else if (mNumChannels == 1)
				{
//					if(mData[offset] != 0)
//						System.out.println(mData[offset]);
					rgb = mData[offset] * 256 * 256 +
					mData[offset] * 256 +
					mData[offset];
					bi.setRGB(x, y, rgb);					
					offset += 1;
				}
			}
		}
		return bi;
	}
	public void writeOut(String filePath) throws IOException
	{
		BufferedImage bi = getBufferedImage();
		OutputStream outputStream = new FileOutputStream (filePath);
		ImageIO.write(bi, "jpg", outputStream);
	}
}

package logic;

import java.io.File;
import java.util.ArrayList;


import org.jcodec.api.FrameGrab;
import org.jcodec.common.io.NIOUtils;
import org.jcodec.common.model.Picture;

/**
 * Carga un archivo de video a memoria principal
 * @author Felipe Velásquez
 *
 */
public class VideoLoader {
	
	public static ArrayList<Picture> LoadVideo() {
		
		ArrayList<Picture> frames = new ArrayList<Picture>();
		
		try {
			File file = new File("video.mp4");
			FrameGrab grab = FrameGrab.createFrameGrab(NIOUtils.readableChannel(file));
			
			//Picture picture = FrameGrab.getFrameFromFile(new File("video.mp4"), 35);
			
			
			Picture picture;
			while (null != (picture = grab.getNativeFrame())) {
				frames.add(picture);
			}
					
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
		return frames;

		
	}

}

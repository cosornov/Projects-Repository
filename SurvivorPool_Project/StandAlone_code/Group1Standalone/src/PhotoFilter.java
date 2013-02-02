import java.io.File;

import javax.swing.filechooser.FileFilter;


public class PhotoFilter extends FileFilter {

	/** overridden method that accepts only photos
	 * that end with jpg, gif, or png
	 * @return true or false if its a photo
	 */
	public boolean accept(File f) {
		/*boolean accept = f.isDirectory();
		
		if(!accept)
		{
			//suffix of the file if not a directory
			String suffix = getSuffix(f);
			if(suffix!=null)
			{
				accept = (suffix.equals("jpg") || suffix.equals("png") || suffix.equals("gif"));
			}
		}
		
		return accept;*/
	        return f.isDirectory() || f.getName().toLowerCase().endsWith(".jpg")
	        	|| f.getName().endsWith(".gif");
	}

	//override the description to let the user know only photos are accepted
	/**
	* @param message to user specifying types of files they may choose from
	*/
	public String getDescription() {
		return "Picture Files(*.jpg or *.gif)";
	}
	
	/*private String getSuffix(File f)
	{
		//get name of file chosen
		String filePath = f.getName();
		//variable used to hold the file extension
		String suffix = null;
		//find the index of the last '.'
		int i = filePath.lastIndexOf(".");
		
		//make sure the file is not a hidden file or ends with a '.'
		if(i>0 && i<filePath.length()-1)
		{
			//make sure the file suffix is lowercase
			suffix = filePath.substring(i+1).toLowerCase();
		}
		
		return suffix;
		
	}*/

}

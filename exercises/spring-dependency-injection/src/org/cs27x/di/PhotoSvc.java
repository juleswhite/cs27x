package org.cs27x.di;

import java.util.List;

import org.cs27x.di.model.Photo;
import org.cs27x.di.model.Result;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

/**
 * This class implements a basic photo management service. The
 * service is exposed as a HTTP-based service using the Java
 * Spring framework.
 * 
 * 
 * @author jules
 *
 */
@Controller
public class PhotoSvc {

	/**
	 * Step 1: Create implementations of the DataStore, TagManager, and PhotoStorage
	 * interfaces. Create unit tests for both implementations.
	 * 
	 */
	
	/**
	 * Step 2: Add member variables to PhotoSvc to hold references to 
	 * TagManager and PhotoStorage instances. You should modify war/WEB-INF/dispatcher-servlet.xml 
	 * to use Spring to inject these dependencies into the PhotoSvc. You can use either
	 * constructor or setter injection:
	 * 
	 * http://docs.spring.io/spring/docs/2.5.6/reference/beans.html#beans-factory-collaborators
	 * 
	 * 
	 * Step 2a: Export your implementation of the DataStore in a jar file and exchange implementations
	 * with another team. Replace your DataStore within the Spring configuration file with the other
	 * team's and use it for the rest of the assignment.
	 * 
	 * 
	 */
	
	public PhotoSvc(){
	}
	
	/**
	 * 
	 * A simple example to help you.
	 * 
	 * Access with:
	 * 
	 * http://localhost:8080/cs27x/add?a=1&b=2
	 * 
	 * @param a
	 * @param b
	 * @return
	 */
	@RequestMapping("add")
	@ResponseBody
	public Result add(int a, int b){
		return new Result(true,a+b);
	}
	
	/**
	 * 
	 * Step 3: Modify the upload method to use your PhotoStorage
	 * implementation to store the photo. Create a unit test to
	 * ensure that your modified version of the method works.
	 * 
	 * To test the actual web version of this method with an HTTP
	 * request, use the test form here:
	 * 
	 * http://localhost:8080/cs27x/uploadForm
	 * 
	 * @param imgdata
	 * @throws Exception
	 */
	@RequestMapping("upload")
	@ResponseBody
	public String addPhoto(MultipartFile imgdata) throws Exception {
		Photo photo = null;
		return photo.getId();
	}

	
	/**
	 * Step 4: Modify the imageList method to use your PhotoStorage implementation
	 * to retrieve the list of Photos. Create a unit test to ensure that your method works.
	 * 
	 * @return
	 */
	@RequestMapping("imageList")
	@ResponseBody
	public List<Photo> imageList(){
		List<Photo> photos = null;
		return photos;
	}
	
	/**
	 * Step 5a: Create a new method that is mapped to the 
	 * "/cs27x/imageListByTag" and takes a tag as
	 * a parameter and returns the list of images with that tag. 
	 * Create a unit test to ensure that your method works.
	 * 
	 * @return
	 */
	
	/**
	 * Step 5b: Create a new method that is mapped to  
	 * "/cs27x/addTagtoImage", takes an image ID and a tag,
	 * and associates the given image with the tag.
	 * Create a unit test to ensure that your method works.
	 * 
	 * @return
	 */

	/**
	 * Step 5c: Create a new method that is mapped to  
	 * "/cs27x/removeTagFromImage", takes an image ID and a tag,
	 * and removes the association between the given image and tag.
	 * Create a unit test to ensure that your method works.
	 * 
	 * @return
	 */
	
	/**
	 * Step 6: Modify the method below to lookup the correct image
	 * based on the passed in ID and return its raw image data.
	 * 
	 * @return
	 */
	@RequestMapping("image")
	@ResponseBody
	public byte[] getPhoto(String id){
		return null;
	}
	
	
	@RequestMapping("uploadForm")
	public void uploadForm(){}
	
	@RequestMapping("imageListView")
	public void imageListView(){}
}
